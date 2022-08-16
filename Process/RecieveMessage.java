package Process;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ListIterator;

import peer.HasCompleteFile;
import peer.MessageBody;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import peer.message.Have;
import peer.message.Piece;
import fileManager.LogWritter;

public class RecieveMessage extends Thread {

	// Declaring soc
	private Socket soc;
	// Declaring ID for remote Peer
	private int IDForRemotePeer;
	// Declaring Size of Chunk
	private long chunkSize;

	public byte[] updateBitField(byte[] fld, int chunkID) {
		int mjj = (chunkID - 1) / 8;
		int ste = 7 - ((chunkID - 1) % 8);
		fld[mjj + 5] = (byte) (fld[mjj + 5] | (1<<ste));
		// Return Field
		return fld;
	}

	public RecieveMessage(Socket soc, long chunkSize) {
		this.soc = soc;
		this.chunkSize = chunkSize;

		ListIterator<Peer> it = PeerProcess.peers.listIterator();

		while(it.hasNext()) {
			Peer p = (Peer)it.next();

			if(p.getSocket().equals(soc)) {
				IDForRemotePeer = p.getPeerID();
			}
		}
	}

	private byte[] receiveMessage() {

		// Declaring message
		byte[] msg = null;
		try {
			// Creating object for ObjectInputStream
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
			// Read object
			msg = (byte[]) ois.readObject();
		}
		catch (Exception e) {
			// Exception
			System.exit(0);
		}
		// Return message
		return msg;
	}


	@Override
	public void run() {
		
		while(true) {
			
			byte[] msg = receiveMessage();
			byte[] interim = new byte[4];
			int mjj=0;
			int yxz=0;
			ListIterator<Peer> it = PeerProcess.peers.listIterator();
			int chunkNo = ByteBuffer.wrap(interim).getInt();
			int MsgType = msg[4];
			
			switch(MsgType){
				case 0:
					//chk
					break;
				
				case 1:
					//unchk
					break;
					
				case 2:
					//determinant
				
					System.out.println("Interested message received from " + IDForRemotePeer);
					System.out.println();
					LogWritter.receiveInterested(IDForRemotePeer);
					break;
					
				case 3:
					//not determinant
				
					System.out.println("Not Interested message received from " + IDForRemotePeer);
					System.out.println();
					LogWritter.noiint(IDForRemotePeer);
					break;
					
				case 4:
					//obtain
					
					yxz = 5;
					mjj = 0;
					while(mjj < interim.length){
						interim[mjj] = msg[yxz];
						yxz=yxz+1;
						mjj=mjj+1;
					}

					while(it.hasNext()) {
						Peer p = (Peer)it.next();

						if(p.getSocket().equals(soc)) {
							byte[] fld = p.getBitfield();

							try {
								synchronized(fld) {
									fld = updateBitField(fld, chunkNo);
									p.setBitfield(fld);
								}
							} catch (Exception e) {
								System.err.println(e);
							}
						}
					}

					System.out.println("Have message received from " + IDForRemotePeer + " for piece " + chunkNo);
					System.out.println();
					LogWritter.receiveHave(IDForRemotePeer, chunkNo);
					break;
					
				case 6:
					//request
					
					yxz = 5;
					mjj = 0;
					while(mjj < interim.length){
						interim[mjj] = msg[yxz];
						yxz=yxz+1;
						mjj=mjj+1;
					}
					int chunkNo1 = ByteBuffer.wrap(interim).getInt();
					Integer incn = new Integer(chunkNo1);
					
					//send piece
					Piece piece = PeerProcess.hm.get(incn);
					
					//debugging start
					System.out.println("piece " + chunkNo1 + " requested from " + IDForRemotePeer);
					System.out.println();
					//debugging end
					
					synchronized (PeerProcess.messageBody) {
						MessageBody mb = new MessageBody();
						mb.setSocket(soc);
						mb.setMessage(piece.piece);
						PeerProcess.messageBody.add(mb);
					}
					break;
					
				case 7:
					//piece
				
					byte ind[] = new byte[4];
					
					yxz = 5;
					mjj = 0;
					while(mjj < ind.length){
						ind[mjj] = msg[yxz];
						yxz=yxz+1;
						mjj=mjj=1;
					}
					int chunkID = ByteBuffer.wrap(ind).getInt();
					Integer noci = new Integer(chunkID);
					byte[] chunk1 = new byte[msg.length - 9];
					mjj = 0;
					while(mjj < chunk1.length){
						chunk1[mjj] = msg[yxz];
						yxz=yxz+1;
						mjj=mjj+1;
					}
				
					if(chunk1.length == chunkSize && !PeerProcess.hm.containsKey(noci)) {
						
						Piece p1 = new Piece(chunkID, chunk1);
						
						try {
							synchronized(PeerProcess.hm) {
								PeerProcess.hm.put(noci, p1);
								Thread.sleep(30);
							}
						} catch (Exception e) {
							System.err.println(e);
						}
						
						
						System.out.println("piece " + chunkID + " received from " + IDForRemotePeer);
						System.out.println();
						LogWritter.downloadPiece(IDForRemotePeer, chunkID);

						
						try {
							synchronized(BitField.bitfield) {
								BitField.updateBitField(chunkID);
								Thread.sleep(20);
							}
						} 
						catch (Exception e) {
							System.err.println(e);
						}
						
						//send obtain to all peers
						Have have = new Have(chunkID);

						while(it.hasNext()) {
							Peer peer = (Peer)it.next();
							
							synchronized (PeerProcess.messageBody) {
								MessageBody mb = new MessageBody();
								mb.setSocket(peer.getSocket());
								mb.setMessage(have.obtain);
								PeerProcess.messageBody.add(mb);
							}
						}
					}
					break;
					
				case 8:
					synchronized(PeerProcess.hasDownloadedCompleteFile) {
					
					ListIterator<HasCompleteFile> iter = PeerProcess.hasDownloadedCompleteFile.listIterator();
					
					while(iter.hasNext()) {
						HasCompleteFile peer = (HasCompleteFile)iter.next();
						
						if(peer.getSocket().equals(soc)) {
							peer.setHasDownLoadedCompleteFile(true);
							break;
						}
					}
					
					try {
						Thread.sleep(1);
					} catch (Exception e) {
						System.err.println(e);
					}
					}
					break;
					
				default:
					break;
			}
		}
	}
}

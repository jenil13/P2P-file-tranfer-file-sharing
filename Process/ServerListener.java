package Process;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ListIterator;

import fileManager.LogWritter;
import peer.Handshake;
import peer.HasCompleteFile;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;

public class ServerListener extends Thread {

	// Hear Port
	private int hearPort;
	// Declaring ID for my Peer
	private int SystemIdForPeer;
	// Declaring has All chunks
	private boolean hasWholeChunk;
	// Declaring Size of file
	private long fileCap;
	// Declaring Size of chunk
	private long chunkCap;
	// Declaring Total Chunks
	private int totalChunkCount;


	private void sendBitfield(Socket soc) {

		try {
			// object for ObjectOutputStream
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			// write object
			oos.writeObject(BitField.bitfield);

		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}

	}


	private void sendHandShake(Socket soc, byte[] handclasp) {

		try {
			// object for ObjectOutputStream
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			// write object
			oos.writeObject(handclasp);
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
	}


	@Override
	public void run() {
		
		try {
			@SuppressWarnings("resource")
			ServerSocket listener = new ServerSocket(hearPort);
			
			while(true) {
				Socket soc = listener.accept();
				int IDforPeer;
				
				
				//Handshake
				
				byte[] got = receiveHandShake(soc);
				
				Handshake give = new Handshake(SystemIdForPeer);
				sendHandShake(soc, give.variableForHandshake);
				
				byte[] interim = new byte[28];
				int mjj = 0;
				while(mjj<28){
					interim[mjj] = got[mjj];
					mjj=mjj+1;
				}
				
				String head = new String(interim);
				
				int jre = 0;
				byte[] interimID = new byte[4];
				mjj = 28;
				while(mjj<32){
					interimID[jre] = got[mjj];
					jre=jre+1;
					mjj=mjj+1;
				}
				
				String st = new String(interimID);
				IDforPeer = Integer.parseInt(st);

				if(head.equals("P2PFILESHARINGPROJ0000000000")) {
					
					boolean report = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int no = iter.next().intValue();
						if(no != SystemIdForPeer)
							continue;
						else
							break;
					}
					
					while(iter.hasNext()) {
						
						int no = iter.next().intValue();
						if(no == IDforPeer)
							report = true;
					}
					
					
					if(report == true) {
						
						Peer p = new Peer();
						p.setmyPeerID(SystemIdForPeer);
						p.setSocket(soc);
						p.setPeerID(IDforPeer);
						
						//give bitfield
						sendBitfield(soc);
						
						//receive bitfield
						p.setBitfield(receiveBitfield(soc));
						p.setInterested(false);
						
						PeerProcess.peers.add(p);
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(soc);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection req from " + IDforPeer);
						System.out.println();
						LogWritter.madeTCPConnection(IDforPeer);
						

						SendMessage sm = new SendMessage();
						sm.start();
						
						RequestPiece rp = new RequestPiece(IDforPeer, totalChunkCount, hasWholeChunk, fileCap, chunkCap);
						rp.start();
						
						RecieveMessage rm = new RecieveMessage(soc, chunkCap);
						rm.start();
						
					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
				
			}
			
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
	}


	private byte[] receiveHandShake(Socket soc) {

		// Declaring handshake
		byte[] handclasp = null;
		try {
			// Declaring ObjectInputStream object
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
			// Read Object
			handclasp = (byte[]) ois.readObject();
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}

		// Return handshake
		return handclasp;
	}


	private byte[] receiveBitfield(Socket soc) {

		// Declaring bitfield
		byte[] bitfld = null;
		try {
			// Declaring ObjectInputStream obj
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
			// Read object
			bitfld = (byte[]) ois.readObject();
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}

		// Return bitfield
		return bitfld;
	}

	public ServerListener(int hearPort, int IDforPeer, int totalChunkCount, boolean hasWholeChunk, long fileCap, long chunkCap) {
		// This listen to Port
		this.hearPort = hearPort;
		// Peer
		SystemIdForPeer = IDforPeer;
		// Total Chunks
		this.totalChunkCount = totalChunkCount;
		// has All chunks
		this.hasWholeChunk = hasWholeChunk;
		// Size of file
		this.fileCap = fileCap;
		// Size of chunk
		this.chunkCap = chunkCap;
	}

}

package Process;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

import peer.Handshake;
import peer.HasCompleteFile;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import fileManager.LogWritter;
import fileManager.PeerInfoParser;
import peer.message.Interested;


public class ClientListener extends Thread {

	// Declaring ID for Peer
	private String SystemIdForNeighbouringPeer;
	// Declaring Port No
	private int port_no;
	// Declaring ID of my Peer
	private int SystemIdForPeer;
	private ArrayList<String[]> client = new ArrayList<String[]>();
	// Declaring Total no of chunks
	private int TotalChunkCount;
	// Declaring has all chunks
	private boolean hasWholeChunk;
	// Declaring Size of file
	private long fileCap;
	// Declaring Size of chunk
	private long chunkCap;
	

	private byte[] receiveBitfield(Socket soc) {

		// Declaring Bitfield
		byte[] bitfld = null;
		try {
			// Creating object for ObjectInputStream
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

	private byte[] receiveHandShake(Socket soc) {

		// Handshake variable
		byte[] handclasp = null;
		try {
			// Object for ObjectInputStream
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
			// Read object
			handclasp = (byte[]) ois.readObject();
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
		// Return handshake variable
		return handclasp;
	}

	@Override
	public void run() {
		
		PeerInfoParser InfoOfPeer = new PeerInfoParser(SystemIdForPeer);
		client = InfoOfPeer.getPeerInfo();
		
		ListIterator<String[]> it = client.listIterator();
		
		while(it.hasNext()) {
			String[] val = it.next();
			SystemIdForNeighbouringPeer = val[1];
			port_no = Integer.parseInt(val[2]);
			
			try {
				Socket soc = new Socket(SystemIdForNeighbouringPeer, port_no);
				
				//Handshake
				Handshake snd = new Handshake(SystemIdForPeer);
				sendHandShake(soc, snd.variableForHandshake);
				
				byte[] got = receiveHandShake(soc);
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
				
				String stg = new String(interimID);
				int HeadID = Integer.parseInt(stg);
				
				if(head.equals("P2PFILESHARINGPROJ0000000000")) {
					
					boolean report = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int no = iter.next().intValue();
						if(no != SystemIdForPeer) {
							if(no == HeadID) {
								report = true;
								break;
							}
						}
					}
					
					if(report) {
						
						Peer p = new Peer();
						p.setmyPeerID(SystemIdForPeer);
						p.setSocket(soc);
						p.setPeerID(Integer.parseInt(val[0]));
						
						//receive bitfield
						byte[] field = receiveBitfield(soc);
						p.setBitfield(field);
						
						//send bitfield
						sendBitfield(soc);
						p.setInterested(false);
						
						synchronized (PeerProcess.peers) {
							PeerProcess.peers.add(p);
							try {
								Thread.sleep(1);
							} catch (Exception e) {
								System.err.println(e);
							}
						}
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(soc);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection request sent to " + Integer.parseInt(val[0]));
						System.out.println();
						LogWritter.makeTCPConnection(Integer.parseInt(val[0]));
						
						SendMessage sm = new SendMessage();
						sm.start();

						RequestPiece pr = new RequestPiece(Integer.parseInt(val[0]), TotalChunkCount, hasWholeChunk, fileCap, chunkCap);
						pr.start();
						
						RecieveMessage mr = new RecieveMessage(soc, chunkCap);
						mr.start();

					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
			} catch (Exception e) {
				System.err.println(e);
			}
			
		}
	}

	public ClientListener(int IDForPeer, int TotalChunkCount, boolean hasWholeChunk, long fileCap, long chunkCap) {
		// Peer ID
		SystemIdForPeer = IDForPeer;
		// Total no of chunks
		this.TotalChunkCount = TotalChunkCount;
		// has all chunks
		this.hasWholeChunk = hasWholeChunk;
		// Size of file
		this.fileCap = fileCap;
		// Size of chunk
		this.chunkCap = chunkCap;
	}

	private void sendBitfield(Socket soc) {

		try {
			// Declaring object for ObjectOutputStream
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
			// Creating object for ObjectOutputStream
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			// Write object
			oos.writeObject(handclasp);
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
	}

}

package Process;

import java.net.Socket;
import java.util.ListIterator;
import java.util.Random;

import peer.HasCompleteFile;
import peer.MessageBody;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import peer.message.Interested;
import peer.message.NotInterested;
import peer.message.Request;
import fileManager.LogWritter;
import fileManager.MergeFile;

public class RequestPiece extends Thread {

	// Declaring ID for Peer
	private int SystemIdForNeighbouringPeer;
	// Declaring ID for my Peer
	private int SystemIdForPeer;
	// Declaring Total no of chunks
	private int TotalChunkCount;
	// Declaring has all chunks
	private boolean hasWholeChunk;
	// Declaring Size of file
	private long fileCap;
	// Declaring Size of chunk
	private long chunkCap;
	// Declaring report
	private int report = 0;
	// Declaring socket
	Socket soc;


	private boolean hasCompleteFile() {

		int reportComp = 1;

		byte[] fld = BitField.bitfield;

		int mjj = 5;
		while(mjj < fld.length - 1){
			if(fld[mjj] != -1) {
				reportComp = 0;
				break;
			}
			mjj=mjj+1;
		}

		if(reportComp == 1) {

			int left = TotalChunkCount % 8;
			int abd = fld[fld.length - 1];
			String a1 = Integer.toBinaryString(abd & 255 | 256).substring(1);
			char[] a2 = a1.toCharArray();
			int[] a3 = new int[8];

			int jre = 0;
			while(jre < a2.length){
				a3[jre] = a2[jre] - 48;
				jre=jre+1;
			}

			jre = 0;
			while(jre < left){
				if(a3[jre] == 0) {
					reportComp = 0;
					break;
				}
				jre=jre+1;
			}
		}

		return (reportComp == 1);
	}



	@Override
	public void run() {
		
		if(hasWholeChunk == false) {
			
			Peer p = null;
			byte[] fld;
			int getChunk;
			
			synchronized(PeerProcess.peers) {
				
				ListIterator<Peer> it = PeerProcess.peers.listIterator();
				
				while(it.hasNext()) {
					p = (Peer)it.next();
					
					if(p.getPeerID() == SystemIdForNeighbouringPeer) {
						SystemIdForPeer = p.getmyPeerID();
						soc = p.getSocket();
						break;
					}
				}

			}

			while(true) {
				
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					// Exception
					System.err.println(e);
				}
				
				boolean completeFile = hasCompleteFile();
				
				if(completeFile) {
				
					if(!LogWritter.fileFlag) {
						LogWritter.fileFlag = true;
												
						System.out.println("Download complete");
						LogWritter.downloadComplete();
						
						MergeFile join = new MergeFile();
						join.reassemble(TotalChunkCount, SystemIdForPeer, fileCap, chunkCap);
						
						try {
							Thread.sleep(20);
						} catch (Exception e) {
							// Exception
							System.err.println(e);
						}
					}
					
					break;
				}
		
				else {
					
					if(p.isInterested()) {
						fld = p.getBitfield();
						getChunk = getPieceInfo(fld, BitField.bitfield);
						if(getChunk == 0) {
							p.setInterested(false);
							NotInterested notitd = new NotInterested();
							synchronized (PeerProcess.messageBody) {
								MessageBody mb = new MessageBody();
								mb.setSocket(soc);
								mb.setMessage(notitd.nondeterminant);
								PeerProcess.messageBody.add(mb);
							}
							report = 1;
						}
						
						else {
							Request reqst = new Request(getChunk);
							synchronized (PeerProcess.messageBody) {
								MessageBody mb = new MessageBody();
								mb.setSocket(soc);
								mb.setMessage(reqst.rqt);
								PeerProcess.messageBody.add(mb);
							}
						}
					}
					
					else {
						
						fld = p.getBitfield();
						getChunk = getPieceInfo(fld, BitField.bitfield);
						
						if(getChunk == 0) {
							if(report == 0) {
								NotInterested notitd = new NotInterested();
								
								synchronized (PeerProcess.messageBody) {
									MessageBody mb = new MessageBody();
									mb.setSocket(soc);
									mb.setMessage(notitd.nondeterminant);
									PeerProcess.messageBody.add(mb);
								}
							}
						}
						
						else {
							p.setInterested(true);
							report = 0;
							
							Interested inted = new Interested();
							
							synchronized (PeerProcess.messageBody) {
								MessageBody mb = new MessageBody();
								mb.setSocket(soc);
								mb.setMessage(inted.determinant);
								PeerProcess.messageBody.add(mb);
							}
							Request reqst = new Request(getChunk);
							synchronized (PeerProcess.messageBody) {
								MessageBody mb = new MessageBody();
								mb.setSocket(soc);
								mb.setMessage(reqst.rqt);
								PeerProcess.messageBody.add(mb);
							}
						}
						
					}
				}
			}
		}
		
		byte[] downLoadedCompleteFile = new byte[5];
		
		int mjj = 0;
		while(mjj < downLoadedCompleteFile.length - 1){
			downLoadedCompleteFile[mjj] = 0;
			mjj=mjj=1;
		}
		downLoadedCompleteFile[4] = 8;
		
		sendHasDownloadedCompleteFile(downLoadedCompleteFile);
	
		while(true) {
			boolean chk = checkAllPeerFileDownloaded();
			
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				// Exception
				System.err.println(e);
			}
			
			if(chk == true && PeerProcess.messageBody.isEmpty())
				break;
		}
		
		if(!LogWritter.fileCompleteFlag)
		{
			LogWritter.fileCompleteFlag = true;
		
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// Exception
				System.err.println(e);
			}
				
			LogWritter.closeLogger();
		}
	
		System.exit(0);
		
	}

	private int select_random_piece(int TotalMissedChunk) {

		// Declaring random
		Random rdm = new Random();
		int randNo = rdm.nextInt(TotalMissedChunk);

		// Return random num
		return randNo;

	}
	public RequestPiece(int SystemIdForNeighbouringPeer, int TotalChunkCount, boolean hasWholeChunk, long fileCap, long chunkCap) {
		// Peer ID
		this.SystemIdForNeighbouringPeer = SystemIdForNeighbouringPeer;
		// Total no of chunks
		this.TotalChunkCount = TotalChunkCount;
		// has all chunks
		this.hasWholeChunk = hasWholeChunk;
		// Size of file
		this.fileCap = fileCap;
		// Size of chunk
		this.chunkCap = chunkCap;
	}

	private void sendHasDownloadedCompleteFile(byte[] downLoadedCompleteFile) {
		
		ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();
		
		while(it.hasNext()) {
		
			HasCompleteFile peer = (HasCompleteFile)it.next();
			
			synchronized (PeerProcess.messageBody) {
				MessageBody mb = new MessageBody();
				mb.setSocket(peer.getSocket());
				mb.setMessage(downLoadedCompleteFile);
				PeerProcess.messageBody.add(mb);
			}
		}
		
	}

	private int getPieceInfo(byte[] fld, byte[] bitfld) {
		
		int[] interim = new int[TotalChunkCount];
		int ste = 0;
		int TotalMissedChunk = 0;
		int left  = TotalChunkCount % 8;

		for (int mjj = 5; mjj < bitfld.length; mjj++) {
			
			int abd = bitfld[mjj];
			int bce = fld[mjj];
			
			String a1 = Integer.toBinaryString(abd & 255 | 256).substring(1);
			char[] a2 = a1.toCharArray();
			int[] a3 = new int[8];
					
			int jre = 0;
			while(jre < a2.length){
				a3[jre] = a2[jre] - 48;
				jre=jre+1;
			}
			
			String b1 = Integer.toBinaryString(bce & 255 | 256).substring(1);
			char[] b2 = b1.toCharArray();
			int[] b3 = new int[8];
					
			jre = 0;
			while(jre < b2.length){
				b3[jre] = b2[jre] - 48;
				jre=jre+1;
			}				
			

			if(mjj < bitfld.length - 1) {
				
				jre = 0;
				while(jre < b3.length){
					if(a3[jre] == 0 && b3[jre] == 1) {
						interim[ste] = 0;
						ste=ste+1;
						TotalMissedChunk=TotalMissedChunk+1;
					}
					
					if(a3[jre] == 0 && b3[jre] == 0) {
						interim[ste] = 1;
						ste=ste+1;
					}
					
					if(a3[jre] == 1) {
						interim[ste] = 1;
						ste=ste+1;
					}
					jre=jre+1;
				}
			}
			
			else {
				jre = 0;
				while(jre < left){
					if(a3[jre] == 0 && b3[jre] == 1) {
						interim[ste] = 0;
						ste=ste+1;
						TotalMissedChunk=TotalMissedChunk+1;
					}
					
					if(a3[jre] == 0 && b3[jre] == 0) {
						interim[ste] = 1;
						ste=ste+1;
					}
					
					if(a3[jre] == 1) {
						interim[ste] = 1;
						ste=ste+1;
					}
					jre=jre+1;
				}
			}
			
		}

		
		try {
			Thread.sleep(1);
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
		

		if(TotalMissedChunk == 0)
			return 0;
		
		int[] chooseFrom = new int[TotalMissedChunk];
		
		int yxz = 0;
		int llm = 0;
		while(llm < interim.length){
			if(interim[llm] == 0) {
				chooseFrom[yxz] = llm;
				yxz=yxz+1;
			}
			llm=llm+1;
		}

		int ind = select_random_piece(TotalMissedChunk);
		int chunk = chooseFrom[ind];
		

		return (chunk + 1);
	}


	private boolean checkAllPeerFileDownloaded() {

		boolean reportAll = true;

		ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();

		while(it.hasNext()) {

			HasCompleteFile peer = (HasCompleteFile)it.next();
			if(peer.isHasDownLoadedCompleteFile()) {
				reportAll = false;
				break;
			}
		}
		// Return flagAll
		return reportAll;
	}
}

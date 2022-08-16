package fileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import peer.PeerProcess;
import peer.message.Piece;


public class MergeFile {
	
	
	public void reassemble(int TotalChunkCount, int SystemIdForNeighbouringPeer, long fileCap, long chunkSize) {
		System.out.println("Chunk");
		String basedir = System.getProperty("user.dir");
		String dir = (new File(basedir).getParent() + "/peer_" + SystemIdForNeighbouringPeer);
		File theDir = new File(dir);

		if (!theDir.exists()) {
			
			try {
				theDir.mkdir();	
			} catch(Exception e) {
				// Exception
				System.err.println(e);
			}        
		}
		
		String Name_Of_File = (dir + "/" + PeerProcess.nameofFiles);
		
		File fle = new File(Name_Of_File);
		try {
			OutputStream fos = new FileOutputStream(fle);
			
			for (int mjj = 1; mjj <= TotalChunkCount - 1; mjj++) {
				
				Integer num = new Integer(mjj);
				Piece p = PeerProcess.hm.get(num);
				byte[] interim = new byte[4];
				
				for (int j = 0; j < 4; j++) {
					interim[j] = p.piece[j];
				}
				
				int size = ByteBuffer.wrap(interim).getInt();
				size = size - 4;
				
				System.out.println(mjj + " = " + size);
				
				byte[] buffer = new byte[size];
				
				for (int pje = 0, zte = 9; pje < buffer.length && zte < p.piece.length; pje++, zte++) {
					buffer[pje] = p.piece[zte];
				}
				
				fos.write(buffer);
			}
			
			Integer num = new Integer(TotalChunkCount);
			Piece p = PeerProcess.hm.get(num);
			
			int cap = (int) (fileCap % chunkSize);
			System.out.println(cap);
			
			byte[] buffer = new byte[cap];
			
			for (int pje = 0, zte = 9; pje < buffer.length && zte < p.piece.length; pje++, zte++) {
				buffer[pje] = p.piece[zte];
			}
			
			fos.write(buffer);
			fos.close();
			
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
		
	}
}

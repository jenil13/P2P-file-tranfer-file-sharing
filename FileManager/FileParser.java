package fileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import peer.message.Piece;

public class FileParser {

	// Declaring Name of file
	private String Name_Of_File;
	// Declaring ID
	private int IndexID;
	// Declaring No of piece
	private int NoOfChunk = 1;
	// Declaring Size of chunk
	private long chunkSize;
	// Declaring HashMap
	private HashMap<Integer, Piece> map = new HashMap<Integer, Piece>();

	public HashMap<Integer, Piece> readFile() {

		String basedir = System.getProperty("user.dir");
		Name_Of_File = (new File(basedir).getParent() + "/peer_" + IndexID + "/" + Name_Of_File);

		// Declaring object of File
		File fobj = new File(Name_Of_File);
		
		try {
			
			InputStream fis = new FileInputStream(fobj);
			// Creating buffer
			byte[] buffer = new byte[(int) chunkSize];
			
			@SuppressWarnings("unused")
			int size;
			
			while((size = fis.read(buffer)) > 0) {
			
				map.put(NoOfChunk, new Piece(NoOfChunk, buffer));
				NoOfChunk++;
			}
			
			fis.close();
			
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return map;
	}

	public FileParser(int IndexID, long chunkSize, String Name_Of_File) {
		// Index ID
		this.IndexID = IndexID;
		// Chunk Size
		this.chunkSize = chunkSize;
		// FileName
		this.Name_Of_File = Name_Of_File;
	}

}

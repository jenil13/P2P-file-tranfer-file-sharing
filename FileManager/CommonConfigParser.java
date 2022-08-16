package fileManager;
import java.io.*;
import java.util.Properties;


public class CommonConfigParser {

	// Declaring Prefered Neighbour No
	private int noOfPreNeigh;
	// Declaring Interval for unchoking
    private int TimeForUnchoking;
	// Declaring Optimistic Interval for unchoking
    private int OptimisticTimeForUnchoking;
	// Declaring name of file
    private String Name_Of_File;
	// Declaring Size of file
    private long fileCap;
	// Declaring chunk size
    private long chunkSize;
    
    public void readFile() {

		String common = "/Common.cfg";
		String basedir = System.getProperty("user.dir");
		String filename = (new File(basedir).getParent() + common);

		Properties prop = new Properties();
		
		try {

			prop.load(new BufferedInputStream(new FileInputStream(filename)));
			// Get value from NumberOfPreferredNeighbors
			String nopn = prop.getProperty("NumberOfPreferredNeighbors");
			// Get value from UnchokingInterval
			String ui = prop.getProperty("UnchokingInterval");
			// Get value from OptimisticUnchokingInterval
			String oui = prop.getProperty("OptimisticUnchokingInterval");
			// Get value from FileSize
			String fs = prop.getProperty("FileSize");
			// Get value from PieceSize
			String ps = prop.getProperty("PieceSize");
			// Get value from FileName
			Name_Of_File = prop.getProperty("FileName");
			// Convert value from string to integer of NumberOfPreferredNeighbors
			this.noOfPreNeigh = Integer.parseInt(nopn);
			// Convert value from string to integer of UnchokingInterval
			TimeForUnchoking = Integer.parseInt(ui);
			// Convert value from string to integer of OptimisticUnchokingInterval
			OptimisticTimeForUnchoking = Integer.parseInt(oui);
			// Convert value from integer to long of fileSize
			fileCap = Long.parseLong(fs);
			// Convert value from integer to long of pieceSize
			chunkSize = Long.parseLong(ps);
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public int getnoOfPreNeigh() {
		return noOfPreNeigh;
	}																					

	public int getTimeForUnchoking() {
		return TimeForUnchoking;
	}

	public int getOptimisticTimeForUnchoking() {
		return OptimisticTimeForUnchoking;
	}

	public String getName_Of_File() {
		return Name_Of_File;
	}

	public long getfileCap() {
		return fileCap;
	}

	public long getChunkSize() {
		return chunkSize;
	}

}

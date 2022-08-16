package fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;

public class LogWritter {

	public static String constans =  " has send Have message to Peer ";
	public static String constants =  " - Peer ";
	public static String logs =  "/log_peer_";

	public static String hanshakemessgae = " has made handshake and is associated with Peer ";

	// Declaring variable For File
	private static File variableForFile;
	// Declaring variable for Buffer Reader
	private static BufferedWriter TodisplayOutput;
	// Declaring variable for OutputStreamWriter
	public static OutputStreamWriter osw;
	// Declaring variable for FileOutputStream
	public static FileOutputStream fos;
	//Declaring Id for peer
	private static int SystemIdForPeer;
	// Declaring total no of pieces
	private static int TotalChunkCount = 0;
	// Declaring Flag for file
	public static boolean fileFlag = false;
	// Declaring flag for completed file
	public static boolean fileCompleteFlag = false;
	// Creating object for linked list
	public static LinkedList<Integer> fileWriteOperation = new LinkedList<Integer>();


	private static byte PART_SIZE = 5;
	String name = "Name of the file you have to transfer";
	public static void fileSplit(String name) {
		//Splitting the file



		//int size = (int) file.length(), p = 0, rlen = PART_SIZE, r = 0;

//		String newName;
//		FileInputStream stream;
//		FileOutputStream segment;
//
//		//Initializing object for the file
//		//File file = new File(name);
//		//Initializing object for file input stream
//		stream = new FileInputStream(file);
//         int size1;
//		while (size1 > 0) {
//			if (size1 <= 5) {
//
//				int rlen = size1;
//			}
           // rlen = 5;
			//Initailizing object for the part of segment
			//byte[] partSegment = new byte[rlen];

//			r = stream.read(partSegment, 0, rlen);
//			size -= r;
//			p++;

			//String newname = file + ".part"+ Integer.toString(p - 1);
			//Initializing object for the output stream variable
//			FileOutputStream partFile = new FileOutputStream(new File(newname));
//
//			partFile.write(partSegment);
//
//			//Close the file
//			//Flush the file
//			partFile.close();
//			partFile.flush();
//			partSegment = null;


		}

		//stream.close();



	public static void startLogger(int SystemIdForNeighbouringPeer) {
		
		SystemIdForPeer = SystemIdForNeighbouringPeer;
		String basedir = System.getProperty("user.dir");
		String Name_Of_File = (new File(basedir).getParent() + ""+logs+"" + SystemIdForPeer + ".log");

		variableForFile = new File(Name_Of_File);
		
		try {
			fos = new FileOutputStream(variableForFile);
			osw = new OutputStreamWriter(fos);
			TodisplayOutput = new BufferedWriter(osw);
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	 

	public static void madeTCPConnection(int SystemIdForNeighbouringPeer) {
		
		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate +" "+ constants +" "+ SystemIdForPeer + ""+hanshakemessgae+"" + SystemIdForNeighbouringPeer + ".";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine
			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	public static void noiint(int SystemIdForNeighbouringPeer) {

		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate + ""+ constants+"" + SystemIdForNeighbouringPeer + " has send Not determinant message to Peer " + SystemIdForPeer + ".";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine
			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void receiveHave(int SystemIdForNeighbouringPeer, int pieceNum) {
		
		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate + ""+ constants+"" + SystemIdForNeighbouringPeer + ""+ constans+"" + SystemIdForPeer + " for the corresponding chunk ID " + pieceNum + ".";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine

			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	public static void downloadComplete() {

		if(fileFlag == true) {

			try {
				// Get Current date
				String currentDate = new Date().toString();
				// Print statement for log
				String Statement = currentDate + ""+ constants+"" + SystemIdForPeer + " has downloaded the complete file.";
				// Adding Statement
				TodisplayOutput.append(Statement);
				// Adding newLine
				TodisplayOutput.newLine();
				// Adding newLine
				TodisplayOutput.newLine();
				// Clear the stream of element
				TodisplayOutput.flush();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}


	public static void receiveInterested(int SystemIdForNeighbouringPeer) {
		
		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate + ""+ constants+"" + SystemIdForNeighbouringPeer + " has send Interested message to Peer " + SystemIdForPeer + ".";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine
			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void makeTCPConnection(int SystemIdForNeighbouringPeer) {

		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate +""+ constants+"" + SystemIdForPeer + " communicates to Peer " + SystemIdForNeighbouringPeer + " for making contact.";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine
			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void downloadPiece(int SystemIdForNeighbouringPeer, int pieceNum) {

		TotalChunkCount= TotalChunkCount+1;
		try {
			// Get Current date
			String currentDate = new Date().toString();
			// Print statement for log
			String statement = currentDate + " - From Peer " + SystemIdForNeighbouringPeer + " the chunk " + pieceNum + " has been completely downloaded to Peer " + SystemIdForPeer + ".";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			statement = "Peer " + SystemIdForPeer + " has " + TotalChunkCount + " number of chunks.";
			// Adding Statement
			TodisplayOutput.append(statement);
			// Adding newLine
			TodisplayOutput.newLine();
			// Adding newLine
			TodisplayOutput.newLine();
			// Clear the stream of element
			TodisplayOutput.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void closeLogger() {
		try {
			// Closing Log
			TodisplayOutput.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}

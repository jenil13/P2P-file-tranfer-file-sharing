package peer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import peer.message.BitField;
import peer.message.Piece;
import Process.ServerListener;
import Process.ClientListener;
import fileManager.CommonConfigParser;
import fileManager.LogWritter;
import fileManager.FileParser;
import fileManager.PeerInfoParser;

public class PeerProcess {
	public static HashMap<Integer, Piece> hm;
	public static String nameofFiles;
    private long TotalLength;

	public static LinkedList<MessageBody> messageBody = new LinkedList<MessageBody>();
    private long chunksCapacity;
    private int chunksCount;
    private int SystemIdforNeighbour;
	public static ArrayList<Peer> peers = new ArrayList<Peer>();
    private int PortNumber;
    private boolean checkingcompletefile;

    public static ArrayList<Integer> allPeerID;

    public static ArrayList<HasCompleteFile> hasDownloadedCompleteFile = new ArrayList<HasCompleteFile>();

	public static void main(String[] args)  {
		
		// Creating peer process object
    	PeerProcess objectPeerforprocesssing = new PeerProcess();
    	
		// Creating object for Common Config Parser
    	CommonConfigParser configurationsettings = new CommonConfigParser();
		// Read Common Config Parser file
		configurationsettings.readFile();

		// Storing name of Common Parser file in filename
		nameofFiles = configurationsettings.getName_Of_File();
		// Getting file size of common parser
		objectPeerforprocesssing.TotalLength = configurationsettings.getfileCap();
		// Getting piece size of common parser
		objectPeerforprocesssing.chunksCapacity = configurationsettings.getChunkSize();
	
		// Getting chunksCount value
		objectPeerforprocesssing.chunksCount = (int) Math.ceil((double) objectPeerforprocesssing.TotalLength / objectPeerforprocesssing.chunksCapacity);
		
		// Creating object for PeerInfoParser
		PeerInfoParser informationstore = new PeerInfoParser(Integer.parseInt(args[0]));
		// Reading PeerInfoParser file
		informationstore.readFile();
		// Getting all peer IDs from informationstore
		allPeerID = informationstore.getAllPeerID();
		
		// Get peer ID from peer info
		objectPeerforprocesssing.SystemIdforNeighbour = informationstore.getSystemIdForNeighbouringPeer();
		// Get PortNumber from peer info
		objectPeerforprocesssing.PortNumber = informationstore.getPort_no();
		// Check if the peer has completed file

		objectPeerforprocesssing.checkingcompletefile = informationstore.isContainsCompletedFile();

		// Setting Bitfield
		BitField.setBitfield(objectPeerforprocesssing.checkingcompletefile, objectPeerforprocesssing.chunksCount);
		
		// Start writing Logs
		LogWritter.startLogger(objectPeerforprocesssing.SystemIdforNeighbour);

		if(objectPeerforprocesssing.checkingcompletefile) {

			FileParser reader = new FileParser(objectPeerforprocesssing.SystemIdforNeighbour, objectPeerforprocesssing.chunksCapacity, nameofFiles);
			// Read from File Parser
			hm = reader.readFile();

			// Creating object for ServerListner
			ServerListener peerListener = new ServerListener(objectPeerforprocesssing.PortNumber, objectPeerforprocesssing.SystemIdforNeighbour, objectPeerforprocesssing.chunksCount, objectPeerforprocesssing.checkingcompletefile, objectPeerforprocesssing.TotalLength, objectPeerforprocesssing.chunksCapacity);
			// Listening from peer
			peerListener.start();
		}
	
		else if(!objectPeerforprocesssing.checkingcompletefile) {
			// Creating object for file parser

			hm = new HashMap<Integer, Piece>();

			// Creating object for ServerListner
			ServerListener peerListener = new ServerListener(objectPeerforprocesssing.PortNumber, objectPeerforprocesssing.SystemIdforNeighbour, objectPeerforprocesssing.chunksCount, objectPeerforprocesssing.checkingcompletefile, objectPeerforprocesssing.TotalLength, objectPeerforprocesssing.chunksCapacity);
			// Listening from peer
			peerListener.start();

			// Creating object for Client Listener
			ClientListener connect = new ClientListener(objectPeerforprocesssing.SystemIdforNeighbour, objectPeerforprocesssing.chunksCount, objectPeerforprocesssing.checkingcompletefile, objectPeerforprocesssing.TotalLength, objectPeerforprocesssing.chunksCapacity);
			// Establishing connection
			connect.start();
		}
	}
}


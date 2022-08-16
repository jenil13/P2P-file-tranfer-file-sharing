package peer;

public class Handshake {

	//This handshake consist of header and zero bits string followed by peerId
	//Handshake is called in ClientListener class
	// Declaring handshake variable
	public byte[] variableForHandshake = new byte[32];
	// Declaring String header
	private String Handshake_Header = "P2PFILESHARINGPROJ";
	// Declaring zero_bits in String
	private String Handshake_zero_bits = "0000000000";
	// Declaring Peer ID
	private int SystemIdForNeighbouringPeer;
	
	public Handshake(int SystemIdForNeighbouringPeer) {
		this.SystemIdForNeighbouringPeer = SystemIdForNeighbouringPeer;
		// Concatinating header, zero_bits and peer ID
		String array = Handshake_Header + Handshake_zero_bits + Integer.toString(this.SystemIdForNeighbouringPeer);
		// Storing cancatinated String in handshake variable
		variableForHandshake = array.getBytes();
	}
	
}

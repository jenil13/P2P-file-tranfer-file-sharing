package peer;

import java.net.Socket;


public class Peer {
	
	// Declaring My PeerID
	private int SystemIdForPeer;
	// Declaring Peer ID for neighbours
	private int SystemIdForNeighbouringPeer;
	// Declaing soc variable
	private Socket soc;
	// Declaring bitfld variable
	private byte[] bitfld;
	// Declaring determinant variable
	private boolean det;

	public void setSocket(Socket soc) {
		this.soc = soc;
	}

	public byte[] getBitfield() {
		return bitfld;
	}

	public int getPeerID() {
		return SystemIdForNeighbouringPeer;
	}

	public void setInterested(boolean det) {
		this.det = det;
	}

	public int getmyPeerID() {
		return SystemIdForPeer;
	}
	
	public void setPeerID(int SystemIdForNeighbouringPeer) {
		this.SystemIdForNeighbouringPeer = SystemIdForNeighbouringPeer;
	}
	
	public Socket getSocket() {
		return soc;
	}

	public void setBitfield(byte[] bitfld) {
		this.bitfld = bitfld;
	}

	public boolean isInterested() {
		return det;
	}

	public void setmyPeerID(int SystemIdForPeer) {
		this.SystemIdForPeer = SystemIdForPeer;
	}
	
}

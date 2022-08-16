package peer.message;

import java.nio.ByteBuffer;

public class NotInterested {
	
	//Declaring nondeterminant variable
	public byte[] nondeterminant = new byte[5];
	// Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 3;
	
	public NotInterested() {
		MsgLen = ByteBuffer.allocate(4).putInt(0).array();
		int mjj = 0;
		while(mjj < MsgLen.length){
			nondeterminant[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		
		nondeterminant[mjj] = MsgType;
		
	}
	
}

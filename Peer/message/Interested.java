package peer.message;

import java.nio.ByteBuffer;

public class Interested {
	// Declaring determinant variable
	public byte[] determinant = new byte[5];
	//Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 2;
	
	public Interested() {
		MsgLen = ByteBuffer.allocate(4).putInt(0).array();
		int mjj = 0;
		while(mjj < MsgLen.length){
			determinant[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		determinant[mjj] = MsgType;
	}
	
}

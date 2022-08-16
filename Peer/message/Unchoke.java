package peer.message;

import java.nio.ByteBuffer;

public class Unchoke {
	
	// Declaring unchk variable
	public byte[] unchk = new byte[5];
	// Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 1;
	
	public Unchoke() {
		MsgLen = ByteBuffer.allocate(4).putInt(0).array();
		int mjj = 0;
		while(mjj < MsgLen.length){
			unchk[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		unchk[mjj] = MsgType;
	}
	
}

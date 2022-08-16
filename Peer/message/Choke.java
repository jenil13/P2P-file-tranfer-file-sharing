package peer.message;

import java.nio.ByteBuffer;

public class Choke {
	
	//Declaring Choke variable
	public byte[] chk = new byte[5];
	// Declaring Message length variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 0;
	
	public Choke() {
		MsgLen = ByteBuffer.allocate(4).putInt(0).array();
		int mjj = 0;
		while(mjj < MsgLen.length){
			chk[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		chk[mjj] = MsgType;
	}
		
}

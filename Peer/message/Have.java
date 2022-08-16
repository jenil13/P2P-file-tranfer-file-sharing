package peer.message;

import java.nio.ByteBuffer;

public class Have {
	
	//Declaring Have variable
	public byte[] obtain = new byte[9];
	// Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 4;
	// Declaring payld variable
	private byte[] payld = new byte[4];
	
	public Have(int index) {
		MsgLen = ByteBuffer.allocate(4).putInt(4).array();
		payld = ByteBuffer.allocate(4).putInt(index).array();
		int mjj = 0;
		while(mjj < MsgLen.length){
			obtain[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		obtain[mjj] = MsgType;
		
		int jre = 0;
		while(jre < payld.length){
			mjj=mjj+1;
			obtain[mjj] = payld[jre];
			jre=jre+1;
		}
		
	}
	
}

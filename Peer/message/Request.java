package peer.message;

import java.nio.ByteBuffer;

public class Request {
	
	// Declaring rqt variable
	public byte[] rqt = new byte[9];
	// Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 6;
	// Declaring payld variable
	private byte[] payld = new byte[4];
	
	public Request(int index) {
		MsgLen = ByteBuffer.allocate(4).putInt(4).array();
		payld = ByteBuffer.allocate(4).putInt(index).array();
		
		int mjj = 0;
		while(mjj < MsgLen.length){
			rqt[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		rqt[mjj] = MsgType;
		
		int jre = 0;
		while(jre < payld.length){
			mjj=mjj+1;
			rqt[mjj] = payld[jre];
			jre=jre+1;
		}
		
	}
	
}

package peer.message;

import java.nio.ByteBuffer;

public class Piece {
	
	// Declaring piece variable
	public byte[] piece;
	// Declaring MsgLen variable
	private byte[] MsgLen = new byte[4];
	// Declaring Message Type
	private byte MsgType = 7;
	// Declaring Piece index
	private byte[] chunkID = new byte[4];
	// Declaring msg variable
	private byte[] msg;




	public static byte[] ActualMessageTypes(int msgType, byte[] msgPayload)
	{
		int messageLength = msgPayload.length;
		byte[] payload = new byte[5 + messageLength];
		payload[0] = (byte) (messageLength >> 24);
		payload[1] = (byte) (messageLength >> 16);
		payload[2] = (byte) (messageLength >> 8);
		payload[3] = (byte) (messageLength);
		payload[4] = (byte) msgType;
		for (int i = 0; i < messageLength; ++i)
		{
			payload[i + 5] = msgPayload[i];
		}
		return payload;
	}

//	public static ServerThread2z3 (int searchID)
//	{
//		ArrayList<int> L;
//		for (int i = 0; i < L.size(); ++i)
//		{
//			if (L.get(i).InstanceID == searchID)
//			{
//				//return L.get(i);
//			}
//		}
//
//	}


	public Piece(int index, byte[] data) {
		
		// Storing data in msg
		msg = data;
		// Storing msg length in data_len
		int data_len = msg.length;
		chunkID = ByteBuffer.allocate(4).putInt(index).array();
		MsgLen = ByteBuffer.allocate(4).putInt(4 + data_len).array();
		piece = new byte[9 + data_len];
		
		int mjj = 0;
		while(mjj < MsgLen.length){
			piece[mjj] = MsgLen[mjj];
			mjj=mjj+1;
		}
		piece[mjj] = MsgType;
		
		int jre = 0;
		while(jre < chunkID.length){
			mjj=mjj+1;
			piece[mjj] = chunkID[jre];
			jre=jre+1;
		}
		
		jre = 0;
		while(jre < msg.length){
			mjj=mjj+1;
			piece[mjj] = msg[jre];
			jre=jre+1;
		}
		
	}
	
}

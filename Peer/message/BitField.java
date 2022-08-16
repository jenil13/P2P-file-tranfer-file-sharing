package peer.message;

import java.nio.ByteBuffer;


public class BitField {

	// Total Pieces
	private static int TotalChunkCount;
	// Has full File
	private static boolean hasFullFile = false;
	// Payload
	private static byte[] payld;
	// Message Length
	private static byte[] lenOfMsg = new byte[4];
	// Type of Message
	private static byte MsgType = 5;
	// bitfield
	public static byte[] bitfield;

	public static void updateBitField(int chunkID) {
		int mjj = (chunkID - 1) / 8;
		int ste = 7 - ((chunkID - 1) % 8);
		bitfield[mjj + 5] = (byte) (bitfield[mjj + 5] | (1<<ste));
	}



//	public void runjsj() {
//		try {
//			InputStream sin22 = null;
//			OutputStream sout33 = null;
////			InputStream sin22= connector22.getInputStream();
////			OutputStream sout33 = connector.getOutputStream();
//			byte buffer22[] = new byte[1024];
//
//			byte buffer12[] = new byte[1024];
//			sin22.read(buffer12);
//			System.out.println(new String(buffer12).trim());
//			System.out.println(new String(buffer12).trim());
//			sout33.write("Hi (server)".getBytes());
//			//connector22.close();
//		} catch (Exception e) {
//		}
//	}

	public static byte[] MakeHandshakers(int peerId)
	{

		String HandshakeHeader = "P2PFILESHARINGPROJ";

		byte[] mess = new byte[32];

		for(int i = 0; i < HandshakeHeader.length(); ++i)
		{
			mess[i] = (byte)(HandshakeHeader.charAt(i));
		}
		for(int i = 0; i < 10; ++i)
		{
			mess[i + 18] = (byte)0;
		}

		mess[28] = (byte) (peerId >> 24);
		mess[29] = (byte) (peerId >> 16);
		mess[30] = (byte) (peerId >> 8);
		mess[31] = (byte) (peerId);

		return mess;

	}
	public static void setBitfield(boolean hasDoc, int no) {
		hasFullFile = hasDoc;
		TotalChunkCount = no;
		int payload_length = (int) Math.ceil((double) TotalChunkCount /8);
		int remaining = TotalChunkCount % 8;
		lenOfMsg = ByteBuffer.allocate(4).putInt(payload_length).array();
		payld = new byte[payload_length];
		bitfield = new byte[payload_length + 5];
		
		int mjj = 0;
		while(mjj < lenOfMsg.length)
		{
			bitfield[mjj] = lenOfMsg[mjj];
			mjj=mjj+1;
		}
		bitfield[mjj] = MsgType;
		if(hasFullFile == false) {
			int jre = 0;
			while(jre < payld.length)
			{
				mjj=mjj+1;
				bitfield[mjj] = 0;
				jre=jre+1;
			}
		}
		
		else {
			int jre=0;
			while(jre < payld.length - 1)
			{
				mjj=mjj+1;
				int ste = 0;
				while(ste<8)
				{
					bitfield[mjj] = (byte) (bitfield[mjj] | (1<<ste));
					ste=ste+1;
				}
				jre=jre+1;
			}
			mjj=mjj+1;
			jre=0;
			while(jre < remaining)
			{
				bitfield[mjj] = (byte) (bitfield[mjj] | (1<< (7 - jre)));
				jre=jre+1;
			}
		}
	}

}

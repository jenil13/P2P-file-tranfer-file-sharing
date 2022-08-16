package peer;

import java.net.Socket;

public class MessageBody {
	
	// Declaring soc variable
	private Socket soc;
	// Declaring msg variable
	private byte[] msg;
	
	public void setSocket(Socket soc) {
		this.soc = soc;
	}
	
	public byte[] getMessage() {
		return msg;
	}

	public Socket getSocket() {
		return soc;
	}
	
	public void setMessage(byte[] msg) {
		this.msg = msg;
	}
	
	
}

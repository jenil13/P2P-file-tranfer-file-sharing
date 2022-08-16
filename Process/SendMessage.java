package Process;

import java.io.ObjectOutputStream;
import java.net.Socket;

import peer.MessageBody;
import peer.PeerProcess;

public class SendMessage extends Thread {

	public void sendMsg(Socket soc, byte[] msg) {

		try {
			// Object for ObjectOutputStream
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			synchronized (soc) {
				oos.writeObject(msg);
			}
		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
	}

	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// Exception
				System.err.println(e);
			}

			// Check if message body is empty
			if(!PeerProcess.messageBody.isEmpty()) {
				// Sync message body
				synchronized (PeerProcess.messageBody) {
					MessageBody mb = PeerProcess.messageBody.poll();
					// Get Socket
					Socket soc = mb.getSocket();
					// get message
					byte[] msg = mb.getMessage();
					// Call send Message function
					sendMsg(soc, msg);
				}
			}
		}
	}
}

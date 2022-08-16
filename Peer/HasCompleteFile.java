package peer;

import java.net.Socket;

public class HasCompleteFile {

	// Declaring soc variable
	private Socket soc;
	// Declaring has Complete Downloaded File
	private boolean hasWholeFile;
	
	public void setSocket(Socket soc) {
		this.soc = soc;
	}
	
	public boolean isHasDownLoadedCompleteFile() {
		return hasWholeFile;
	}

	public Socket getSocket() {
		return soc;
	}
	
	public void setHasDownLoadedCompleteFile(boolean hasWholeFile) {
		this.hasWholeFile = hasWholeFile;
	}
}

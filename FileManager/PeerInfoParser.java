package fileManager;

import java.io.*;
import java.util.ArrayList;


public class PeerInfoParser {

	// Declaring Id for Peer
	private int SystemIdForNeighbouringPeer;
	// Declaring Id for My Peer
	private int SystemIdForPeer;
	// Declaring IP of Peer
	private String IPaddressOfPeer;
	// Declaring Port no
	private int port_no;
	// Complete File flag
	private boolean containsCompletedFile;

	String basedir = System.getProperty("user.dir");
	private String fileLocation = (new File(basedir).getParent() + "/PeerInfo.cfg");


	public PeerInfoParser(int SystemIdForPeer) {
		this.SystemIdForPeer = SystemIdForPeer;
	}

	public ArrayList<String[]> getPeerInfo() {
		
		ArrayList<String[]> arr = new ArrayList<String[]>(); 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(SystemIdForPeer != Integer.parseInt(parts[0])) {
					arr.add(parts);
				}
				else
					break;
			}
			
			br.close();
		
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return arr;
	}


	public void readFile() {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");

				if(SystemIdForPeer == Integer.parseInt(parts[0])) {
					SystemIdForNeighbouringPeer = Integer.parseInt(parts[0]);
					IPaddressOfPeer = parts[1];
					port_no = Integer.parseInt(parts[2]);

					if(parts[3].equals("1"))
						containsCompletedFile = true;
					else
						containsCompletedFile = false;
				}

			}

			br.close();

		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}
	}

	public String getIPaddressOfPeer() {
		return IPaddressOfPeer;
	}

	public int getPort_no() {
		return port_no;
	}

	public ArrayList<Integer> getAllPeerID() {

		ArrayList<Integer> arr = new ArrayList<Integer>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				arr.add(Integer.parseInt(parts[0]));
			}

			br.close();

		} catch (Exception e) {
			// Exception
			System.err.println(e);
		}

		return arr;
	}

	public int getSystemIdForNeighbouringPeer() {
		return SystemIdForNeighbouringPeer;
	}

	public boolean isContainsCompletedFile() {
		return containsCompletedFile;
	}



}

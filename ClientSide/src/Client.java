import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Client {
	private final String serverName;
	private final int sPort;
	private Socket socket;
	private InputStream iStream;
	private OutputStream oStream;
	private BufferedReader reader;
	
	private ArrayList<StatusListener> statusListeners = new ArrayList<>();
	private ArrayList<MessageListener> messagesListeners = new ArrayList<>();
	
	
	//construction to assign instance port and ip
	public Client(String serverName, int sPort) {
		this.serverName = serverName;
		this.sPort = sPort;
	}

	
	public void msgOut (String reciever, String msg) throws IOException {
		String line = "msg" + " " + reciever + " " + msg + "\n";
		oStream.write(line.getBytes());
	}

	public Boolean login(String username, String password) throws IOException{
	
		String line ="login " + username + " " + password + "\n";
		oStream.write(line.getBytes());
		
		String response = reader.readLine();		
		
		if (response.equals("ok login")) {
			startLoop();
			return true;
		}else {
			return false;
		}
	}
	
	public void logoff() throws IOException{
		String line = "logoff\n";
		oStream.write(line.getBytes());
	}

	public void startLoop() {
		Thread t = new Thread() {
			@Override
			public void run() {
				messageInLoop();
			}
		};
		t.start();
	}

	protected void messageInLoop() {
		try {
			String line;
			while( (line = reader.readLine()) != null) {
				String[] parts = StringUtils.split(line);
				if(parts !=null && parts.length > 0) {
					String cmd = parts[0];
					if(cmd.equals("online")) {
						handleOnline(parts);
					}else if (cmd.equals("offline")) {
						handleOffline(parts);
					}else if (cmd.equals("msg")) {
						String[] msg = StringUtils.split(line, null, 3);
						handleMessenger(msg);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			try {
				oStream.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleMessenger(String[] msg) {
		String username = msg[1];
		String msgBody = msg[2];
		
		for(MessageListener listener: messagesListeners) {
			listener.onMessage(username, msgBody);
		}
	}

	public void handleOffline(String[] parts) {
		String username = parts[1];
		for (StatusListener listener : statusListeners) {
			listener.offline(username);
		}
	}
	
	public void handleOnline(String[] parts) {
		String username = parts[1];
		for (StatusListener listener : statusListeners) {
			listener.online(username);
		}
	}
	
	public boolean connect() {
		try {
			this.socket = new Socket(serverName, sPort);
			System.out.println("Port: " + socket.getLocalPort());
			this.oStream = socket.getOutputStream();
			this.iStream = socket.getInputStream();
			this.reader = new BufferedReader(new InputStreamReader(iStream));
			return true;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addUserStatusListener(StatusListener listener) {
		statusListeners.add(listener);
	}

	public void removeUserStatusListener(StatusListener listener) {
		statusListeners.remove(listener);
	}
	
	public void addMessageListener(MessageListener listener) {
		messagesListeners.add(listener);
	}

	public void removeMessageListener(MessageListener listener) {
		messagesListeners.remove(listener);
	}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class UserListPane extends JPanel implements StatusListener{
	private final Client client;
	private JList<String> userListGUI;
	private DefaultListModel<String> userListModel = new DefaultListModel<>();
	
	public UserListPane(Client client, Messenger messenger, MessagePane mess) {
		this.client = client;
		this.client.addUserStatusListener(this);
		
		
		userListGUI = new JList<>(userListModel);
		setLayout(new BorderLayout());
		add(new JScrollPane(userListGUI), BorderLayout.CENTER);
		
		userListGUI.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
					String username = userListGUI.getSelectedValue();
					MessagePane messagePane = new MessagePane(client, username);
					
					messenger.append(messenger, mess, messagePane);
					
			}
		});
	}

	@Override
	public void online(String username) {
		userListModel.addElement(username);
	}

	@Override
	public void offline(String username) {
		userListModel.removeElement(username);
	}
	
}

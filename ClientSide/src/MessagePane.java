import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class MessagePane extends JPanel implements MessageListener{

	private final Client client;
	private final String username;
	
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private JList<String> messageList = new JList<>(listModel);
	private JTextField inputField = new JTextField();

	public MessagePane(Client client, String username) {
		this.client = client;
		this.username = username;
		
		this.client.addMessageListener(this);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(messageList), BorderLayout.CENTER);
		add(inputField, BorderLayout.SOUTH);
		
		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			try {
				String text = inputField.getText();
				if (!text.equals(null) || !text.equals("")){
					client.msgOut(username , text);
					inputField.setText("");
				}
				
			}catch(IOException e1){
				e1.printStackTrace();
			}
			}

		});
	}

	@Override
	public void onMessage(String fromLogin, String msgBody) {
			String line = fromLogin + "-> " + msgBody;
			listModel.addElement(line);		
	}
	
	@Override
	public void onLogoff(String username) {
		this.client.removeMessageListener(this);
	}
	
}

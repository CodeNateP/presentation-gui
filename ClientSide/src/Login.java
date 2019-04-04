import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Login extends JFrame{
	
	private final Client client;
	Messenger messenger = new Messenger();
	JTextField usernameField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton = new JButton("login");
	
	public Login(){		
		this.client = new Client("localhost", 1999);
		client.connect();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
		login.add(usernameField);
		login.add(passwordField);
		login.add(loginButton);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitLogin();
			}

		});
		
		getContentPane().add(login, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Login login = new Login();
		login.setVisible(true);
	}
	private void submitLogin() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		try {
			if (client.login(username, password)) {
				//bring up user list
				messenger = new Messenger();
				messenger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				MessagePane mess = new MessagePane(client, username);
				messenger.getContentPane().add(mess);
				
				UserListPane userList = new UserListPane(client, messenger, mess);
				messenger.getContentPane().add(new JScrollPane(userList));
				
				messenger.setLayout(new GridLayout(1,2));
				messenger.setSize(600,600);
				
				
				messenger.setVisible(true);
				setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(this, "wrong username or password");
				usernameField.setText("");
				passwordField.setText("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

import javax.swing.JFrame;

public class Messenger extends JFrame{
	public Messenger () {
		
	}
	
	public void append (Messenger messenger, MessagePane mess, MessagePane messagePane) {
		
		messenger.getContentPane().remove(mess);
		messenger.getContentPane().add(messagePane);
		
	}
}

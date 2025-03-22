import javax.swing.JFrame;
import javax.swing.JPanel;

public class Launcher
{    
	
	private static JPanel mainWindow;
	
    public static void main(String[] args)
    {
    	JFrame jf = new JFrame();
    	jf.setTitle("DXMD Archive Editor");
    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);          
        
        jf.add(new MainGUI());
        jf.setResizable(true);        
        jf.setSize(850, 700);
        jf.setVisible(true);    
    }
    
    public static void showDoneMessage()
    {
    	((MainGUI) mainWindow).showDoneMessage();
    }    

}

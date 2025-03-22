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
			
			MainGUI mg = new MainGUI();
			try{
				mg.strGameFile = args[0];
				System.out.println("PARAM:"+mg.strGameFile);
			}catch(Exception e){}
			
			jf.add(mg);
			jf.setResizable(false);
			jf.setSize(850, 700);
			jf.setVisible(true);
			jf.setLocationRelativeTo(null); //center it
		}
		
		public static void showDoneMessage()
		{
			((MainGUI) mainWindow).showDoneMessage();
		}    

}

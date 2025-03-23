import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;

import java.io.RandomAccessFile;

import options.BooleanOption;
import options.InventoryXOption;
import options.NumericOption;
import options.Option;
import options.ShortOption;
import options.SectionLabelOption;

public class MainGUI extends JPanel 
{	
	private ArrayList<Option> optionData;
	private ArrayList<JComponent> entryValues;
	private ArrayList<JLabel> entryLabels;
	private ArrayList<JTextField> invalidValues;
	
	private int valueEntry_x, valueEntry_y;
	
	private float getEnvFloat(String strEnvVar,float fMin,float fMax) {
		float f;
		try {
			f = Float.parseFloat(System.getenv(strEnvVar));
			if(f<fMin)f=fMin;
			if(f>fMax)f=fMax;
			System.out.println("ENVVAR["+strEnvVar+"]:"+f);
			return f;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0.0f;
		}
	}
	private boolean getEnvBool(String strEnvVar) {
		String strVal="false";
		try { strVal=System.getenv(strEnvVar); }catch(Exception e){}
		if(strVal == null) strVal = "false";
		System.out.println("ENVVAR["+strEnvVar+"]:"+strVal);
		if(strVal.equals("true"))return true;
		return false;
	}
	
	private int fixHeight(int i)
	{
		int iFixed = (int)(i*fontScale);
		if(iFixed<25)iFixed=25;
		return iFixed;
	}
	private int scaleFontSize(int i)
	{
		int iFixed = (int)(i*fontScale);
		if(iFixed<10)iFixed=10;
		return iFixed;
	}
	
	private float fontScale=getEnvFloat("fScale",0.65f,1000.0f); //help:evnvar TODO:ISSUE: less than 0.65f will mess text field values, may be fixable
	
	private String strGameFile = "";
	private Boolean bDarkTheme = getEnvBool("bDarkTheme"); //help:evnvar true or false
	
	private JPanel scrollPanel;
	private JButton btn_Select, btn_Default, btn_File, btn_Apply;   
	private JLabel lbl_gameAddress;
	private JScrollPane scrollPane;
	private JTextField jtf_FileAddress;     
	private JTextArea descTextArea;
	
	public void setGameFile(String str) {
		strGameFile = str;
		if(strGameFile == null)
			strGameFile = "";
	}
	
	private void prepareForRun() throws IOException
	{
		String gameFileLocation = jtf_FileAddress.getText();
		
		for (int i = 0; i < optionData.size(); i++)
		{
			if (entryValues.get(i) instanceof JTextField)
				((NumericOption) optionData.get(i)).setNewValue(Integer.parseInt(((JTextField) entryValues.get(i)).getText()));
			else if (entryValues.get(i) instanceof JCheckBox)
				((BooleanOption) optionData.get(i)).setNewValue(((JCheckBox) entryValues.get(i)).isSelected());
		}
		
		ExecuteChanges.run(new File(gameFileLocation), optionData);
		FileAnalyzer.analyze(new File(gameFileLocation), optionData);	
		applyPreset("Current Values");
	}
	
	public MainGUI() 
	{
		setLayout(null);  
		optionData = new ArrayList<Option>();
		entryValues = new ArrayList<JComponent>();
		entryLabels = new ArrayList<JLabel>();
		invalidValues = new ArrayList<JTextField>();
		
		/********
		 * Chose game ID using environment variable: strGameID
		 */
		String strGameID="";
		try { strGameID = System.getenv("strGameID"); } catch(Exception e) { } //help:evnvar
		if(strGameID == null)
			strGameID="DXMD";
		System.out.println("ENVVAR[strGameID]:"+strGameID);
		if(strGameID.equals("DXMD")) {
			(new GameDataDXMD(optionData)).fillOptionData();
		} else {
			System.out.println("ERROR: Unsupported game: '"+strGameID+"'");
			System.exit(1);
		}
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(null);
		int ScrollPanelHeight = (optionData.size() % 2 == 1) ? (10 + (20 * (optionData.size() + 1))) : (10 + (20 * optionData.size()));
		scrollPanel.setPreferredSize(new Dimension(810, ScrollPanelHeight)); // parameters of Dimension constructor are: x, y
					 
		
		lbl_gameAddress = new JLabel("Address of Game.layer.1.all.archive File:");
		lbl_gameAddress.setBounds(10, 0, 480, fixHeight(50));
		lbl_gameAddress.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(20)));
		add(lbl_gameAddress);
		
		btn_Select = new JButton("File Selector");
		btn_Select.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(20)));
		btn_Select.setBounds(660, 10, 175, fixHeight(35)); // left edge from left side of window, top edge from top of window, width, height
		btn_Select.addActionListener(actionEvent -> 
		{	
			System.out.println("INIT:"+strGameFile);
			JFileChooser fileChooser = new JFileChooser(); //do not initialize file param in the constructor
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(!strGameFile.isEmpty())
				fileChooser.setSelectedFile(new File(strGameFile));
			int result = fileChooser.showOpenDialog(null);

			if (result == JFileChooser.APPROVE_OPTION)
			{
				jtf_FileAddress.setText(fileChooser.getSelectedFile().getAbsolutePath());
				System.out.println("NEWCHOSEN:"+jtf_FileAddress.getText());
				try 
				{
					FileAnalyzer.analyze(new File(fileChooser.getSelectedFile().getAbsolutePath()), optionData);
					enableOptions();
					applyPreset("Current Values");
					strGameFile = jtf_FileAddress.getText();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
		add(btn_Select);
		
		jtf_FileAddress = new JTextField();
		jtf_FileAddress.setEditable(false);
		jtf_FileAddress.setBackground(bDarkTheme ? Color.BLACK : Color.WHITE);
		jtf_FileAddress.setBounds(10, 50, 825, fixHeight(30));
		jtf_FileAddress.setFont(new Font("Courier New", Font.PLAIN, scaleFontSize(14)));
		add(jtf_FileAddress);
		
		
		valueEntry_x = 10;
		valueEntry_y = 10;
		
		for (int i = 0; i < optionData.size(); i++)
		{
			Option currentOption = optionData.get(i);
			
			if (currentOption instanceof NumericOption) // Add a JTextField for a numeric input
			{
				JTextField tempTF = new JTextField();
				tempTF.setBounds(valueEntry_x, valueEntry_y, 70, fixHeight(40)); // width from 65 to 70 to fit 65535 better at least in 0.65 scale
				tempTF.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(20)));
				tempTF.setVisible(true);
				tempTF.setEnabled(false);
				tempTF.getDocument().addDocumentListener(new DocumentListener() 
					{
						public void changedUpdate(DocumentEvent e){}
						public void removeUpdate(DocumentEvent e) 
						{
							checkInputValidity(tempTF, ((NumericOption) currentOption).getMaxValue());
						}
						public void insertUpdate(DocumentEvent e) 
						{
							checkInputValidity(tempTF, ((NumericOption) currentOption).getMaxValue());
						}
					});
				tempTF.addMouseListener(new java.awt.event.MouseAdapter()
					{
						public void mouseEntered(java.awt.event.MouseEvent evt) 
						{
							descTextArea.setText(currentOption.getOptionDesc());
						}
						public void mouseExited(java.awt.event.MouseEvent evt)
						{
							descTextArea.setText("");
						}
					});
				scrollPanel.add(tempTF);
				entryValues.add(tempTF);
			}
			else if (currentOption instanceof BooleanOption) // Add a checkbox for a boolean input
			{
				JCheckBox tempCB = new JCheckBox();
				tempCB.setBounds(valueEntry_x + 25, valueEntry_y, 40, fixHeight(40));
				tempCB.setVisible(true);
				tempCB.setEnabled(false);
				tempCB.addMouseListener(new java.awt.event.MouseAdapter()
					{
						public void mouseEntered(java.awt.event.MouseEvent evt) 
						{
							descTextArea.setText(currentOption.getOptionDesc());
						}
						public void mouseExited(java.awt.event.MouseEvent evt)
						{
							descTextArea.setText("");
						}
					});
				scrollPanel.add(tempCB);
				entryValues.add(tempCB);
			}
			else if (currentOption instanceof SectionLabelOption) // dummy, just a section separator
			{
				entryValues.add(new JLabel("DUMMY")); //this is required to grant the index of entryValues will match optionData index
			}
				
			JLabel tempLbl = new JLabel(currentOption.getOptionName());
			tempLbl.setBounds(valueEntry_x + 75, valueEntry_y, 300, fixHeight(40));
//            tempLbl.setSize(100, 100);
			tempLbl.setFont(new Font("Courier New", Font.PLAIN, scaleFontSize(20)));
			tempLbl.setVisible(true);
			tempLbl.setForeground(Color.GRAY);
			if(!currentOption.isData())
				tempLbl.setForeground(bDarkTheme ? Color.CYAN : Color.BLUE);
			tempLbl.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseEntered(java.awt.event.MouseEvent evt) 
				{
					descTextArea.setText(currentOption.getOptionDesc());
				}
				public void mouseExited(java.awt.event.MouseEvent evt)
				{
					descTextArea.setText("");
				}
			}); 
			scrollPanel.add(tempLbl);
			entryLabels.add(tempLbl);
			
			incrementCheckBoxLocation();
		}
		
		
		scrollPane = new JScrollPane(scrollPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10); // Scroll speed. Default of 1
		scrollPane.setBounds(10, 100, 830, 370);
		add(scrollPane);
		
		
		// Description text area section
		descTextArea = new JTextArea();
		descTextArea.setBounds(10, 500, 825, fixHeight(85));
		descTextArea.setFont(new Font("Courier New", Font.PLAIN, scaleFontSize(18)));
		descTextArea.setLineWrap(true);
		descTextArea.setWrapStyleWord(true); // Needed for the line wrap to actually work
		descTextArea.setFocusable(false);
		add(descTextArea);                       
		
		
		btn_Default = new JButton("Default Values");
		btn_Default.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(18)));
		btn_Default.setBounds(10, 600, 220, fixHeight(50)); // left edge from left side of window, top edge from top of window, width, height
		btn_Default.setEnabled(false);
		btn_Default.addActionListener(actionEvent ->
		{        	
			applyPreset("Default Values");     
		});        
		add(btn_Default);
		
		
		btn_File = new JButton("Current File Values");
		btn_File.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(18)));
		btn_File.setBounds(240, 600, 220, fixHeight(50)); // left edge from left side of window, top edge from top of window, width, height
		btn_File.setEnabled(false);
		btn_File.addActionListener(actionEvent ->
		{        	
			applyPreset("Current Values");
		});        
		add(btn_File);
		
		
		btn_Apply = new JButton("Apply");
		btn_Apply.setFont(new Font("Courier New", Font.BOLD, scaleFontSize(24)));
		btn_Apply.setBounds(670, 600, 150, fixHeight(50)); // left edge from left side of window, top edge from top of window, width, height
		btn_Apply.addActionListener(actionEvent ->
		{        	
			try 
			{
				prepareForRun();
			}
			catch (IOException e) 
			{			
				e.printStackTrace();
			}        
		});        
		handleApplyButtonEnabled();
		add(btn_Apply);
	}

	private void checkInputValidity(JTextField tempTF, int optionMax)
	{    	
		if (!isEntryValid(tempTF.getText(), optionMax))
		{
			if (!invalidValues.contains(tempTF))
			{
				tempTF.setForeground(Color.RED);
				invalidValues.add(tempTF);
			}
		}
		else
		{
			if (invalidValues.contains(tempTF))
			{
				invalidValues.remove(tempTF);
				tempTF.setForeground(bDarkTheme ? Color.WHITE : Color.BLACK);
			}            		
		} 
		handleApplyButtonEnabled();
	}
	
	public void showDoneMessage()
	{
		descTextArea.setText("Done");
	}
	
	private void incrementCheckBoxLocation()
	{    	
			if (valueEntry_x == 10)
				valueEntry_x = 390;
			else
			{
				valueEntry_x = 10;
				valueEntry_y += fixHeight(40);        
			}
	}
	
	private void enableOptions()
	{
		for (JComponent tempField : entryValues)
		{
			tempField.setEnabled(true);
		}
		
		for (JLabel tempLabel : entryLabels)
		{
			if(!tempLabel.getText().startsWith(GameData.g_strSectionInit))
				tempLabel.setForeground(bDarkTheme ? Color.LIGHT_GRAY : Color.BLACK);
		}
		
		btn_Default.setEnabled(true);
		btn_File.setEnabled(true);
	}
	
	private void applyPreset(String preset)
	{ 
		int value;
		if (preset.equals("Current Values"))
			value = 0;
		else
			value = 1;
		
			for (int i = 0; i < entryValues.size(); i++)
			{
				if (optionData.get(i) instanceof NumericOption)
					((JTextField) entryValues.get(i)).setText(Integer.toString(((NumericOption) optionData.get(i)).getSpecificValue(value)));
				else if (optionData.get(i) instanceof BooleanOption)
					((JCheckBox) entryValues.get(i)).setSelected(((BooleanOption) optionData.get(i)).getSpecificValue(value));
			}        
	}
	
	// Used to know whether a specific entry is valid for the specific option it is trying to change
	private boolean isEntryValid(String entry, int optionMax)
	{
		try
		{
			int intVal = Integer.parseInt(entry);
			if (intVal >= 0 && intVal <= optionMax)
				return true;
			else
				return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	private void handleApplyButtonEnabled()
	{
		if (invalidValues.size() > 0 || jtf_FileAddress.getText().isEmpty())
			btn_Apply.setEnabled(false);
		else
			btn_Apply.setEnabled(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {}
	
}

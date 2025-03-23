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
	
	private float getEnvFloat(String strEnvVar) {
		float f;
		try {
			f = Float.parseFloat(System.getenv(strEnvVar));
			System.out.println("ENVVAR["+strEnvVar+"]:"+f);
			return f;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0.0f;
		}
	}
	private boolean getEnvBool(String strEnvVar) {
		String strVal=System.getenv(strEnvVar);
		System.out.println("ENVVAR["+strEnvVar+"]:"+strVal);
		if(strVal == "true")return true;
		return false;
	}
	private float fontScaleEnv=getEnvFloat("fScale"); //ex.: fScale=0.75f java -jar ...
	private float fontScale = fontScaleEnv > 0.0f ? fontScaleEnv : 1.0f;
	private int fontA=(int)(20*fontScale);
	private int fontB=(int)(14*fontScale);
	private int fontC=(int)(18*fontScale);
	private int fontD=(int)(24*fontScale);
	private String strGameFile;
	private Boolean bDarkTheme = getEnvBool("bDarkTheme");
	
	private JPanel scrollPanel;
	private JButton btn_Select, btn_Default, btn_File, btn_Apply;   
	private JLabel lbl_gameAddress;
	private JScrollPane scrollPane;
	private JTextField jtf_FileAddress;     
	private JTextArea descTextArea;
	
	public void setGameFile(String str) { strGameFile = str; }
	
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
		fillOptionDataDXMD();
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(null);
		int ScrollPanelHeight = (optionData.size() % 2 == 1) ? (10 + (20 * (optionData.size() + 1))) : (10 + (20 * optionData.size()));
		scrollPanel.setPreferredSize(new Dimension(810, ScrollPanelHeight)); // parameters of Dimension constructor are: x, y
					 
		
		lbl_gameAddress = new JLabel("Address of Game.layer.1.all.archive File:");
		lbl_gameAddress.setBounds(10, 0, 480, 50);
		lbl_gameAddress.setFont(new Font("Courier New", Font.BOLD, fontA));
		add(lbl_gameAddress);
		
		btn_Select = new JButton("File Selector");
		btn_Select.setFont(new Font("Courier New", Font.BOLD, fontA));
		btn_Select.setBounds(660, 10, 175, 35); // left edge from left side of window, top edge from top of window, width, height
		btn_Select.addActionListener(actionEvent -> 
		{	
			System.out.println("INIT:"+strGameFile);
			JFileChooser fileChooser = new JFileChooser(); //do not initialize file param in the constructor
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
		jtf_FileAddress.setBackground(bDarkTheme?Color.BLACK:Color.WHITE);
		jtf_FileAddress.setBounds(10, 50, 825, 30);
		jtf_FileAddress.setFont(new Font("Courier New", Font.PLAIN, fontB));
		add(jtf_FileAddress);
		
		
		valueEntry_x = 10;
		valueEntry_y = 10;
		
		for (int i = 0; i < optionData.size(); i++)
		{
			Option currentOption = optionData.get(i);
			
			if (currentOption instanceof NumericOption) // Add a JTextField for a numeric input
			{
				JTextField tempTF = new JTextField();
				tempTF.setBounds(valueEntry_x, valueEntry_y, 65, (int)(40*fontScale));
				tempTF.setFont(new Font("Courier New", Font.BOLD, fontA));
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
				tempCB.setBounds(valueEntry_x + 25, valueEntry_y, 40, (int)(40*fontScale));
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
				//if(i%2==1) {
					//scrollPanel.add(new JLabel(""));
					//entryLabels.add(new JLabel(""));
				//}
			}
				
			JLabel tempLbl = new JLabel(currentOption.getOptionName());
			tempLbl.setBounds(valueEntry_x + 75, valueEntry_y, 300, (int)(40*fontScale));
//            tempLbl.setSize(100, 100);
			tempLbl.setFont(new Font("Courier New", Font.PLAIN, fontA));
			tempLbl.setVisible(true);
			tempLbl.setForeground(Color.GRAY);
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
			
			//if (currentOption instanceof SectionLabelOption) // dummy, just a section separator
			//{
				//if(i%2==0) {
					//JLabel tempLblSect = new JLabel(".");
					//tempLblSect.setBounds(valueEntry_x + 75, valueEntry_y, 300, (int)(40*fontScale));
					//tempLblSect.setFont(new Font("Courier New", Font.PLAIN, fontA));
					//tempLblSect.setVisible(true);
					//tempLblSect.setForeground(Color.GRAY);
					//scrollPanel.add(tempLblSect);
					//entryLabels.add(tempLblSect);
				//}
			//}
			
			incrementCheckBoxLocation();
		}
		
		
		scrollPane = new JScrollPane(scrollPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10); // Scroll speed. Default of 1
		scrollPane.setBounds(10, 100, 830, 370);
		add(scrollPane);
		
		
		// Description text area section
		descTextArea = new JTextArea();
		descTextArea.setBounds(10, 500, 825, 85);
		descTextArea.setFont(new Font("Courier New", Font.PLAIN, fontC));
		descTextArea.setLineWrap(true);
		descTextArea.setWrapStyleWord(true); // Needed for the line wrap to actually work
		descTextArea.setFocusable(false);
		add(descTextArea);                       
		
		
		btn_Default = new JButton("Default Values");
		btn_Default.setFont(new Font("Courier New", Font.BOLD, fontC));
		btn_Default.setBounds(10, 600, 220, (int)(50*fontScale)); // left edge from left side of window, top edge from top of window, width, height
		btn_Default.setEnabled(false);
		btn_Default.addActionListener(actionEvent ->
		{        	
			applyPreset("Default Values");     
		});        
		add(btn_Default);
		
		
		btn_File = new JButton("Current File Values");
		btn_File.setFont(new Font("Courier New", Font.BOLD, fontC));
		btn_File.setBounds(240, 600, 220, (int)(50*fontScale)); // left edge from left side of window, top edge from top of window, width, height
		btn_File.setEnabled(false);
		btn_File.addActionListener(actionEvent ->
		{        	
			applyPreset("Current Values");
		});        
		add(btn_File);
		
		
		btn_Apply = new JButton("Apply");
		btn_Apply.setFont(new Font("Courier New", Font.BOLD, fontD));
		btn_Apply.setBounds(670, 600, 150, (int)(50*fontScale)); // left edge from left side of window, top edge from top of window, width, height
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
				tempTF.setForeground(bDarkTheme?Color.WHITE:Color.BLACK);
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
				valueEntry_y += (int)(40*fontScale);        
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
			tempLabel.setForeground(bDarkTheme?Color.WHITE:Color.BLACK);
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
	
	private void addSection(String str)
	{
		if(optionData.size()%2==1)optionData.add(new SectionLabelOption(""));
		optionData.add(new SectionLabelOption(str));
		optionData.add(new SectionLabelOption(""));
	}
	
	private void fillOptionDataDXMD()
	{
		addSection("_______ Experience Gain _______");
		
		// Note: final parameter given should be the default value
		optionData.add(new ShortOption(setUpLongAL(5401405), "XP for lvl. 1 Hack", "Sets the amount of XP received from hacking a level 1 device. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(5401429), "XP for lvl. 2 Hack", "Sets the amount of XP received from hacking a level 2 device. Range:0-65535", 50));
		optionData.add(new ShortOption(setUpLongAL(5401453), "XP for lvl. 3 Hack", "Sets the amount of XP received from hacking a level 3 device. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(5401477), "XP for lvl. 4 Hack", "Sets the amount of XP received from hacking a level 4 device. Range:0-65535", 100));
		optionData.add(new ShortOption(setUpLongAL(5401501), "XP for lvl. 5 Hack", "Sets the amount of XP received from hacking a level 5 device. Range:0-65535", 125));
		optionData.add(new ShortOption(setUpLongAL(5401525), "XP for First Try Hack", "Sets the amount of XP received from hacking a device on your first try. Range:0-65535", 5));
		
		optionData.add(new ShortOption(setUpLongAL(5399013, 5403213, 5404333, 5405805), "XP for Headshot", "Sets the amount of XP received from a headshot kill. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(5398989, 5403189, 5404309, 5405781), "XP for Non-Lethal", "Sets the amount of XP received from a non-lethal takedown (merciful soul). Range:0-65535", 20));
		
		
		addSection("_______ Item's price _______");
		
		optionData.add(new ShortOption(setUpLongAL(7736149), "Praxis Shop Cost", "Sets the amount credits a Praxis kit will cost in a store. Range:0-65535", 10000));
		optionData.add(new ShortOption(setUpLongAL(4570325), "Biocell Shop Cost", "Sets the amount credits a biocell will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(5796261), "Hypostim Shop Cost", "Sets the amount credits a hypostim will cost in a store. Range:0-65535", 150));
		optionData.add(new ShortOption(setUpLongAL(4913021), "Painkiller Shop Cost", "Sets the amount credits a painkiller bottle will cost in a store. Range:0-65535", 50));
		optionData.add(new ShortOption(setUpLongAL(6119021), "Multitool Shop Cost", "Sets the amount credits a Multitool will cost in a store. Range:0-65535", 800));
		optionData.add(new ShortOption(setUpLongAL(7558933), "Typhoon Ammo Shop Cost", "Sets the amount credits a single Typhoon ammo will cost in a store. This is multiplied by 3 or 5 for the packs. Range:0-65535", 500));
		optionData.add(new ShortOption(setUpLongAL(5852637), "Tesla Ammo Shop Cost", "Sets the amount credits a single Tesla ammo will cost in a store. This is multiplied by 8 for the pack. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(5853429), "Nanoblade Ammo Shop Cost", "Sets the amount credits a single Nanoblade ammo will cost in a store. This is multiplied by 8 for the pack. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(5865149), "Weapon Part Shop Cost", "Sets the amount credits a single weapon part will cost in a store. This is multiplied for the pack. Range:0-65535", 5));
		
		optionData.add(new ShortOption(setUpLongAL(6789165), "Reveal Shop Cost", "Sets the amount credits a Reveal software will cost in a store. Range:0-65535", 250));
		optionData.add(new ShortOption(setUpLongAL(6789837), "Stealth Shop Cost", "Sets the amount credits a Stealth software will cost in a store. Range:0-65535", 250));
		optionData.add(new ShortOption(setUpLongAL(6790509), "Nuke Shop Cost", "Sets the amount credits a Nuke software will cost in a store. Range:0-65535", 150));
		optionData.add(new ShortOption(setUpLongAL(6791181), "Datascan Shop Cost", "Sets the amount credits a Datascan software will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(6791853), "Stop! Shop Cost", "Sets the amount credits a Stop! software will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(6792525), "Overclock Shop Cost", "Sets the amount credits an Overclock software will cost in a store. Range:0-65535", 200));
		
		optionData.add(new ShortOption(setUpLongAL(7562693), "Typhoon Ammo Crafting Cost", "Sets the amount weapons parts needed to craft a 3-pack of Typhoon ammo. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(7557853), "Mine Template Crafting Cost", "Sets the amount weapons parts needed to craft a mine template. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(5865517), "Biocell Crafting Cost", "Sets the amount weapons parts needed to craft a biocell. Range:0-65535", 120));
		optionData.add(new ShortOption(setUpLongAL(6119341), "Multi-Tool Crafting Cost", "Sets the amount weapons parts needed to craft a Multi-Tool. Range:0-65535", 120));
		optionData.add(new ShortOption(setUpLongAL(6172733), "Nanoblade Crafting Cost", "Sets the amount weapons parts needed to craft a Nanoblade ammo pack. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(7441693), "Tesla Ammo Crafting Cost", "Sets the amount weapons parts needed to craft a Tesla ammo pack. Range:0-65535", 75));
		
		
		addSection("_______ Inventory: Items' Width _______");
		
		optionData.add(new InventoryXOption(setUpLongAL(6784805), "Sniper Width", "Sets the width of the sniper rifle in the inventory (number of tiles). Range:0-16", 7));
		optionData.add(new InventoryXOption(setUpLongAL(4936557), "Tranquilizer Rifle Width", "Sets the width of the tranquilizer rifle in the inventory (number of tiles). Range:0-16", 7));
		optionData.add(new InventoryXOption(setUpLongAL(4987389, 6754613), "Shotgun Width", "Sets the width of the shotgun in the inventory (number of tiles). Range:0-16", 5));
		optionData.add(new InventoryXOption(setUpLongAL(4967181), "Grenade Launcher Width", "Sets the width of the grenade launcher in the inventory (number of tiles). Range:0-16", 4));
		optionData.add(new InventoryXOption(setUpLongAL(4940389), "Machine Pistol Width", "Sets the width of the machine pistol in the inventory (number of tiles). Range:0-16", 4));
		optionData.add(new InventoryXOption(setUpLongAL(4951797), "Battle Rifle Width", "Sets the width of the battle rifle in the inventory (number of tiles). Range:0-16", 6));
		optionData.add(new InventoryXOption(setUpLongAL(4886637), "Combat Rifle Width", "Sets the width of the combat rifle in the inventory (number of tiles). Range:0-16", 5));
		
		
		addSection("_______ Inventory: Items Stack Size _______");
		
		optionData.add(new ShortOption(setUpLongAL(4264429, 4265549, 4267085, 4268245, 4269245, 4270501, 4285101, 4286237, 4287013, 4288053, 4288885, 4290013, 6615853, 6616941, 6966957, 7525853), "Ammo Stack", "Sets the max inventory stack size of weapon ammo (grenade launcher excluded). Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(4282117, 4282861, 4283605, 4284349), "Grenade Ammo Stack", "Sets the max inventory stack size of grenade launcher ammo. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(4907021, 4908021, 5202949, 5204013, 5709093, 5710741, 5711741, 5723317, 5862917, 6783125, 6784101), "Grenade Stack", "Sets the max inventory stack size of thrown grenades and mines. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(4570277), "Biocell Stack", "Sets the max inventory stack size of Biocells. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(4912973), "Painkiller Stack", "Sets the max inventory stack size of painkillers. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(5796213), "Hypostim Stack", "Sets the max inventory stack size of hypostims. Range:0-65535", 25));    	
		
		
		addSection("_______ Special Toggles List _______");
		
		optionData.add(new BooleanOption(setUpLongAL(7413191, 7413192), setUpShortAL(4, 66), setUpShortAL(0, 0), "No Takedown Cost", "Makes takedowns have no energy consumption.", false));
		optionData.add(new BooleanOption(setUpLongAL(6611045, 7589029, 7589797, 7704685, 7718621, 7719573, 7721117, 7722581, 7723565, 7727101),  setUpShortAL(10, 10, 10, 10, 10, 10, 10, 10, 10, 10), setUpShortAL(0, 0, 0, 0, 0, 0, 0, 0, 0, 0), 
			"Augs Non-Experimental", "Makes all augmentations non-experimental, removing need for neuroplasticity calibrator.", false));
		
		//TODO: these tests failed, still did not guess the offset. It could be height tho, also it could be impossible or have to change DXMD.exe may be or other file
		//optionData.add(new InventoryXOption(setUpLongAL(5694417), "Ammo T.Dart Width", "Width of Tranquilizer Dart ammo box. Range:0-16", 2)); //binary pos hint:"Tranquilizer_Dart"+8
		//optionData.add(new InventoryXOption(setUpLongAL(7646673), "Ammo 10mm Regular Width", "Width of 10mm (pistol) Regular ammo box. Range:0-16", 2)); //binary pos hint:"10mm (pistol) Regular"+4
		//optionData.add(new InventoryXOption(setUpLongAL(7647561), "Ammo 10mm Emp Width", "Width of 10mm (pistol) Emp ammo box. Range:0-16", 2)); //binary pos hint:"10mm (pistol) Emp"+4
		
		///////                                                                                       ////
		//////                                                                                       /////
		// based on hints from: https://www.grognougnou.com/mods-tutorials-deus-ex-mankind-divided.html //
		////                                                                                       ///////
		optionData.add(new BooleanOption(setUpLongAL(6578429,6578453,6579061,6579085,6579109), setUpShortAL(1,1,1,1,1), setUpShortAL(0,0,0,0,0), "Aug Energy Recharge Rate OFF", "Disables augmentation energy RechargeRate lvl1 even if it shows being active", false)); //hint: 05 ... 01 near aug name for the 3 levels
		optionData.add(new BooleanOption(setUpLongAL(6580989,4787093,4787117), setUpShortAL(1,1,1), setUpShortAL(0,0,0), "Aug C. Defibrillator OFF", "Disables free initial augmentation CardiovertorDefibrillator, that is the auto healing", false)); //hint: 05 ... 01 near aug name for the 3 levels
		
		optionData.add(new BooleanOption(setUpLongAL(4570015,4570016), setUpShortAL(0xAA,0x42), setUpShortAL(0xAA,0x43), "BioCell Energy 100%", "BioCell recovers 100% of the energy bar if available", false)); //hint: seek after 72C142AA. The full value is 2 bytes, 16bits (at least)
	}
	
	private ArrayList<Long> setUpLongAL(long ... input)
	{
		ArrayList<Long> tempList = new ArrayList<Long>();
		for (Long l : input)
			tempList.add(l);
		return tempList;
	}
	
	private ArrayList<Short> setUpShortAL(int ... input)
	{
		ArrayList<Short> tempList = new ArrayList<Short>();
		for (int l : input)
			tempList.add((short) l);
		return tempList;
	}
	
	@Override
	public void paintComponent(Graphics g) {}
	
}

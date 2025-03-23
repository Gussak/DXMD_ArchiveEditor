import options.BooleanOption;
import options.InventoryXOption;
import options.NumericOption;
import options.Option;
import options.ShortOption;
import options.SectionLabelOption;

import java.util.ArrayList;

public class GameDataDXMD extends GameData
{	
	GameDataDXMD(ArrayList<Option> _optionData) { optionData=_optionData; }
	
	public void fillOptionData()
	{
		/************************
		 * To add a new game data, just copy above here to a new file
		 * and at MainGUI.java update the "Chose game ID" code list
		 */
		
		addSection("Experience Gain: Hacking");
		
		// Note: final parameter given should be the default value
		optionData.add(new ShortOption(setUpLongAL(5401405), "XP for lvl. 1 Hack", "Sets the amount of XP received from hacking a level 1 device. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(5401429), "XP for lvl. 2 Hack", "Sets the amount of XP received from hacking a level 2 device. Range:0-65535", 50));
		optionData.add(new ShortOption(setUpLongAL(5401453), "XP for lvl. 3 Hack", "Sets the amount of XP received from hacking a level 3 device. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(5401477), "XP for lvl. 4 Hack", "Sets the amount of XP received from hacking a level 4 device. Range:0-65535", 100));
		optionData.add(new ShortOption(setUpLongAL(5401501), "XP for lvl. 5 Hack", "Sets the amount of XP received from hacking a level 5 device. Range:0-65535", 125));
		optionData.add(new ShortOption(setUpLongAL(5401525), "XP for First Try Hack", "Sets the amount of XP received from hacking a device on your first try. Range:0-65535", 5));
		
		
		addSection("Experience Gain: Combat");
		
		optionData.add(new ShortOption(setUpLongAL(5399013, 5403213, 5404333, 5405805), "XP for Headshot", "Sets the amount of XP received from a headshot kill. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(5398989, 5403189, 5404309, 5405781), "XP for Non-Lethal", "Sets the amount of XP received from a non-lethal takedown (merciful soul). Range:0-65535", 20));
		
		
		addSection("Item's price: Aug Ammo");
		
		optionData.add(new ShortOption(setUpLongAL(7558933), "Typhoon Ammo Shop Cost", "Sets the amount credits a single Typhoon ammo will cost in a store. This is multiplied by 3 or 5 for the packs. Range:0-65535", 500));
		optionData.add(new ShortOption(setUpLongAL(5852637), "Tesla Ammo Shop Cost", "Sets the amount credits a single Tesla ammo will cost in a store. This is multiplied by 8 for the pack. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(5853429), "Nanoblade Ammo Shop Cost", "Sets the amount credits a single Nanoblade ammo will cost in a store. This is multiplied by 8 for the pack. Range:0-65535", 200));
		
		
		addSection("Item's price: Software");
		
		optionData.add(new ShortOption(setUpLongAL(6789165), "Reveal Shop Cost", "Sets the amount credits a Reveal software will cost in a store. Range:0-65535", 250));
		optionData.add(new ShortOption(setUpLongAL(6789837), "Stealth Shop Cost", "Sets the amount credits a Stealth software will cost in a store. Range:0-65535", 250));
		optionData.add(new ShortOption(setUpLongAL(6790509), "Nuke Shop Cost", "Sets the amount credits a Nuke software will cost in a store. Range:0-65535", 150));
		optionData.add(new ShortOption(setUpLongAL(6791181), "Datascan Shop Cost", "Sets the amount credits a Datascan software will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(6791853), "Stop! Shop Cost", "Sets the amount credits a Stop! software will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(6792525), "Overclock Shop Cost", "Sets the amount credits an Overclock software will cost in a store. Range:0-65535", 200));
		
		
		addSection("Item's price: Other");
		
		optionData.add(new ShortOption(setUpLongAL(7736149), "Praxis Shop Cost", "Sets the amount credits a Praxis kit will cost in a store. Range:0-65535", 10000));
		optionData.add(new ShortOption(setUpLongAL(4570325), "Biocell Shop Cost", "Sets the amount credits a biocell will cost in a store. Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(5796261), "Hypostim Shop Cost", "Sets the amount credits a hypostim will cost in a store. Range:0-65535", 150));
		optionData.add(new ShortOption(setUpLongAL(4913021), "Painkiller Shop Cost", "Sets the amount credits a painkiller bottle will cost in a store. Range:0-65535", 50));
		optionData.add(new ShortOption(setUpLongAL(6119021), "Multitool Shop Cost", "Sets the amount credits a Multitool will cost in a store. Range:0-65535", 800));
		optionData.add(new ShortOption(setUpLongAL(5865149), "Weapon Part Shop Cost", "Sets the amount credits a single weapon part will cost in a store. This is multiplied for the pack. Range:0-65535", 5));
		
		
		addSection("Inventory: Items' Width");
		
		optionData.add(new InventoryXOption(setUpLongAL(6784805), "Sniper Width", "Sets the width of the sniper rifle in the inventory (number of tiles). Range:0-16", 7));
		optionData.add(new InventoryXOption(setUpLongAL(4936557), "Tranquilizer Rifle Width", "Sets the width of the tranquilizer rifle in the inventory (number of tiles). Range:0-16", 7));
		optionData.add(new InventoryXOption(setUpLongAL(4987389, 6754613), "Shotgun Width", "Sets the width of the shotgun in the inventory (number of tiles). Range:0-16", 5));
		optionData.add(new InventoryXOption(setUpLongAL(4967181), "Grenade Launcher Width", "Sets the width of the grenade launcher in the inventory (number of tiles). Range:0-16", 4));
		optionData.add(new InventoryXOption(setUpLongAL(4940389), "Machine Pistol Width", "Sets the width of the machine pistol in the inventory (number of tiles). Range:0-16", 4));
		optionData.add(new InventoryXOption(setUpLongAL(4951797), "Battle Rifle Width", "Sets the width of the battle rifle in the inventory (number of tiles). Range:0-16", 6));
		optionData.add(new InventoryXOption(setUpLongAL(4886637), "Combat Rifle Width", "Sets the width of the combat rifle in the inventory (number of tiles). Range:0-16", 5));
		
		
		addSection("Inventory: Items' Stack Size");
		
		optionData.add(new ShortOption(setUpLongAL(4264429, 4265549, 4267085, 4268245, 4269245, 4270501, 4285101, 4286237, 4287013, 4288053, 4288885, 4290013, 6615853, 6616941, 6966957, 7525853), "Ammo Stack", "Sets the max inventory stack size of weapon ammo (grenade launcher excluded). Range:0-65535", 200));
		optionData.add(new ShortOption(setUpLongAL(4282117, 4282861, 4283605, 4284349), "Grenade Ammo Stack", "Sets the max inventory stack size of grenade launcher ammo. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(4907021, 4908021, 5202949, 5204013, 5709093, 5710741, 5711741, 5723317, 5862917, 6783125, 6784101), "Grenade Stack", "Sets the max inventory stack size of thrown grenades and mines. Range:0-65535", 10));
		optionData.add(new ShortOption(setUpLongAL(4570277), "Biocell Stack", "Sets the max inventory stack size of Biocells. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(4912973), "Painkiller Stack", "Sets the max inventory stack size of painkillers. Range:0-65535", 25));
		optionData.add(new ShortOption(setUpLongAL(5796213), "Hypostim Stack", "Sets the max inventory stack size of hypostims. Range:0-65535", 25));    	
		
		
		addSection("Items' Craft Cost");
		
		optionData.add(new ShortOption(setUpLongAL(7562693), "Typhoon Ammo Crafting Cost", "Sets the amount weapons parts needed to craft a 3-pack of Typhoon ammo. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(7557853), "Mine Template Crafting Cost", "Sets the amount weapons parts needed to craft a mine template. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(5865517), "Biocell Crafting Cost", "Sets the amount weapons parts needed to craft a biocell. Range:0-65535", 120));
		optionData.add(new ShortOption(setUpLongAL(6119341), "Multi-Tool Crafting Cost", "Sets the amount weapons parts needed to craft a Multi-Tool. Range:0-65535", 120));
		optionData.add(new ShortOption(setUpLongAL(6172733), "Nanoblade Crafting Cost", "Sets the amount weapons parts needed to craft a Nanoblade ammo pack. Range:0-65535", 75));
		optionData.add(new ShortOption(setUpLongAL(7441693), "Tesla Ammo Crafting Cost", "Sets the amount weapons parts needed to craft a Tesla ammo pack. Range:0-65535", 75));
		
		
		addSection("Special Toggles List");
		
		optionData.add(new BooleanOption(setUpLongAL(7413191, 7413192), setUpShortAL(4, 66), setUpShortAL(0, 0), "No Takedown Cost", "Makes takedowns have no energy consumption.", false));
		optionData.add(new BooleanOption(setUpLongAL(6611045, 7589029, 7589797, 7704685, 7718621, 7719573, 7721117, 7722581, 7723565, 7727101),  setUpShortAL(10, 10, 10, 10, 10, 10, 10, 10, 10, 10), setUpShortAL(0, 0, 0, 0, 0, 0, 0, 0, 0, 0), 
			"Augs Non-Experimental", "Makes all augmentations non-experimental, removing need for neuroplasticity calibrator.", false));
		
		//TODO: these tests failed, still did not guess the offset. It could be height tho, also it could be impossible or have to change DXMD.exe may be or other file
		//optionData.add(new InventoryXOption(setUpLongAL(5694417), "Ammo T.Dart Width", "Width of Tranquilizer Dart ammo box. Range:0-16", 2)); //binary pos hint:"Tranquilizer_Dart"+8
		//optionData.add(new InventoryXOption(setUpLongAL(7646673), "Ammo 10mm Regular Width", "Width of 10mm (pistol) Regular ammo box. Range:0-16", 2)); //binary pos hint:"10mm (pistol) Regular"+4
		//optionData.add(new InventoryXOption(setUpLongAL(7647561), "Ammo 10mm Emp Width", "Width of 10mm (pistol) Emp ammo box. Range:0-16", 2)); //binary pos hint:"10mm (pistol) Emp"+4
		
		///////                                                                                           ///////
		//////                                                                                           ///////
		// based also on hints from: https://www.grognougnou.com/mods-tutorials-deus-ex-mankind-divided.html //
		////                                                                                           ///////
		optionData.add(new BooleanOption(setUpLongAL(6578429,6578453,6579061,6579085,6579109), setUpShortAL(1,1,1,1,1), setUpShortAL(0,0,0,0,0), "Aug Energy Recharge Rate OFF", "Disables augmentation energy RechargeRate lvl1 even if it shows being active", false)); //hint: 05 ... 01 near aug name for the 3 levels
		optionData.add(new BooleanOption(setUpLongAL(6580989,4787093,4787117), setUpShortAL(1,1,1), setUpShortAL(0,0,0), "Aug C. Defibrillator OFF", "Disables free initial augmentation CardiovertorDefibrillator, that is the auto healing", false)); //hint: 05 ... 01 near aug name for the 3 levels
		
		optionData.add(new BooleanOption(setUpLongAL(4570015,4570016), setUpShortAL(0xAA,0x42), setUpShortAL(0xAA,0x43), "BioCell Energy 100%", "BioCell recovers 100% of the energy bar if available", false)); //hint: seek after 72C142AA. The full value is 2 bytes, 16bits (at least)
	}
}

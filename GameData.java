import options.Option;
import options.SectionLabelOption;

import java.util.ArrayList;

public abstract class GameData
{	
	protected ArrayList<Option> optionData;
	public void fillOptionData(){}
	
	public static String g_strSectionInit="> ";
	public void addSection(String str)
	{
		if(optionData.size()%2==1)optionData.add(new SectionLabelOption(""));
		optionData.add(new SectionLabelOption(g_strSectionInit+str));
		optionData.add(new SectionLabelOption(""));
	}
	
	public ArrayList<Long> setUpLongAL(long ... input)
	{
		ArrayList<Long> tempList = new ArrayList<Long>();
		for (Long l : input)
			tempList.add(l);
		return tempList;
	}
	
	public ArrayList<Short> setUpShortAL(int ... input)
	{
		ArrayList<Short> tempList = new ArrayList<Short>();
		for (int l : input)
			tempList.add((short) l);
		return tempList;
	}
}

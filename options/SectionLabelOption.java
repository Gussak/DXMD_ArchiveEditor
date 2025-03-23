package options;

import java.io.RandomAccessFile;

public class SectionLabelOption extends Option
{
	public SectionLabelOption(String str)
	{
		this.optionName = str;
	}
	
	@Override
	public void makeChanges(RandomAccessFile accessGameFile) { }
	
	@Override
	public boolean isData() {return false;}
}

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import options.BooleanOption;
import options.InventoryXOption;
import options.NumericOption;
import options.Option;
import options.ShortOption;

public class FileAnalyzer
{

	public static void analyze(File gameFile, ArrayList<Option> optionData) throws IOException 
	{
		// We only need to open the file with read access
		RandomAccessFile accessGameFile = new RandomAccessFile(gameFile, "r");
		
		for (Option option : optionData)
		{
			if (option instanceof ShortOption)
			{
				accessGameFile.seek(option.getAddresses().get(0)); // Operating under the assumption that they are all going to be the same
				short firstByte = signedByteToUShort(accessGameFile.readByte());
				accessGameFile.seek(option.getAddresses().get(0) + 1);
				short secondByte = signedByteToUShort(accessGameFile.readByte());
				
				int shortTotal = firstByte + (256 * secondByte);
				((NumericOption) option).setCurrentFileValue(shortTotal);
			}
			else if (option instanceof InventoryXOption)
			{
				accessGameFile.seek(option.getAddresses().get(0)); // Operating under the assumption that they are all going to be the same				
				((NumericOption) option).setCurrentFileValue(signedByteToUShort(accessGameFile.readByte()));
			}
			else
			{
				accessGameFile.seek(option.getAddresses().get(0)); // Operating under the assumption that any changes already made will be done by this program
				if (signedByteToUShort(accessGameFile.readByte()) == ((BooleanOption) option).getSpecificValueFalseVals(0))
					((BooleanOption) option).setCurrentFileValue(false);
				else
					((BooleanOption) option).setCurrentFileValue(true);
			}
		}
		
		accessGameFile.close();		
	}
	
	// Signed shorts are used in replacement for unsigned bytes
	private static short signedByteToUShort(byte byteInput)
	{
		if (byteInput < 0)
			return (short) ((128 + byteInput) + 128);
		else
			return byteInput;
	}
	
}

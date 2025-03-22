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
				
				/* INFO: this checks only the first value of the checkbox(boolean like) array, so a big 16bits value or bigger could fail if the first byte remains unchanged
				if (signedByteToUShort(accessGameFile.readByte()) == ((BooleanOption) option).getSpecificValueFalseVals(0))
					((BooleanOption) option).setCurrentFileValue(false);
				else
					((BooleanOption) option).setCurrentFileValue(true);
				*/
				
				// tries to match all bytes meaning the check box is ON, if any fails, check box goes OFF
				boolean bArrayFullyMatches=true;
				for(int i=0; i < ((BooleanOption) option).getAddressesSize(); i++)
				{
					if (signedByteToUShort(accessGameFile.readByte()) != ((BooleanOption) option).getSpecificValueTrueVals(i))
					{
						bArrayFullyMatches=false;
						break;
					}
				}
				((BooleanOption) option).setCurrentFileValue(bArrayFullyMatches);
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

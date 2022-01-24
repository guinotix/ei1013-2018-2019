package practica8;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Frequencies
{
	int freqs[] = new int[256];
	char chars[] = new char[256];
	
	public Frequencies()
	{
		for (int i=0; i<256; i++)
		{
			freqs[i] = 0;
			chars[i] = (char) i;
		}
	}

	//loads a text file and computes character frequencies
	public boolean loadFile(String filename) throws IOException
	{
		RandomAccessFile inputFile = new RandomAccessFile (filename, "r");

		int count = 0;
		
		while (count < inputFile.length()) {
			char c = (char)inputFile.readUnsignedByte();
			count++;
			freqs[c]++;
		}

		inputFile.close();

		sortTable();
		
		return true;
	}

	public int[] frequenciesTable()
	{
		return freqs;
	}
	

	public char[] charsTable()
	{
		return chars;
	}

	private void sortTable()
	{
		for (int i=0; i<255; i++)
		{
			for (int j=i+1; j<256; j++)
			{
				if (freqs[i] < freqs[j])
				{
					int f = freqs[i];
					freqs[i] = freqs[j];
					freqs[j] = f;
					
					char c = chars[i];
					chars[i] = chars[j];
					chars[j] = c;
				}
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder retVal = new StringBuilder();
		for (int i=0; i<256; i++) retVal.append("[" + i + "] (" + (chars[i] >= ' ' ? chars[i] :
                ("\\" + Character.getNumericValue(chars[i])))  +
				") = " + freqs[i] + "\n");
		return retVal.toString();
	}
}

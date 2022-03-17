package assignment10;


public class MoveToFront 
{
	private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
    	char[] alph = new char[R];
    	for(int i = 0; i < R; i++)
    		alph[i] = (char)i;
    	while(!BinaryStdInputStream.isEmpty())
    	{
    		char c = BinaryStdInputStream.readChar();
    		int pos = 0;
    		for(int i = 0; i < R; i++)
    			if(alph[i] == c) 
    			{
    				BinaryStdOutputStream.write(i, 8);
    				pos = i;
    			}
    		for(int i = pos; i > 0; i--)
    			alph[i] = alph[i - 1];
    		alph[0] = c;
    	}
    	BinaryStdOutputStream.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
    	char[] alph = new char[R];
    	for(int i = 0; i < R; i++)
    		alph[i] = (char)i;
    	while(!BinaryStdInputStream.isEmpty())
    	{
    		int c = BinaryStdInputStream.readChar();
    		BinaryStdOutputStream.write(alph[c], 8);
    		char item = alph[c];
    		for(int i = c; i > 0; i--)
    			alph[i] = alph[i - 1];
    		alph[0] = item;
    	}
    	BinaryStdOutputStream.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args)
    {
    	if(args[0].equals("+")) decode();
    	else if(args[0].equals("-")) encode();
    }

}

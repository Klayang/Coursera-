package assignemnt6;

public class Outcast 
{
	private WordNet wordnet;
	public Outcast(WordNet wordnet) {this.wordnet = wordnet;}         // constructor takes a WordNet object
	public String outcast(String[] nouns) 
	{
		String outcast_one = null;
		int maxlength = 0;
		for(String each_one: nouns)
		{
			int length = 0;
			for(String one: nouns)
				length += wordnet.distance(each_one, one);
			if(length > maxlength)
			{
				maxlength = length;
				outcast_one = each_one;
			}
		}
		return outcast_one;	
	}   // given an array of WordNet nouns, return an outcast
	public static void main(String[] args) {}  // see test client below
}

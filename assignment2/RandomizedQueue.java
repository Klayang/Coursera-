package assignment2;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
	private int N = 0;
	private Item[] randomized_queue = (Item[])new Object[1];
	public boolean isEmpty()
	{
		return N == 0;
	}
	public int size()
	{
		return N;
	}
	public Item sample()
	{
		if(N == 0) throw new NoSuchElementException();
		else return randomized_queue[StdRandom.uniform(N)];
	}
	public void enqueue(Item item)
	{
		if(item == null) throw new IllegalArgumentException();
		else {
			if(N == randomized_queue.length) resize(2 * N);
			randomized_queue[N] = item;
			N++;
		}
	}
	public Item dequeue()
	{
		if(N == 0) throw new NoSuchElementException();
		else
		{
			if(N <= randomized_queue.length / 4) resize(randomized_queue.length / 2);
			adjust();
			StdRandom.shuffle(randomized_queue);
			Item item = randomized_queue[--N];
			randomized_queue[N] = null;
			return item;
		}
	}
	private void resize(int max)
	{
		Item[] new_array = (Item[])new Object[max];
		for(int i = 0; i < N; i++)
		{
			new_array[i] = randomized_queue[i];
		}
		randomized_queue = new_array;
	}
	private void adjust()
	{
		Item[] item = Arrays.copyOf(randomized_queue, N);
		randomized_queue = item;
	}
	@Override
	public Iterator<Item> iterator() 
	{
		return new randomized_queue_Iterator();
	}
	private class randomized_queue_Iterator implements Iterator<Item>
	{
		Item[] item = (Item[]) new Object[N];
		randomized_queue_Iterator()
		{
			item = Arrays.copyOf(randomized_queue, N);
			StdRandom.shuffle(item);
		}
		private int index = 0;
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return index != N;
		}

		public Item next() {
			// TODO Auto-generated method stub
			if(index >= N) throw new NoSuchElementException();
			else {
				index++;
				return item[index - 1];
			}
		}
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	public static void main(String[] argv)
	{
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
		         rq.isEmpty();     
		         rq.size();        
		         rq.enqueue(922);
		         rq.dequeue();     
		         rq.enqueue(376);
		         rq.enqueue(436);
		         rq.enqueue(853);
		         rq.isEmpty();     
		         StdOut.println(rq.dequeue());     
		         StdOut.println(rq.dequeue());     
	}
}
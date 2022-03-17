package assignment2;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item>
{
	private class Node{
		Item item;
		Node next;
		Node previous;
	}
	private int N;
	private Node head;
	private Node tail;
	public boolean isEmpty()
	{
		return head == null;
	}
	public int size()
	{
		return N;
	}
	public void addFirst(Item item)
	{
		if(item == null) throw new IllegalArgumentException();
		else
		{
			Node oldhead = head;
			head = new Node();
			head.item = item;
			head.previous = null;
			head.next = oldhead;
			if(N == 0) tail = head;
			else oldhead.previous = head;
			N++;
		}
	}
	public void addLast(Item item)
	{
		if(item == null) throw new IllegalArgumentException();
		else
		{
			Node oldtail = tail;
			tail = new Node();
			tail.item = item;
			tail.next = null;
			tail.previous = oldtail;
			if(N == 0) head = tail;
			else oldtail.next = tail;
			N++;
		}
	}
	public Item removeFirst()
	{
		if(isEmpty()) throw new NoSuchElementException();
		else
		{
			Item item = head.item;
			head = head.next;
			N--;
			if(N == 0) tail = null;
			return item;
		}
	}
	public Item removeLast()
	{
		if(isEmpty()) throw new NoSuchElementException();
		else
		{
			Item item = tail.item;
			tail = tail.previous;
			if(N == 1) head = null;
			else tail.next = null;
			N--;
			return item;
		}
	}
	@Override
	public Iterator<Item> iterator() 
	{
		return new DequeIterator();
	}
	private class DequeIterator implements Iterator<Item>
	{
		Node current = head;
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next()
		{
			if(current == null) throw new NoSuchElementException();
			else 
			{
				Item item = current.item;
				current = current.next;
				return item;
			}
		}
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	public static void main(String[] argv)
	{
		Deque<Integer> deque = new Deque<Integer>();
		         deque.size();
		         deque.size();
		         deque.addFirst(5);
		         deque.removeFirst();  
		         deque.addFirst(7);
		         deque.isEmpty();         
		         deque.addFirst(9);
		         deque.isEmpty();       
		         deque.removeLast();
	}
}

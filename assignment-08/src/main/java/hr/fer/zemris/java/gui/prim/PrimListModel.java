package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Implementaion of a list model that can store prime numbers.
 * 
 * @author jankovidakovic
 *
 */
public class PrimListModel implements ListModel<Integer> {

	private int nextPrime; // next prime number to be stored
	private List<Integer> primes; // list of currently stored prime numbers

	private List<ListDataListener> listeners; // listeners of the model

	public PrimListModel() {
		nextPrime = 1; // not actually prime but it does not matter
		primes = new ArrayList<Integer>();
		listeners = new ArrayList<ListDataListener>();
		primes.add(nextPrime); // add the first number

	}
	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);

	}

	/**
	 * Calculates the next prime, inserts it into the inner list and notifies
	 * all the listeners that the inner list has changed.
	 */
	public void next() {
		nextPrime++;
		while (!isPrime(nextPrime)) {
			nextPrime++;
		}
		primes.add(nextPrime);

		ListDataEvent event = new ListDataEvent(this,
				ListDataEvent.INTERVAL_ADDED, getSize(), getSize());
		for (ListDataListener listener : listeners) {
			listener.intervalAdded(event);
		}
	}

	/**
	 * Checks whether a given number is prime. Prime numbers are divisible only
	 * be 1 and by themselves.
	 * 
	 * @param  n number to be checked
	 * @return   <code>true</code> if number is prime,
	 *           <code>false</code>otherwise
	 */
	public boolean isPrime(int n) {
		for (int i = 2; i * i <= n; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

}

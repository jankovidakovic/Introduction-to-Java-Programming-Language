package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Program that demonstrates the functionality of <code>processRemaining</code>
 * method of element getters. Method should consume all the remaining elements
 * and process them using the provided method.
 *
 * @author jankovidakovic
 *
 */
public class ElementsGetterProcessRemainingDemo {

	public static void main(String[] args) {
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna"); //fill with elements
		
		ElementsGetter getter = col.createElementsGetter();
		getter.getNextElement();
		
		//should print "Ana" then "Jasna"
		getter.processRemaining(System.out::println);
	}

}

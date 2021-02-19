package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Program that demonstrates the idempotent property of <code>ElementsGetter</code>
 * class. Specifically, if no new elements have been retrieved by 
 * <code>getNextElement</code>, concurrent calls of <code>hasNextElement</code>
 * should all have the same result.
 *
 * @author jankovidakovic
 *
 */
public class ElementsGetterIdempotentDemo {

	public static void main(String[] args) {
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna"); //fill with elements
		
		ElementsGetter getter = col.createElementsGetter();
		
		//should print "true" 5 times
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		
		//should print "Iva"
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "true" 2 times
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		
		//should print "Ana"
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "true" 3 times
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
		
		//should print "Jasna"
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "false" 2 times
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Has more elements: " + getter.hasNextElement());
				
	}

}

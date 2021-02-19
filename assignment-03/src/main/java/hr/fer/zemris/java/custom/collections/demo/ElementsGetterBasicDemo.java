package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Program that demonstrates the basic functionality of <code>ElementsGetter</code>
 * objects. If there are more elements that have not yet been retrieved, <code>hasNextElement</code> outputs
 * <code>true</code>, and otherwise it outputs <code>false</code>. Also, when
 * there are no more elements, <code>getNextElement<code> throws an exception.
 *
 * @author jankovidakovic
 *
 */
public class ElementsGetterBasicDemo {
	
	public static void main(String[] args) {
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna"); //fill with elements
		
		ElementsGetter getter = col.createElementsGetter();
		
		//should print "true" and "Ivo"
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "true" and "Ana"
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "true" and "Jasna"
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Next element: " + getter.getNextElement());
		
		//should print "false" and cause an exception
		System.out.println("Has more elements: " + getter.hasNextElement());
		System.out.println("Next element: " + getter.getNextElement());
		
	}
}

package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Program that demonstrates the independence of the instances of
 * <code>ElementsGetter</code> classes. They all retrieve elements independently,
 * and do not influence each other even though they operate upon the same
 * collection.
 *
 * @author jankovidakovic
 *
 */
public class ElementsGetterIndependentInstances {

	public static void main(String[] args) {
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna"); //fill with elements
		
		//two separate instances
		ElementsGetter getter1 = col.createElementsGetter();
		ElementsGetter getter2 = col.createElementsGetter();
		
		//should print "Ivo" and "Ana"
		System.out.println("Next element of getter1: " + getter1.getNextElement());
		System.out.println("Next element of getter1: " + getter1.getNextElement());
		
		//should print "Ivo" - independent of getter1
		System.out.println("Next element of getter2: " + getter2.getNextElement());
		
		//should print "Jasna"
		System.out.println("Next element of getter1: " + getter1.getNextElement());
		
		//should print "Ana"
		System.out.println("Next element of getter2: " + getter2.getNextElement());
		
	}

}

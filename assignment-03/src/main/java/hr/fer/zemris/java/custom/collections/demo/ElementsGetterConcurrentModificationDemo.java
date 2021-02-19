package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Program which demonstrates the behavior of <code>ElementsGetter</code> objects,
 * specifically how they deal with unsupported modifications of the collection
 * which they refer to. When such modification is made, any attempt of using 
 * some active <code>ElementsGetter</code> which works with newly modified
 * collection will cause an exception.
 *
 * @author jankovidakovic
 *
 */
public class ElementsGetterConcurrentModificationDemo {

	public static void main(String[] args) {
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna"); //fill with elements
		
		ElementsGetter getter = col.createElementsGetter();
		
		//next 2 lines are okay - collection was not modified yet
		System.out.println("Single element: " + getter.getNextElement());
		System.out.println("Single element: " + getter.getNextElement());
		
		col.clear(); //modification of the collection is not supported by getter
		
		//this line should throw an exception
		System.out.println("Single element: " + getter.getNextElement());
		
	}

}

package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

public class ProcessRemainingDemo {
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		col.add("Z");
		col.add("plus");
		col.add("PLUS");
		
		ElementsGetter getter = col.createElementsGetter();
		getter.getNextElement();
		
		getter.processRemaining(System.out::println);
	}
}

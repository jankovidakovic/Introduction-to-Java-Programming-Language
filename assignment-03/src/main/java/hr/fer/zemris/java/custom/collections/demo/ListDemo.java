package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;

public class ListDemo {
	public static void main(String[] args) {
		List col1 = new ArrayIndexedCollection();
		List col2 = new LinkedListIndexedCollection();
		col1.add("Ivana");
		col2.add("Jasna");
		
		Collection col3 = col1;
		Collection col4 = col2;
		
		col1.get(0);
		col2.get(0);
		//col3.get(0); - this does not compile because col3 is instance of 
		//Collection, which doesn't define the get method
		//col4.get(0); - this also does not compile for the same reason
		
		//should print "Ivana"
		col1.forEach(System.out::println);
		
		//should print "Jasna"
		col2.forEach(System.out::println);
		
		//should print "Ivana"
		col3.forEach(System.out::println);
		
		//should print "Jasna"
		col4.forEach(System.out::println);
		
	}
}

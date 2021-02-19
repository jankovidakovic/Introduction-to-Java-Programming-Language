package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Program that demonstrates the functionality of <code>Tester</code> interface.
 * It creates an instance of Tester which tests whether a given value is an
 * even number, and then checks its functionality.
 *
 * @author jankovidakovic
 *
 */
public class EvenIntegerTester implements Tester {
	@Override
	public boolean test(Object obj) {
		if (!(obj instanceof Integer)) return false;
		Integer i = (Integer) obj;
		return i%2 == 0;	//it is an even number
	}
	
	public static void main(String[] args) {
		Tester t = new EvenIntegerTester();
		
		System.out.println(t.test("Ivo")); //false
		System.out.println(t.test(22));		//true
		System.out.println(t.test(31));		//false
		
	}
}



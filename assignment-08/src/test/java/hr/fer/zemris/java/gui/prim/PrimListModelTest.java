package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JList;

import org.junit.jupiter.api.Test;

class PrimListModelTest {

	@Test
	void test() {
		PrimListModel model = new PrimListModel();
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);

		assertEquals(1, list1.getModel().getElementAt(0));
		assertEquals(1, list2.getModel().getElementAt(0));

		model.next();

		assertEquals(2, list1.getModel().getElementAt(1));
		assertEquals(2, list2.getModel().getElementAt(1));

	}

}

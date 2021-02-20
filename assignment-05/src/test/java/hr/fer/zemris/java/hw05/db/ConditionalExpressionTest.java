package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {

	 @Test
	 void test() {
		 ConditionalExpression expr = new ConditionalExpression(
				 FieldValueGetters.LAST_NAME,
				 "Iv*",
				 ComparisonOperators.LIKE);
		 
		 StudentRecord record = new StudentRecord("0000000001", "Ivić",
				 "Ivan", 5);
		 
		 assertEquals(true, expr.getComparisonOperator().satisfied(
				 expr.getFieldValueGetter().get(record), 
				 expr.getStringLiteral()
			));
	 }

}

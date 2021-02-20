package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Query filter that can filter student records based on a query which is
 * passed in the constructor.
 *
 * @author jankovidakovic
 *
 */
public class QueryFilter implements IFilter {

	private final List<ConditionalExpression> query; //query

	/**
	 * Initializes the filter with respect to the given query
	 *
	 * @param query query by which the records can be filtered
	 */
	public QueryFilter(List<ConditionalExpression> query) {
		this.query = query;
	}

	/**
	 * Filters the given record using the query. Filtering is done by 
	 * evaluating every expression of the query upon the record, and
	 * obtaining the end result. As the only logical operator supported
	 * is "AND", the record passes the filter if and only if all expression
	 * evaluate to true. If they do not, the record is filtered.
	 *
	 * @param record record to be filtered
	 * @return <code>true</code> if record passes the filter according
	 * to the query, <code>false</code> otherwise.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		boolean evaluation = true; //final result
		
		for (ConditionalExpression expr : query) {
			evaluation &= expr.evaluate(record); //filter through every expression
		}
		return evaluation;
	}

}

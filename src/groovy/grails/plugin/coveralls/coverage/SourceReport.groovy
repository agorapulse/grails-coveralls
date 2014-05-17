package grails.plugin.coveralls.coverage

/**
 * The model class of the report of a source file for Coveralls' format.
 */
class SourceReport {

	String name
	String source
	List<Integer> coverage

}
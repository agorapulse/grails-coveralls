package grails.plugin.coveralls.coverage

/**
 * The interface for factory classes of SourceReport.
 */
interface SourceReportFactory {
	List<SourceReport> createReportList(File file)
}

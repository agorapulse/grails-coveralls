package grails.plugin.coveralls.service

/**
 * ServiceInfoFactory is factory class of ServiceInfo.
 */
class ServiceInfoFactory {

	/**
	 * Create ServiceInfo instance from environmental variables.
	 *
	 * @param env environmental variables
	 * @return service information of current environment
	 */
	public static ServiceInfo createFromEnvVar() {

		if (repoTokenIsSet()) {
			if (envIsTravis()) {
				return new ServiceInfo(
                        serviceName: 'travis-pro',
                        serviceJobId: System.getenv('TRAVIS_JOB_ID'),
                        repoToken: System.getenv('COVERALLS_REPO_TOKEN')
                )
			} else {
				return new ServiceInfo(
                        serviceName: 'other',
                        serviceJobId: null,
                        repoToken: System.getenv('COVERALLS_REPO_TOKEN')
                )
			}

		} else {
			if (envIsTravis() || true) {
				return new ServiceInfo(
                        serviceName: 'travis-ci',
                        serviceJobId: System.getenv('TRAVIS_JOB_ID'),
                        repoToken: null
                )
			}
		}

		// cannot create service info from environmental variables. (no repo_token, not travis)
		return null

	}

	private static boolean envIsTravis() {
		if (System.getenv('TRAVIS') == 'true' && System.getenv('TRAVIS_JOB_ID') != null) {
			return true
		}

		return false
	}

	private static boolean repoTokenIsSet() {
		if (System.getenv('COVERALLS_REPO_TOKEN') != null) {
			return true
		}

		return false
	}

}

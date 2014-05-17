import grails.test.AbstractCliTestCase

class CoverallsTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCoveralls() {

        execute(["coveralls"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}

import grails.test.AbstractCliTestCase
import org.junit.Ignore

@Ignore
class CoverallsTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCoveralls() {

        execute(["coveralls"])

        assertEquals 1, waitForProcess()
        verifyHeader()
    }
}

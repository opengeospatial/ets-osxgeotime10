package org.opengis.cite.osxgeotime;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import org.opengis.cite.osxgeotime.util.XMLUtils;
import org.opengis.cite.osxgeotime.util.TestSuiteLogger;
import org.opengis.cite.osxgeotime.util.URIUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;
import org.w3c.dom.Document;

/**
 * A listener that performs various tasks before and after a test suite is run,
 * usually concerned with maintaining a shared test suite fixture. Since this
 * listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite
 * definition and before any annotated configuration methods.
 *
 * Attributes set on an ISuite instance are not inherited by constituent test
 * group contexts (ITestContext). However, suite attributes are still accessible
 * from lower contexts.
 *
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        processSuiteParameters(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        Reporter.clear(); // clear output from previous test runs
        Reporter.log("Test suite parameters:");
        Reporter.log(suite.getXmlSuite().getAllParameters().toString());
    }

    /**
     * Processes test suite parameters and sets suite attributes accordingly.
     * For example, parsing the entity referenced by the "iut" parameter
     * value and setting the resulting Document as the value of the "testSubject"
     * attribute.
     *
     * @param suite An ISuite object representing a TestNG test suite.
     */
    void processSuiteParameters(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log(Level.CONFIG, "Suite parameters\n" + params.toString());
        String iutRef = params.get(TestRunArg.IUT.toString());

        if ((null == iutRef) || iutRef.isEmpty()) {
            throw new IllegalArgumentException("Required parameter not found");
        }
        URI iutURI = URI.create(iutRef);
        Document doc = null;
        try {
            doc = URIUtils.resolveURIAsDocument(iutURI);
        } catch (Exception ex) {
            // push exception up through ISuiteListener interface
            throw new RuntimeException("Failed to parse XML resource at "
                    + iutURI, ex);
        }
        if (null != doc) {
            suite.setAttribute(SuiteAttribute.TEST_SUBJECT.getName(), doc);
            if (TestSuiteLogger.isLoggable(Level.FINE)) {
                StringBuilder logMsg = new StringBuilder("Parsed resource from ");
                logMsg.append(iutURI).append("\n");
                logMsg.append(XMLUtils.writeNodeToString(doc));
                TestSuiteLogger.log(Level.FINE, logMsg.toString());
            }
        }


        String rncRef = params.get(TestRunArg.RNC.toString());
        if ((null == rncRef) || rncRef.isEmpty()) {
            throw new IllegalArgumentException("Required parameter not found");
        }
        suite.setAttribute(SuiteAttribute.RELAX_NG.getName(), rncRef);



    }
}

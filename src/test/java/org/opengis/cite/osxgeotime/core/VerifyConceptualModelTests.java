package org.opengis.cite.osxgeotime.core;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the ConceptualModelTests class.
 */
public class VerifyConceptualModelTests {

    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public VerifyConceptualModelTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    @Test
    public void supplyValidAtomFeedViaTestContext() throws SAXException,
            IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atomgeo01.xml"));
        when(suite.getAttribute(SUBJ)).thenReturn(doc);
        RelaxNGTest iut = new RelaxNGTest();
        iut.setupClassFixture(testContext);
        iut.setRncFile("/org/opengis/cite/osxgeotime/schemas/atomgeo.rnc");
        iut.validateXMLfromRNC();
    }

    @Test
    public void supplyInvalidAtomFeed() throws SAXException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("1 schema validation error(s) detected");
        RelaxNGTest iut = new RelaxNGTest();
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atomgeotime01_nogeorss_noauthor.xml"));
        iut.setTestSubject(doc);
        iut.setRncFile("/org/opengis/cite/osxgeotime/schemas/atomgeo.rnc");
        iut.validateXMLfromRNC();
    }
}

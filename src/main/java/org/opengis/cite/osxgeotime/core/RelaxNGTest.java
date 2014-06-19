package org.opengis.cite.osxgeotime.core;

import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.xerces.util.XMLCatalogResolver;
import org.opengis.cite.osxgeotime.ETSAssert;
import org.opengis.cite.osxgeotime.ErrorMessage;
import org.opengis.cite.osxgeotime.ErrorMessageKeys;
import org.opengis.cite.osxgeotime.SuiteAttribute;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * Includes tests based on the fundamental conceptual model.
 *
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>OGC ij-xyz, cl. 2: Conformance classes</li>
 * <li>ISO 19ijk:20nn, cl. x.y: Title</li>
 * </ul>
 */
public class RelaxNGTest {

    private Document testSubject;
    private String rncFile;
    private LSResourceResolver resolver;
    private static final String ENTITY_CATALOG = "/org/opengis/cite/osxgeotime/schema-catalog.xml";

    /**
     * Obtains the test subject from the ISuite context and creates a
     * catalog-based resource resolver. The suite attribute
     * {@link org.opengis.cite.osxgeotime.SuiteAttribute#TEST_SUBJECT} should evaluate
     * to a DOM Document node.
     *
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    public void setupClassFixture(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
        }

        Object objRnc = testContext.getSuite().getAttribute(
                SuiteAttribute.RELAX_NG.getName());

        if ((null != objRnc) && String.class.isAssignableFrom(objRnc.getClass())) {
            this.rncFile = String.class.cast(objRnc);
        }

        URL catalog = getClass().getResource(ENTITY_CATALOG);
        String[] catalogList = new String[] { catalog.toString() };
        this.resolver = new XMLCatalogResolver(catalogList);
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject
     *            A Document node representing the test subject or metadata
     *            about it.
     */
    public void setTestSubject(Document testSubject) {
        this.testSubject = testSubject;
    }

    /**
     * Sets the Schema File. This method is intended to facilitate unit
     * testing.
     *
     * @param rncFile
     *            A Document node representing the test subject or metadata
     *            about it.
     */

    public void setRncFile(String rncFile) {
        this.rncFile = rncFile;
    }

    /**
     * [{@code Test}] Verifies that the test subject is a valid OpenSearch
     * description document according to the (informative) RELAX NG grammar
     * listed in OGC 10-032
     *

     * @see <a href="http://schemas.opengis.net/opensearchgeo/1.0/schemas/osddgeo.rnc">osddgeo.rnc</a>,
     *      RELAX NG Compact Schema
     */
    @Test
    public void validateXMLfromRNC() throws SAXException, IOException {

        URL schemaRef = getClass().getResource(
                this.rncFile );
                //"/org/opengis/cite/osxgeotime/schemas/osdd.rnc");


        RelaxNGValidator validator = new RelaxNGValidator(schemaRef,
                this.resolver);
        Source xmlSource = (null != testSubject) ? new DOMSource(
                this.testSubject) : null;

        validator.validate(xmlSource);
        ValidationErrorHandler err = validator.getErrorHandler();
        Assert.assertFalse(
                err.errorsDetected(),
                ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                        err.getErrorCount(), err.toString() + "42" + this.rncFile + "42" ));

        // Check Schematron Rules

/*
        URL schRef = this.getClass().getResource(
                "/org/opengis/cite/osxgeotime/tests/result-osd.rnc.sch");
        ETSAssert
                .assertSchematronValid(schRef, new DOMSource(this.testSubject));
*/

    }


    /**
     * [{@code Test}] Verifies that the test subject respects the additional
     * conformance classes defined in OGC 10-032
     *
     */
     /*
    @Test
    public void validateAdditionalConformance() throws SAXException, IOException {

        validateRNCandSCH( "/org/opengis/cite/osxgeotime/tests/result-osd.rnc");
        URL schemaRef = getClass().getResource(
                "/org/opengis/cite/osxgeotime/tests/result-osd.rnc");
        RelaxNGValidator validator = new RelaxNGValidator(schemaRef,
                this.resolver);
        Source xmlSource = (null != testSubject) ? new DOMSource(
                this.testSubject) : null;
        validator.validate(xmlSource);
        ValidationErrorHandler err = validator.getErrorHandler();
        Assert.assertFalse(
                err.errorsDetected(),
                ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                        err.getErrorCount(), err.toString()));

        // Check Schematron Rules

        URL schRef = this.getClass().getResource(
                "/org/opengis/cite/osxgeotime/tests/result-osd.rnc.sch");
        ETSAssert
                .assertSchematronValid(schRef, new DOMSource(this.testSubject));


    }*/
/*
    private void validateRNCandSCH( String file) throws SAXException, IOException {
        URL schemaRef = getClass().getResource( file );
        RelaxNGValidator validator = new RelaxNGValidator(schemaRef,
                this.resolver);
        Source xmlSource = (null != testSubject) ? new DOMSource(
                this.testSubject) : null;
        validator.validate(xmlSource);
        ValidationErrorHandler err = validator.getErrorHandler();
        Assert.assertFalse(
                err.errorsDetected(),
                ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                        err.getErrorCount(), err.toString()));

        // Check Schematron Rules

        URL schRef = this.getClass().getResource( file + ".sch");
        ETSAssert
                .assertSchematronValid(schRef, new DOMSource(this.testSubject));


    }
*/
    /**
     * [{@code Test}] Verify that the test subject satisfies the supplementary
     * Schematron constraints defined in RFC 4287 and Geo-Time Extensions.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4287">RFC 4287</a>

    @Test
    public void checkSchematronRules() throws SAXException, IOException {
        URL schRef = this.getClass().getResource(
                "/org/opengis/cite/osxgeotime/sch/result-atom1.0.sch");
        ETSAssert
                .assertSchematronValid(schRef, new DOMSource(this.testSubject));
    }
*/
}

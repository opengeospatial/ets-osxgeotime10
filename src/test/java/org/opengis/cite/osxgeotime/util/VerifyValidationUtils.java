package org.opengis.cite.osxgeotime.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Test;
import org.opengis.cite.validation.SchematronValidator;

/**
 * Verifies the behavior of the ValidationUtils class.
 */
public class VerifyValidationUtils {

    public VerifyValidationUtils() {
    }

    @Test
    public void testBuildSchematronValidator() {
        String schemaRef = "http://schemas.opengis.net/gml/3.2.1/SchematronConstraints.xml";
        String phase = "";
        SchematronValidator result = ValidationUtils.buildSchematronValidator(
                schemaRef, phase);
        Assert.assertNotNull(result);
    }

    @Test
    public void extractRelativeSchemaReference() throws FileNotFoundException,
            XMLStreamException {
        File xmlFile = new File("src/test/resources/Alpha-1.xml");
        URI xsdRef = ValidationUtils.extractSchemaReference(new StreamSource(
                xmlFile), null);
        Assert.assertTrue("Expected schema reference */xsd/alpha.xsd", xsdRef
                .toString().endsWith("/xsd/alpha.xsd"));
    }
}

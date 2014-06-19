package org.opengis.cite.osxgeotime;

/**
 * XML namespace names.
 *
 * @see <a href="http://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 *
 */
public class Namespaces {

    private Namespaces() {
    }

    /** SOAP 1.2 message envelopes. */
    public static final String SOAP_ENV = "http://www.w3.org/2003/05/soap-envelope";
    /** W3C XLink */
    public static final String XLINK = "http://www.w3.org/1999/xlink";
    /** OGC 06-121r3 (OWS 1.1) */
    public static final String OWS = "http://www.opengis.net/ows/1.1";
    /** ISO 19136 (GML 3.2) */
    public static final String GML = "http://www.opengis.net/gml/3.2";
    /** Atom rfc4287 */
    public static final String ATOM = "http://www.w3.org/2005/Atom";
    /** OpenSearch  */
    public static final String OS = "http://a9.com/-/spec/opensearch/1.1/";
    /** OpenSearch Geo Extension */
    public static final String OSGEO = "http://a9.com/-/opensearch/extensions/geo/1.0/";
    /** OpenSearch Time Extension */
    public static final String OSTIME = "http://a9.com/-/opensearch/extensions/time/1.0/";
    /** GeoRSS */
    public static final String GEORSS = "http://www.georss.org/georss";
    /** Dublin Core Elements */
    public static final String DC = "http://purl.org/dc/elements/1.1/";
}

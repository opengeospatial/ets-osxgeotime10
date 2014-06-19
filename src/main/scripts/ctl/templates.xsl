<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:os="http://a9.com/-/spec/opensearch/1.1/"

      xmlns:atom="http://www.w3.org/2005/Atom"
      xmlns:dc="http://purl.org/dc/elements/1.1/"
      xmlns:geo="http://a9.com/-/opensearch/extensions/geo/1.0/"
      xmlns:time="http://a9.com/-/opensearch/extensions/time/1.0/"
      xmlns:dct="http://purl.org/dc/terms/"

      version="2.0">

<!--
    XSLT for reading and instanciating OpenSearch URL Templates
    Author: Pedro Gonçalves <pedro.goncalves@terradue.com>
    Copyright (c) 2014 Terradue
-->

<xsl:template name="tokenize-url-template">
      <xsl:param name="string"/>
      <xsl:param name="separator-start" select="'{'"/>
      <xsl:param name="separator-stop" select="'}'"/>
      <xsl:param name="context" select="."/>
      <xsl:param name="namespaces">
        <ns prefix="os">http://a9.com/-/spec/opensearch/1.1/</ns>
        <ns prefix="geo">http://a9.com/-/opensearch/extensions/geo/1.0/</ns>
        <ns prefix="time">http://a9.com/-/opensearch/extensions/time/1.0/</ns>
      </xsl:param>


    <xsl:if test="string-length($string) &gt; 0">

          <xsl:variable name="before-separator" select="substring-before($string, $separator-start)"/>
          <xsl:variable name="after-separator" select="substring-after($string, $separator-stop)"/>
          <xsl:variable name="Qtype" select="substring-before(substring-after($string,$separator-start),$separator-stop)"/>
          <xsl:variable name="optional" select="substring-before($Qtype,'?')!=''"/>


          <xsl:variable name="type">
            <xsl:choose>
              <xsl:when test="$context">
                <xsl:variable name="ns">
                  <xsl:for-each select="$context">
                    <xsl:value-of select="namespace::node()[local-name()=substring-before($Qtype,':')]"/>
                  </xsl:for-each>
                </xsl:variable>
                <xsl:variable name="prefix">
                  <xsl:choose>
                    <xsl:when test="$namespaces/ns[.=$ns]">
                      <xsl:value-of select="$namespaces/ns[.=$ns]/@prefix"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="substring-before($Qtype, ':')"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:variable>
                <xsl:variable name="lname">
                  <xsl:choose>
                    <xsl:when test="contains($Qtype, ':')">
                      <xsl:value-of select="substring-after($Qtype, ':')"/>
                    </xsl:when>
                    <xsl:otherwise><xsl:value-of select="$Qtype"/></xsl:otherwise>
                  </xsl:choose>
                </xsl:variable>
                <xsl:value-of select="concat($prefix,':',translate($lname,'?',''))"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$Qtype"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <token>
            <xsl:attribute name="type"><xsl:value-of select="translate($type,'?','')"/></xsl:attribute>
            <xsl:attribute name="optional"><xsl:value-of select="$optional"/></xsl:attribute>

            <xsl:choose>
              <xsl:when test="string-length($before-separator)=0 and string-length($after-separator)=0">
                <xsl:value-of select="$string"/>
              </xsl:when>
              <xsl:otherwise>
                  <xsl:value-of select="$before-separator"/>
              </xsl:otherwise>
            </xsl:choose>
        </token>
        <xsl:if test="not(string-length($before-separator)=0 and string-length($after-separator)=0)">
          <xsl:call-template name="tokenize-url-template">
              <xsl:with-param name="string" select="$after-separator"/>
              <xsl:with-param name="separator-start" select="$separator-start"/>
              <xsl:with-param name="separator-stop" select="$separator-stop"/>
              <xsl:with-param name="context" select="$context"/>
              <xsl:with-param name="namespaces" select="$namespaces"/>

          </xsl:call-template>
        </xsl:if>
      </xsl:if>
  </xsl:template>



 <xsl:template name="instantiate-url-template">
    <xsl:param name="tokens"/>
    <xsl:param name="request"/>

    <xsl:for-each select="$tokens/token">
         <xsl:value-of select="."/>
         <xsl:value-of select="encode-for-uri($request/token[@type=current()/@type])"/>
      </xsl:for-each>
 </xsl:template>




</xsl:transform>

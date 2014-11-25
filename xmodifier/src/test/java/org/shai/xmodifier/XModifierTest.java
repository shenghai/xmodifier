package org.shai.xmodifier;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XModifierTest {

    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xml");
        Document document = readDocument(in);
        XModifier modifier = new XModifier(document);
        modifier.setNamespace("ns", "http://localhost");
        modifier.addModify("/ns:root/ns:element1=text1");
        modifier.addModify("/ns:root/ns:element4=text4");
        modifier.addModify("//ns:element4/ns:element5[@value=100]=text4");
        modifier.modify();
        String expectedXML = "<root xmlns=\"http://localhost\">\n" +
                "    <element1>text1</element1>\n" +
                "    <element2/>\n" +
                "    <element3/>\n" +
                "<element4>text4<element5 value=\"100\">text4</element5>\n" +
                "</element4>\n" +
                "</root>";
        String actualXML = writeXMLToString(document);
        DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(expectedXML, actualXML));
        Assert.assertTrue(diff.getAllDifferences().toString(), diff.identical());
    }

    private Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }

    private Document readDocument(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        return documentBuilder.parse(in);
    }

    public static String formatXMLString(String xmlText) {
        String output;

        Source xmlInput = new StreamSource(new StringReader(xmlText));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(xmlInput, xmlOutput);
            output = xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return output;
    }

    public static String writeXMLToString(Node node) {
        if (node == null) return null;
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(node);
            transformer.transform(source, result);
            return formatXMLString(result.getWriter().toString());
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

}
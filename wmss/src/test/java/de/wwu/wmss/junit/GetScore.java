package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetScore {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;
	private static String source= StartWMSS.source;

	@Test
	public void getScore() {
		boolean result = true;
				
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			URL url = new URL(server+":"+port+"/wmss?source="+source+"&request=GetScore&identifier=http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
			InputStream stream = url.openStream();
			Document doc = docBuilder.parse(stream);
						
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			NodeList subfields = (NodeList) xpath.evaluate("//work/work-title", doc,XPathConstants.NODESET);

			System.out.println("\nMusicXML Title: " +subfields.item(0).getTextContent());
			
		} catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
			result = false;
			e.printStackTrace();
		}
		
		assertEquals(result, true);
	}
}

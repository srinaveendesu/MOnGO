package project.finalyear.init;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLHelper extends DefaultHandler {
	//private String URL_Main = "http://maps.googleapis.com/maps/api/geocode/xml?address=chit+vihar,+sastra+university&sensor=false";
	//private String URL_Main =resp ;
	String TAG = "XMLHelper";

	Boolean currTag = false;
	String currTagVal = "";
	public XmlValue post = null;
	public ArrayList<XmlValue> posts = new ArrayList<XmlValue>();

	public void get(String URL_Main) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(this);
			InputStream inputStream = new URL(URL_Main).openStream();
			reader.parse(new InputSource(inputStream));
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}
	}

	// Receives notification of the start of an element
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		Log.i(TAG, "TAG: " + localName);
		
		currTag = true;
		currTagVal = "";
		if (localName.equals("GeocodeResponse")) {
			post = new XmlValue();
		}

	}

	// Receives notification of end of element
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currTag = false;
		
		if (localName.equalsIgnoreCase("status"))
			post.setstatus(currTagVal);
			
		else if (localName.equalsIgnoreCase("formatted_address"))
			post.setLocation(currTagVal);

		else if(localName.equalsIgnoreCase("lat"))
			post.setLat(currTagVal);
		
		else if(localName.equalsIgnoreCase("lng"))
			post.setLng(currTagVal);
		
		else if (localName.equalsIgnoreCase("location"))
			posts.add(post);

	}

	// Receives notification of character data inside an element 
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currTag) {
			currTagVal = currTagVal + new String(ch, start, length);
			currTag = false;
		}

	}
}
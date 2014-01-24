
package org.toms.integration.telegraph.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
*  xml객체로 부터 파싱하기 위한 xml Utility임  
*  @author airlee
*/
public class XMLUtil {
    /**
	* file의 위치로 부터  XML Document객체로 변환 
	* @param  filePath  :  xml 파일이 위치한 full path
	* @return Element : xml의 Elemnet객체   
	* @exception  
	**/ 	
	public static Element getDocumentFromFile(String classPath) throws Exception {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = fact.newDocumentBuilder();
			Document doc = parser.parse(XMLUtil.class.getResourceAsStream(classPath));
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;			
		} catch (Exception e) {
			e.printStackTrace();
			trace("XmlUtil error: " + e);  
		}
		return null;
	}
    /**
	* xml형태의 string을 XML Document객체로 변환 
	* @param  xmlString  :  xml 문장   
	* @return Element : xml의 Elemnet객체   
	* @exception  
	**/ 
	public static Document getDocument(String xmlString) {
	    try{
	        DocumentBuilderFactory factory = 
	                            DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        ByteArrayInputStream bis = new ByteArrayInputStream(xmlString.getBytes());
	        InputSource xmlSrc = new InputSource(bis);
	        return builder.parse(xmlSrc);
	    }catch(Exception e){
	        trace("XmlUtil error: " + e);           
	    }
	    return null;
	}
    /**
    * xml형태의 string을 XML Element객체로 변환 

    * @param  xmlString  :  xml 문장   
    * @return Element : xml의 Elemnet객체   
    * @exception  
    **/ 
    public static Element getRootElement(String xmlString) {
        try{
            Document doc = getDocument(xmlString);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        }catch(Exception e){
            trace("XmlUtil error: " + e);           
        }
        return null;
    }
    /**
    * InputStream형태의  string을 XML Element객체로 변환 

    * @param  xmlString  :  xml 문장   
    * @return Element : xml의 Elemnet객체   
    * @exception  
    **/ 
    public static Element getRootElement(InputStream is) {
        try {
            DocumentBuilderFactory factory = 
                                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            BufferedReader  br = new BufferedReader( new InputStreamReader( is ));
            InputSource xmlSrc = new InputSource(br);
            Document doc = builder.parse(xmlSrc);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (Exception pce) {
         trace("XmlUtil error: " + pce);
        }
        return null;
    }
	/**
	* 1. 기능 : xml 파싱을 위해 Document객체로 로딩
	**/

	public static Element getRootElement(URL xmlURL) {
		try {
			DocumentBuilderFactory factory = 
								DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
    		
			InputSource xmlSrc = new InputSource(xmlURL.openStream());
			Document doc = builder.parse(xmlSrc);
			Element root = doc.getDocumentElement();
			//root.normalize();
			return root;
		}catch (SAXParseException err) {
			trace("XmlUtil Parsing error" + ", line " +
						err.getLineNumber () + ", uri " + err.getSystemId ());
			trace("XmlUtil error: " + err.getMessage ());
		} catch (SAXException e) {
			trace("XmlUtil error: " + e);
		} catch (java.net.MalformedURLException mfx) {
			trace("XmlUtil error: " + mfx);
		} catch (java.io.IOException e) {
			trace("XmlUtil error: " + e);
		} catch (Exception pce) {
			trace("XmlUtil error: " + pce);
		}
        
		return null;
	}	
  
    //log for debugging 
    private static void trace(String msg)
    {
        System.out.println("XMLUtil =["+msg+"]");  
    }
}



package com.mthiam.gescom.config;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;


public abstract class XmlParser {


    public static Document parserXMLFile(InputStream fileName) throws ParserConfigurationException, IOException, SAXException {

        //File xmlFile = new File(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc =  builder.parse(fileName);
        doc.getDocumentElement().normalize();
        return doc;
    }



    public static void updateXMLFile(Document doc, String fileName) throws TransformerException, URISyntaxException, IOException {
        doc.getDocumentElement().normalize();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

//initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new File(fileName));
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);



    }

    public static String getContentTag(Document doc,String tagName){
        Node tag=doc.getElementsByTagName(tagName).item(0);
        return tag.getTextContent();
    }

    public static void setContextTextToTag(Document doc, String tagName, String text){
        Node node = doc.getElementsByTagName(tagName).item(0).getFirstChild();
        node.setTextContent(text);
    }

    public static void setContextTextToTagWithAttribut(Document doc, String tagName,String attribute,String value,String text){
        for(int i=0;i<doc.getElementsByTagName(tagName).getLength();i++) {

            if(doc.getElementsByTagName(tagName).item(i).getAttributes().getNamedItem(attribute).getTextContent().equals(value)){
                doc.getElementsByTagName(tagName).item(i).setTextContent(text);

            }


        }

    }

}

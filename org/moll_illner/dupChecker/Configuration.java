/*
 * Created on 19.09.2015
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.moll_illner.dupChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Configuration {
    private static Logger log = Logger.getLogger(Configuration.class);
    private List<StartingPoint> _startingPoints;
    
    public Configuration() {
        _startingPoints = new ArrayList<StartingPoint>();
    }
    
    public void readConfiguration(String path) {
        try {
            _startingPoints = new ArrayList<StartingPoint>();
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
                    
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
    
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                    
            Node startingPointListNode = doc.getElementsByTagName("StartingPoints").item(0);
            NodeList startingPointNodes = startingPointListNode.getChildNodes();

            //XPathFactory factory = XPathFactory.newInstance();
            //using this factory to create an XPath object: 
            //XPath xpath = factory.newXPath();

            // XPath Query for showing all nodes value
            //XPathExpression expr = xpath.compile("//" + "item1" + "/*");
            //Object result = expr.evaluate(doc, XPathConstants.NODESET);
            // NodeList nodes = (NodeList) result;
            for (int i = 0; i < startingPointNodes.getLength(); i++) {
                Node startingPointNode = startingPointNodes.item(i);
                NodeList children = startingPointNode.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j); 
                    if (child.getNodeName().equals("Path")) {
                        StartingPoint sp = new StartingPoint(child.getTextContent());
                        _startingPoints.add(sp);
                    }
                }
            }
        }
        catch (Exception ex) { 
            log.error("Fehler beim Einlesen der Konfiguration aus " + path, ex);
            _startingPoints = new ArrayList<StartingPoint>();
        }
    }
    
    
    public List<StartingPoint> getStartingPoints() {
        return _startingPoints;
    }
}

package utils;

import dao.PeopleList;
import model.HealthProfile;
import model.Person;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HealthProfileReader {  	

    private static String xmlPath;
    private static Document doc;
    private static XPathFactory factory;
    private static XPath xpath;

    private static Unmarshaller unmarshaller;


    // Class constructors

    public HealthProfileReader() throws ParserConfigurationException, SAXException,
            IOException, JAXBException {
        this("data/people.xml");
    }
    public HealthProfileReader(String sourceXmlFilePath) throws ParserConfigurationException, SAXException,
            IOException, JAXBException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        this.xmlPath = sourceXmlFilePath;
        this.doc = builder.parse(this.xmlPath);
        this.factory = XPathFactory.newInstance();
        this.xpath = this.factory.newXPath();

        JAXBContext jc = JAXBContext.newInstance(PeopleList.class);
        this.unmarshaller = jc.createUnmarshaller();
    }


    // Use xpath to implement methods like getWeight and getHeight
    // (getWeight(personID) returns weight of a given person, the same for getHeight)

    public String getWeight(String personID) throws XPathExpressionException {
        String searchExpr = "/people/person[@id='" + personID + "']";
        XPathExpression expr = this.xpath.compile(searchExpr);
        Object result = expr.evaluate(this.doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            NodeList weightNodes = e.getElementsByTagName("weight");
            return weightNodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return "";
    }
    public String getHeight(String personID) throws XPathExpressionException {
        String searchExpr = "/people/person[@id='" + personID + "']";
        XPathExpression expr = this.xpath.compile(searchExpr);
        Object result = expr.evaluate(this.doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            NodeList weightNodes = e.getElementsByTagName("height");
            return weightNodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return "";
    }


    private Person readPersonFromXmlElement(Element e) {
        String personId = e.getAttribute("id");

        NodeList firstnameNodes = e.getElementsByTagName("firstname");
        NodeList lastnameNodes = e.getElementsByTagName("lastname");
        NodeList birthdateNodes = e.getElementsByTagName("birthdate");
        NodeList weightNodes = e.getElementsByTagName("weight");
        NodeList heightNodes = e.getElementsByTagName("height");

        String firstname = firstnameNodes.item(0).getChildNodes().item(0).getNodeValue();
        String lastname = lastnameNodes.item(0).getChildNodes().item(0).getNodeValue();
        String birthdate = birthdateNodes.item(0).getChildNodes().item(0).getNodeValue();
        double weight = Double.parseDouble(weightNodes.item(0).getChildNodes().item(0).getNodeValue());
        double height = Double.parseDouble(heightNodes.item(0).getChildNodes().item(0).getNodeValue());

        return new Person(Long.parseLong(personId), firstname, lastname, birthdate, new HealthProfile(weight, height));
    }


    // A function that prints all people in the list with detail.

    public List<Person> listAllPeople() throws  XPathExpressionException {
        List<Person> results = new ArrayList<Person>();
        String searchExpr = "/people/person";
        XPathExpression expr = this.xpath.compile(searchExpr);
        Object result = expr.evaluate(this.doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            results.add(readPersonFromXmlElement(e));
        }
        return results;
    }


    // A function that accepts id as parameter
    // and prints the HealthProfile of the person with that id

    public HealthProfile getHealthProfileByPersonId(String personID) throws XPathExpressionException {
        String searchExpr = "/people/person[@id='" + personID + "']";
        XPathExpression expr = this.xpath.compile(searchExpr);
        Object result = expr.evaluate(this.doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            return readPersonFromXmlElement(e).getHealthProfile();
        }
        return new HealthProfile(0, 0);
    }


    // A function which accepts a weight and an operator (=, > , <) as parameters
    // and prints people that fulfill that condition (i.e., >80Kg, =75Kg, etc.).

    public List<Person> getPersonByWeight(double weight, String operator) throws XPathExpressionException {
        List<Person> results = new ArrayList<Person>();

        String searchExpr = "/people/person[healthprofile/weight" + operator + weight + "]";
        XPathExpression expr = this.xpath.compile(searchExpr);
        Object result = expr.evaluate(this.doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            results.add(readPersonFromXmlElement(e));
        }

        return results;
    }


    // Un-marshalling from XML using classes generated with JAXB XJC.

    public PeopleList unmarshal() throws JAXBException {
        return this.unmarshal(this.xmlPath);
    }
    public PeopleList unmarshal(String inputXmlFilePath) throws JAXBException {
        return (PeopleList) this.unmarshaller.unmarshal(new File(inputXmlFilePath));
    }
}
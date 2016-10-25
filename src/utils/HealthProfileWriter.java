package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import dao.PeopleList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.io.IOException;

public class HealthProfileWriter {

    private static String xmlPath;
    private static Marshaller marshaller;

    private static String jsonPath;
    private static JaxbAnnotationModule jacksonModule;
    private static ObjectMapper jacksonObjectMapper;


    // Class constructors

    public HealthProfileWriter() throws JAXBException {
        this("data/peopleOutput.xml", "data/peopleOutput.json");
    }
    public HealthProfileWriter(String outputXmlFilePath, String outputJsonFilePath) throws JAXBException {
        this.xmlPath = outputXmlFilePath;
        this.jsonPath = outputJsonFilePath;

        JAXBContext jc = JAXBContext.newInstance(PeopleList.class);
        this.marshaller = jc.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Adding the Jackson Module to process JAXB annotations
        this.jacksonModule = new JaxbAnnotationModule();
        // Jackson Object Mapper
        this.jacksonObjectMapper = new ObjectMapper();
        // Configure the mapper as necessary
        this.jacksonObjectMapper.registerModule(this.jacksonModule);
        this.jacksonObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.jacksonObjectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }


    // Marshalling to XML using classes generated with JAXB XJC.

    public void marshal(PeopleList peopleList) throws JAXBException {
        this.marshal(peopleList, this.xmlPath);
    }
    public void marshal(PeopleList peopleList, String xmlOutputFilePath) throws JAXBException {
        this.marshaller.marshal(peopleList, System.out);
        this.marshaller.marshal(peopleList, new File(xmlOutputFilePath));
    }


    // Marshalling to JSON.

    public void marshalJson(PeopleList peopleList) throws IOException {
        this.marshalJson(peopleList, this.jsonPath);
    }
    public void marshalJson(PeopleList peopleList, String jsonOutputFilePath) throws IOException {
        String result = this.jacksonObjectMapper.writeValueAsString(peopleList);
        System.out.println(result);
        this.jacksonObjectMapper.writeValue(new File(jsonOutputFilePath), peopleList);
    }
}
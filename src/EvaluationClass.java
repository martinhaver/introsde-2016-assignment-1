import utils.HealthProfileReader;
import utils.HealthProfileWriter;
import dao.PeopleList;
import model.HealthProfile;
import model.Person;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class EvaluationClass {

    private static void printPerson(Person p) {
        System.out.println();
        System.out.println("ID: " + p.getPersonId().toString());
        System.out.println("Full name: " + p.getFirstname() + " " + p.getLastname());
        System.out.println("Date of birth: " + p.getBirthdate());
        System.out.println("Health profile: " + p.getHealthProfile().toString());
    }

    public static void main(String[] args) throws ParserConfigurationException, JAXBException, SAXException, IOException, XPathExpressionException {
        HealthProfileReader reader = new HealthProfileReader("people/people.xml");
        HealthProfileWriter writer = new HealthProfileWriter("people/peopleOutput.xml", "people/peopleOutput.json");

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("1. List all people");
        List<Person> allPeople = reader.listAllPeople();
        for (Iterator<Person> i = allPeople.iterator(); i.hasNext();) {
            Person p = i.next();
            printPerson(p);
        }

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("2. Print person with id = 5");
        String personID = "05";
        HealthProfile hp = reader.getHealthProfileByPersonId(personID);
        System.out.println(hp.toString());

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("3. Print Health Profile of person with weight > 90");
        String operator = ">";
        double searchWeight  = 90;
        List<Person> peopleByWeight = reader.getPersonByWeight(searchWeight, operator);
        for (Iterator<Person> i = peopleByWeight.iterator(); i.hasNext();) {
            Person p = i.next();
            printPerson(p);
        }

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("4. marshaling to XML - create 3 persons using java and marshal them to XML");
        PeopleList peopleList = new PeopleList();
        peopleList.setData(peopleByWeight);
        writer.marshal(peopleList);

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("5. unmarshaling from XML");
        PeopleList peopleListFromXml = reader.unmarshal();
        List<Person> personsFromXml = peopleListFromXml.getData();
        for (Person p : personsFromXml) {
            printPerson(p);
        }

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("6. marshaling to JSON - create 3 persons using java and marshal them to JSON - print the content and save to .json file");
        writer.marshalJson(peopleList);
    }

}

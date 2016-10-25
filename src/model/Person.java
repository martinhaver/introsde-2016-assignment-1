package model;

import javax.xml.bind.annotation.*;
import java.util.Calendar;

// XmlRootElement defines the root element of the XML tree to which this class will be serialized
// --> <person> ... </person>
@XmlRootElement(name = "person")

// XmlType can optionally define the order in which the fields of person are written
@XmlType(propOrder = { "firstname", "lastname", "birthdate", "healthProfile" })

// XmlAccessorType indicates what to use to create the mapping: either FIELDS, PROPERTIES (i.e., getters/setters), PUBLIC_MEMBER or NONE (which means, you should indicate manually)
@XmlAccessorType(XmlAccessType.FIELD)

public class Person {

    private String firstname;
    private String lastname;
    private String birthdate;

    // XmlElement indicates the element to use for this field. Only used if the name in XML will be different than that in the class
    @XmlElement(name="healthprofile")
    private HealthProfile healthProfile;

    // XmlAttribute indicates that this field will be serialized as an attribute
    @XmlAttribute(name="id")
    private Long personId;


    // Getters and setters

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }
    public void setHhealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
    }

    public Long getPersonId() {
        return personId;
    }
    public void setPersonId(Long personId) {
        this.personId = personId;
    }


    // Class constructors

    public Person(Long personId, String firstname, String lastname, String birthdate, HealthProfile healthProfile) {
        this.setPersonId(personId);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthdate(birthdate);
        this.healthProfile = healthProfile;
    }
    public Person(Long personId, String firstname, String lastname, String birthdate) {
        this.setPersonId(personId);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthdate(birthdate);
        this.healthProfile = new HealthProfile();
    }
    public Person() {
        // Setting personId to a random number between 21 and 9999
        this.personId = Math.round(Math.floor(Math.random()*9978)+21);

        this.firstname = "John";
        this.lastname = "Doe";
        this.birthdate = this.getRandomDate();
        this.healthProfile = new HealthProfile();
    }


    // Random date generator function

    private String getRandomDate() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR); 		// 1. get the current year
        int year = (int) Math.round(Math.random()*(currentYear-1950)+1950); // 2. generate a random year
        //    between 1950 and currentYear
        int month = (int) Math.round(Math.floor(Math.random()*11)+1);		// 3. select a random month of the year
        // 4. select a random day in the selected month
        // 4.1 prepare a months array to store how many days in each month
        int[] months = new int[]{31,28,30,30,31,30,31,31,30,31,30,31};
        // 4.2 if it is a leap year, feb (months[1]) should be 29
        if ((currentYear % 4 == 0) && ((currentYear % 100 != 0) || (currentYear % 400 == 0))) {
            months[1] = 29;
        }
        long day = Math.round(Math.floor(Math.random()*(months[month-1]-1)+1));
        return ""+year+"-"+month+"-"+day;
    }
}
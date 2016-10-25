package dao;

import model.Person;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "people")
@XmlAccessorType(XmlAccessType.FIELD)
public class PeopleList {
    //@XmlElementWrapper(name="peopleList")
    @XmlElement(name = "person")
    private List<Person> data = new ArrayList<Person>();

    public PeopleList() {
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
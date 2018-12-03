package pt.ua.icm.studentmanagerv1;

import java.util.List;
import java.util.Map;

public class ObjectCourse {

    private String Abbreviation;
    private String ECTS;
    private Map<String, List<String>> Editions;
    private String ID;
    private String Name;

    public ObjectCourse() {
    }

    public ObjectCourse(String abbreviation, String ects, Map<String, List<String>>  editions, String id, String name) {
        this.Abbreviation = abbreviation;
        this.ECTS = ects;
        this.Editions = editions;
        this.ID = id;
        this.Name = name;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public String getECTS() {
        return ECTS;
    }

    public Map<String, List<String>>  getEditions() {
        return Editions;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }
}

package objects;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Course {

    private String Abbreviation;
    private String ECTS;
    private Map<String, List<String>> Editions;
    private String ID;
    private String Name;
    private String DocumentId;

    public Course() {
    }

    public Course(String abbreviation, String ects, Map<String, List<String>>  editions, String id, String name) {
        this.Abbreviation = abbreviation;
        this.ECTS = ects;
        this.Editions = editions;
        this.ID = id;
        this.Name = name;
    }

    public Course(String abbreviation, String ECTS, Map<String, List<String>> editions, String ID, String name, String documentId) {
        Abbreviation = abbreviation;
        this.ECTS = ECTS;
        Editions = editions;
        this.ID = ID;
        Name = name;
        DocumentId = documentId;
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

    public String getDocumentId() {
        return DocumentId;
    }

    public void setEmptyEditions() {
        Editions = Collections.emptyMap();
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}

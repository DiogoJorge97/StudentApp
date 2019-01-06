package objects;

import java.util.List;
import java.util.Map;

public class Degree {

    private String Abbreviation;
    private String ID;
    private String Name;
    private String Type;
    private Map<String, List<String>> ClassRepresentatives;
    private Map<String, List<String>> Directors;

    public Degree() {
    }

    public Degree(String abbreviation, String ID, String name, String type, Map<String, List<String>> classRepresentatives, Map<String, List<String>> directors) {
        Abbreviation = abbreviation;
        this.ID = ID;
        Name = name;
        Type = type;
        ClassRepresentatives = classRepresentatives;
        Directors = directors;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }

    public Map<String, List<String>> getClassRepresentatives() {
        return ClassRepresentatives;
    }

    public Map<String, List<String>> getDirectors() {
        return Directors;
    }

    @Override
    public String toString() {
        return "Degree{" +
                "Abbreviation='" + Abbreviation + '\'' +
                ", ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                ", ClassRepresentatives=" + ClassRepresentatives +
                ", Directors=" + Directors +
                '}';
    }
}

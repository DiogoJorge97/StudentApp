package pt.ua.icm.studentmanagerv1;

import java.util.List;

public class ObjectClasses {


    private String abreviation;
    private String name;
    private int ects; //TODO check if Int, and make sure it doesn't crash here
    private String academicYear;

    private List<String> semester;

    public ObjectClasses() {}


    public ObjectClasses(String abreviation, String name, int ects, String academicYear, List<String> semester) {
        this.abreviation = abreviation;
        this.name = name;
        this.ects = ects;
        this.academicYear = academicYear;
        this.semester = semester;
    }


    public String getAbreviation() {
        return abreviation;
    }

    public String getName() {
        return name;
    }

    public int getEcts() {
        return ects;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public List<String> getSemester() {
        return semester;
    }

    @Override
    public String toString() {
        return "ObjectClasses{" +
                "abreviation='" + abreviation + '\'' +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                ", academicYear='" + academicYear + '\'' +
                ", semester=" + semester +
                '}';
    }
}

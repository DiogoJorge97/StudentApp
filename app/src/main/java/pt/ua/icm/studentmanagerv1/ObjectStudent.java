package pt.ua.icm.studentmanagerv1;

import java.util.List;

public class ObjectStudent {

    private String Gmail;
    private String UaEmail;
    private String Nmec;
    private String Name;
    private List<String> Degrees;

    public ObjectStudent() {
    }

    public ObjectStudent(String gmail, String uaEmail, String nmec, String name, List<String> degrees) {
        this.Gmail = gmail;
        this.UaEmail = uaEmail;
        this.Nmec = nmec;
        this.Name = name;
        this.Degrees = degrees;
    }


    public String getGmail() {
        return Gmail;
    }

    public String getUaEmail() {
        return UaEmail;
    }

    public String getNmec() {
        return Nmec;
    }

    public String getName() {
        return Name;
    }

    public List<String> getDegrees() {
        return Degrees;
    }

    @Override
    public String toString() {
        return "ObjectStudent{" +
                "Gmail='" + Gmail + '\'' +
                ", UaEmail='" + UaEmail + '\'' +
                ", Nmec='" + Nmec + '\'' +
                ", Name='" + Name + '\'' +
                ", Degrees=" + Degrees +
                '}';
    }
}

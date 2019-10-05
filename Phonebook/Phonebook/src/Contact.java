import java.util.Objects;

// this class will store contacts in its instances
public class Contact {
    String name;
    String number;
    
    // creating the constructor for the contact class
    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    
    // getters and setters for the different fields...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

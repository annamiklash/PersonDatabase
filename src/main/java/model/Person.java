package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Person implements Comparable<Person> {

    private final String firstName;
    private final String lastName;
    private final Date birthDate;

    public Person(String firstName, String lastName, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    // assignment 8
    public void serialize(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(firstName);

            dataOutputStream.writeUTF(lastName);
            dataOutputStream.writeLong(birthDate.getTime());
            //DateUtil.serializeDate(birthDate, dataOutputStream);
            dataOutputStream.flush();

        } catch (IOException e) {
            System.out.println("Error while serializing from Person");
            e.printStackTrace();
        }

        // serialize birth date with getTime() method
        // encapsulate IOException in Assignment08Exception
    }

    public static Person deserialize(DataInputStream input) {
        try {

            final String firstName = input.readUTF();
            final String lastName = input.readUTF();
            final Date birthDate = new Date(input.readLong());

            return new Person(firstName, lastName, birthDate);
        } catch (IOException e) {
            System.out.println("Error while deserilizing to Person");
            e.printStackTrace();
        }
        return null;
    }

    public int compareTo(Person that) {
        return Comparator.comparing(Person::getLastName)
                .thenComparing(Person::getFirstName)
                .thenComparing(Person::getBirthDate)
                .compare(this, that);
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(birthDate, person.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate);
    }
}
package dao;

import comparator.BirthdayComparator;
import comparator.FirstNameComparator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import model.Person;
import util.InputParserUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public final class PersonDatabase {

    private final Comparator<Person> naturalOrderComparator;
    private final FirstNameComparator firstNameComparator;
    private final BirthdayComparator birthdayComparator;

    private final Map<Date, List<Person>> peopleByBirthDateMap;
    private final List<Person> personListForBornOnDate;
    private final List<Person> personListForSortByFirstName;
    private final List<Person> personListForSortByBirthDate;
    private final List<Person> personListForNaturalOrder;

    public PersonDatabase() {
        naturalOrderComparator = Comparator.naturalOrder();
        firstNameComparator = new FirstNameComparator();
        birthdayComparator = new BirthdayComparator();

        personListForNaturalOrder = InputParserUtil.parse(new File("testData/sortedBySurnameFirstNameAndBirthDateTestData.txt"));
        personListForBornOnDate = InputParserUtil.parse(new File("testData/bornOnDateTestData.txt"));
        personListForSortByFirstName = InputParserUtil.parse(new File("testData/sortByFirstNameTestData.txt"));
        personListForSortByBirthDate = InputParserUtil.parse(new File("testData/sortByBirthDateTestData.txt"));

        peopleByBirthDateMap = personListForBornOnDate.stream()
                .collect(Collectors.groupingBy(Person::getBirthDate));
    }

    public PersonDatabase(List<Person> personListForBornOnDate,
                          List<Person> personListForSortByFirstName,
                          List<Person> personListForSortByBirthDate,
                          List<Person> personListForNaturalOrder) {
        naturalOrderComparator = Comparator.naturalOrder();
        firstNameComparator = new FirstNameComparator();
        birthdayComparator = new BirthdayComparator();

        this.personListForNaturalOrder = personListForNaturalOrder;
        this.personListForBornOnDate = personListForBornOnDate;
        this.personListForSortByFirstName = personListForSortByFirstName;
        this.personListForSortByBirthDate = personListForSortByBirthDate;

        peopleByBirthDateMap = personListForBornOnDate.stream()
                .collect(Collectors.groupingBy(Person::getBirthDate));
    }


    public List<Person> sortedByFirstName() {
        return personListForSortByFirstName.stream()
                .sorted(firstNameComparator)
                .collect(Collectors.toList());
    }

    public List<Person> sortedBySurnameFirstNameAndBirthDate() { // natural order
        return personListForNaturalOrder.stream()
                .sorted(naturalOrderComparator)
                .collect(Collectors.toList());
    }

    public List<Person> sortedByBirthDate() {
        return personListForSortByBirthDate.stream()
                .sorted(birthdayComparator)
                .collect(Collectors.toList());
    }

    public Optional<List<Person>> bornOnDay(Date date) {
        return Optional.ofNullable(peopleByBirthDateMap.get(date));
    }

    public void serialize(Map<String, DataOutputStream> dataOutputStreamMap) {
        serializePersonListForBornOnDate(dataOutputStreamMap.get("data/outputPersonListForBornOnDate.txt"));
        serializePersonListForSortByFirstName(dataOutputStreamMap.get("data/outputPersonListForSortByFirstName.txt"));
        serializePersonListForNaturalOrder(dataOutputStreamMap.get("data/outputPersonListForNaturalOrder.txt"));
        serializePersonListForSortByBirthDate(dataOutputStreamMap.get("data/outputPersonListForSortByBirthDate.txt"));
    }

    public void serializePersonListForBornOnDate(DataOutputStream dataOutputStream) {
        serializeList(dataOutputStream, personListForBornOnDate);
    }

    public void serializePersonListForSortByFirstName(DataOutputStream dataOutputStream) {
        serializeList(dataOutputStream, personListForSortByFirstName);
    }

    public void serializePersonListForNaturalOrder(DataOutputStream dataOutputStream) {
        serializeList(dataOutputStream, personListForNaturalOrder);
    }

    public void serializePersonListForSortByBirthDate(DataOutputStream dataOutputStream) {
        serializeList(dataOutputStream, personListForSortByBirthDate);
    }


    private List<Person> serializeList(DataOutputStream dataOutputStream, List<Person> list) {
        try {
            dataOutputStream.writeInt(list.size());
            list.forEach(person ->
                    person.serialize(dataOutputStream));
            return list;
        } catch (IOException e) {
            System.out.println("Error while serializing from database");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    // assignment 8 - factory method based on deserialization

    public static PersonDatabase deserialize(Map<String, DataInputStream> dataInputStreamMap) {

        List<Person> deserializedPersonListForBornOnDate = deserializeList(dataInputStreamMap.get("data/outputPersonListForBornOnDate.txt"));
        List<Person> deserializedPersonListForSortByFirstName = deserializeList(dataInputStreamMap.get("data/outputPersonListForSortByFirstName.txt"));
        List<Person> deserializedPersonListForNaturalOrder = deserializeList(dataInputStreamMap.get("data/outputPersonListForNaturalOrder.txt"));
        List<Person> deserializedPersonListForSortByBirthDate = deserializeList(dataInputStreamMap.get("data/outputPersonListForSortByBirthDate.txt"));

        return new PersonDatabase(deserializedPersonListForBornOnDate, deserializedPersonListForSortByFirstName,
                deserializedPersonListForSortByBirthDate, deserializedPersonListForNaturalOrder);
    }


    public static List<Person> deserializeList(DataInputStream dataInputStream) {
        try {
            final int size = dataInputStream.readInt();
            List<Person> personList = new ArrayList<>();
            for (int i = 0; i < size && dataInputStream.available() > 0; i++) {
                Person person = Person.deserialize(dataInputStream);
                personList.add(person);
            }
            return personList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

}
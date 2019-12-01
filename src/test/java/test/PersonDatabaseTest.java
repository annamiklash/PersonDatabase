package test;

import dao.PersonDatabase;
import model.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DateUtil;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class PersonDatabaseTest {

    private final PersonDatabase db = new PersonDatabase();
    private Map<String, DataOutputStream> dataOutputStreamMap;
    private Map<String, DataInputStream> dataInputStreamMap;


    @Before
    public void initMapWithOutputStreams() {
        final List<String> keyList = Arrays.asList("data/outputPersonListForBornOnDate.txt", "data/outputPersonListForNaturalOrder.txt",
                "data/outputPersonListForSortByBirthDate.txt", "data/outputPersonListForSortByFirstName.txt");

        dataOutputStreamMap = keyList.stream()
                .collect(Collectors.toMap(s -> s, s -> {
                    try {
                        return new DataOutputStream(new FileOutputStream(s));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }));

    }

    @Before
    public void initMapWithInputStreams() {
        final List<String> keyList = Arrays.asList("data/outputPersonListForBornOnDate.txt", "data/outputPersonListForNaturalOrder.txt",
                "data/outputPersonListForSortByBirthDate.txt", "data/outputPersonListForSortByFirstName.txt");

        dataInputStreamMap = keyList.stream()
                .collect(Collectors.toMap(s -> s, s -> {
                    try {
                        return new DataInputStream(new FileInputStream(s));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }));

    }


    @Test
    public void sortedByFirstName() throws ParseException {
        final List<Person> expected = Arrays.asList(
                new Person("Daniel", "Brown", DateUtil.parse("1980-01-03")),
                new Person("Emma", "Ford", DateUtil.parse("1900-01-01")),
                new Person("Noah", "Smith", DateUtil.parse("1980-01-03")),
                new Person("Santiago", "White", DateUtil.parse("1599-10-15")),
                new Person("Sebastian", "Johnson", DateUtil.parse("2005-05-22")),
                new Person("Sofia", "Lee", DateUtil.parse("1759-12-30")));

        final List<Person> actual = db.sortedByFirstName();

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void sortedByBirthDate() throws ParseException {
        final List<Person> expected = Arrays.asList(
                new Person("Susan", "Brown", DateUtil.parse("1750-11-28")),
                new Person("Max", "Colt", DateUtil.parse("1800-03-25")),
                new Person("Richard", "Nixon", DateUtil.parse("1960-12-30")),
                new Person("Barack", "Obama", DateUtil.parse("1969-01-01")),
                new Person("Leslie", "Knope", DateUtil.parse("1984-08-13")),
                new Person("Michael", "Scott", DateUtil.parse("1994-05-19")),
                new Person("Anna", "Miklash", DateUtil.parse("1994-06-22")));

        final List<Person> actual = db.sortedByBirthDate();

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void sortedBySurnameFirstNameAndBirthDateTest() throws ParseException {
        final List<Person> expected = Arrays.asList(
                new Person("Mark", "Aligator", DateUtil.parse("1980-01-03")),
                new Person("Mark", "Aligator", DateUtil.parse("1985-01-03")),
                new Person("Walter", "Black", DateUtil.parse("1599-10-15")),
                new Person("Walter", "Black", DateUtil.parse("1699-10-15")),
                new Person("Mark", "Brown", DateUtil.parse("1980-01-03")),
                new Person("Henry", "Ford", DateUtil.parse("1900-01-01")),
                new Person("Ashley", "Johnson", DateUtil.parse("2005-05-22")),
                new Person("Ryan", "Lee", DateUtil.parse("1759-12-30")),
                new Person("John", "Smith", DateUtil.parse("1980-01-03")),
                new Person("John", "Smooth", DateUtil.parse("1940-01-03")),
                new Person("John", "Smooth", DateUtil.parse("1980-01-03")),
                new Person("Walter", "White", DateUtil.parse("1599-10-15")));

        final List<Person> actual = db.sortedBySurnameFirstNameAndBirthDate();

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);

    }

    @Test
    public void borOnSameDateTest() throws ParseException {
        final Date dateTested = DateUtil.parse("1980-01-03");

        final List<Person> personList = Arrays.asList(
                new Person("John", "Smith", DateUtil.parse("1980-01-03")),
                new Person("Mark", "Brown", DateUtil.parse("1980-01-03")),
                new Person("Daniel", "Craig", DateUtil.parse("1980-01-03")));

        final Optional<List<Person>> expected = Optional.of(personList);
        final Optional<List<Person>> actual = db.bornOnDay(dateTested);

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void testDB() throws IOException {
        db.serialize(dataOutputStreamMap);

        final PersonDatabase actual = PersonDatabase.deserialize(dataInputStreamMap);

        final int expectedFirstName = db.getPersonListForSortByFirstName().size();
        final int actualFirstName = actual.getPersonListForSortByFirstName().size();

        Assert.assertEquals(expectedFirstName, actualFirstName);
        Assert.assertEquals(db.getPersonListForSortByFirstName(), actual.getPersonListForSortByFirstName());
        Assert.assertNotSame(db.getPersonListForSortByFirstName(), actual.getPersonListForSortByFirstName());

        final int expectedBornOnDate = db.getPersonListForBornOnDate().size();
        final int actualBornOnDate = actual.getPersonListForBornOnDate().size();

        Assert.assertEquals(expectedBornOnDate, actualBornOnDate);
        Assert.assertEquals(db.getPersonListForBornOnDate(), actual.getPersonListForBornOnDate());
        Assert.assertNotSame(db.getPersonListForBornOnDate(), actual.getPersonListForBornOnDate());

        final int expectedNaturalOrder = db.getPersonListForNaturalOrder().size();
        final int actualNaturalOrder = actual.getPersonListForNaturalOrder().size();

        Assert.assertEquals(expectedNaturalOrder, actualNaturalOrder);
        Assert.assertEquals(db.getPersonListForNaturalOrder(), actual.getPersonListForNaturalOrder());
        Assert.assertNotSame(db.getPersonListForNaturalOrder(), actual.getPersonListForNaturalOrder());

        final int expectedBirthDate = db.getPersonListForSortByBirthDate().size();
        final int actualBirthDate = actual.getPersonListForSortByBirthDate().size();

        Assert.assertEquals(expectedBirthDate, actualBirthDate);
        Assert.assertEquals(db.getPersonListForSortByBirthDate(), actual.getPersonListForSortByBirthDate());
        Assert.assertNotSame(db.getPersonListForSortByBirthDate(), actual.getPersonListForSortByBirthDate());

    }

    @Test
    public void serializeDeserializePersonListForBornOnDateTest() throws IOException {
        final List<Person> expected = db.getPersonListForBornOnDate();
        final DataOutputStream dataOutputStream = dataOutputStreamMap.get("data/outputPersonListForBornOnDate.txt");
        db.serializePersonListForBornOnDate(dataOutputStream);
        dataOutputStream.close();


        InputStream inputStream = new FileInputStream("data/outputPersonListForBornOnDate.txt");
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        final List<Person> actual = PersonDatabase.deserializeList(dataInputStream);
        dataInputStream.close();


        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());

    }

    @Test
    public void serializeDeserializePersonForNaturalOrderTest() throws IOException {
        final List<Person> expected = db.getPersonListForNaturalOrder();
        final DataOutputStream dataOutputStream = dataOutputStreamMap.get("data/outputPersonListForNaturalOrder.txt");
        db.serializePersonListForNaturalOrder(dataOutputStream);
        dataOutputStream.close();

        final InputStream inputStream = new FileInputStream("data/outputPersonListForNaturalOrder.txt");
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        final List<Person> actual = PersonDatabase.deserializeList(dataInputStream);
        dataInputStream.close();


        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());

    }

    @Test
    public void serializeDeserializePersonListForSortByBirthDateTest() throws IOException {
        final List<Person> expected = db.getPersonListForSortByBirthDate();
        final DataOutputStream dataOutputStream = dataOutputStreamMap.get("data/outputPersonListForSortByBirthDate.txt");
        db.serializePersonListForSortByBirthDate(dataOutputStream);
        dataOutputStream.close();

        final InputStream inputStream = new FileInputStream("data/outputPersonListForSortByBirthDate.txt");
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        final List<Person> actual = PersonDatabase.deserializeList(dataInputStream);
        dataInputStream.close();


        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());

    }

    @Test
    public void serializeDeserializePersonListForSortByFirstNameTest() throws IOException {
        final List<Person> expected = db.getPersonListForSortByFirstName();
        final DataOutputStream dataOutputStream = dataOutputStreamMap.get("data/outputPersonListForSortByFirstName.txt");
        db.serializePersonListForSortByFirstName(dataOutputStream);
        dataOutputStream.close();

        final InputStream inputStream = new FileInputStream("data/outputPersonListForSortByFirstName.txt");
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        final List<Person> actual = PersonDatabase.deserializeList(dataInputStream);
        dataInputStream.close();


        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());

    }


}

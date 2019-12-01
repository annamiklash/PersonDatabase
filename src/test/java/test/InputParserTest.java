package test;

import org.junit.Assert;
import util.DateUtil;
import util.InputParserUtil;
import model.Person;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public final class InputParserTest {

    @Test
    public void parseTest() throws ParseException {
        final List<Person> expected = Arrays.asList(
                new Person("John", "Smith", DateUtil.parse("1980-01-03")),
                new Person("Mark", "Brown", DateUtil.parse("1989-02-02")),
                new Person("Walter", "White", DateUtil.parse("1599-10-15")),
                new Person("Ryan", "Lee", DateUtil.parse("1759-12-30")),
                new Person("Ashley", "Johnson", DateUtil.parse("2005-05-22")),
                new Person("Henry", "Ford", DateUtil.parse("1900-01-01")));

        final List<Person> actual = InputParserUtil.parse(new File("testData/parseTestData.txt"));

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);

    }
}

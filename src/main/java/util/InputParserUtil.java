package util;

import model.Person;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class InputParserUtil {

    // 1. Use regular expressions (Pattern) for validating input data

    // 2. Convert input string representing date using SimpleDateFormat "yyyy-MM-dd"
    //    SimpleDateFormat format "yyyy-MM-dd"

    public static List<Person> parse(File file) {

        final Optional<List<List<String>>> parsedDataListOptional = readData(file);
        return parsedDataListOptional
                .map(lists -> lists.stream()
                        .filter(InputParserUtil::containsThreeObjects)
                        .filter(InputParserUtil::isValid)// regex check
                        .map(InputParserUtil::buildPerson)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    private static Person buildPerson(List<String> strings) {
        final String firstName = strings.get(0);
        final String lastName = strings.get(1);
        final String stringBirthDate = strings.get(2);
        final Date date = convertBirthDate(stringBirthDate);
        return new Person(firstName, lastName, date);
    }

    private static Date convertBirthDate(String birthDate) {
        try {
            return DateUtil.parse(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private static boolean isValid(List<String> strings) {
        final String nameString = strings.get(0);
        final String lastNameString = strings.get(1);
        final String birthDateString = strings.get(2);

        final boolean isNameValid = isNameValid(nameString);
        final boolean isLastNameValid = isNameValid(lastNameString);
        final boolean isBirthDateValid = isDateValid(birthDateString);

        return isNameValid && isLastNameValid && isBirthDateValid;
    }

    private static boolean isDateValid(String birthDateString) {
        final String regx = RegexExpUtil.DATE_PATTERN;

        if (birthDateString.matches(regx)) {
            return true;
        } else {
            System.out.println(birthDateString + " didn't match a pattern. Should be YYYY-MM-DD");
            return false;
        }
    }

    private static boolean isNameValid(String name) {
        final String regx = RegexExpUtil.NAME_PATTERN;

        if (name.matches(regx)) {
            return true;
        } else {
            System.out.println(name + " didn't match a pattern.");
            return false;
        }
    }

    private static boolean containsThreeObjects(List<String> strings) {
        int itemCount = strings.size();
        return itemCount == 3;
    }

    private static Optional<List<List<String>>> readData(File file) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            final List<List<String>> parsedDataList = reader.lines()
                    //.filter(s -> s.matches(RegexExp.LINE_PATTERN))
                    .map(s -> s.split(" "))
                    .map(Arrays::asList)
                    .collect(Collectors.toList());
            return Optional.of(parsedDataList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
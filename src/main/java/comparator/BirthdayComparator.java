package comparator;

import model.Person;

import java.util.Comparator;

public class BirthdayComparator implements Comparator<Person> {

    @Override
    public int compare(Person o1, Person o2) {
        if (o1.getBirthDate().before(o2.getBirthDate())) {
            return -1;
        } else if (o1.getBirthDate().after(o2.getBirthDate())) {
            return 1;
        } else {
            return 0;
        }
    }
}

package seedu.address.model.person;

import java.util.function.Predicate;

public class FavouritePredicate implements Predicate<ReadOnlyPerson>{

    @Override
    public boolean test(ReadOnlyPerson readOnlyPerson) {
        boolean result = false;
        if (readOnlyPerson.getFavourite().toString().equals("true")) {
            result = true;
        }
        else if (readOnlyPerson.getFavourite().toString().equals("false")) {
            result = false;
        }
        return result;
    }
}

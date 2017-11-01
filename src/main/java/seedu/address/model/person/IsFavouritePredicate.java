package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests if a {@code ReadOnlyPerson}'s {@code Favourite} variable is true.
 */
public class IsFavouritePredicate implements Predicate<ReadOnlyPerson> {

    @Override
    public boolean test(ReadOnlyPerson person) {
        return person.getFavourite();
    }
}

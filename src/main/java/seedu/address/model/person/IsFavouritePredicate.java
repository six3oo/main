package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests if a {@code ReadOnlyPerson}'s {@code Favourite} variable is true.
 */
public class IsFavouritePredicate implements Predicate<ReadOnlyPerson> {
    private final boolean fave;

    public isFavouritePredicate(boolean fave) {
        this.fave = fave;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return fave;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IsFavouritePredicate // instanceof handles nulls
                && this.fave.equals(((IsFavouritePredicate) other).fave)); // state check
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Advent14 {

    private final int[]              inputSequence;
    private final int                lookahead;
    private final ArrayList<Integer> recipesAsList = new ArrayList<>(Arrays.asList(3, 7));
    private       int                elve1, elve2  = 1;

    Advent14(final int[] inputSequence, final int lookahead) {
        this.inputSequence = inputSequence.clone();
        this.lookahead = lookahead;
    }

    public static void main(final String[] args) {
        final Advent14 kitchen = new Advent14(new int[]{5, 9, 8, 7, 0, 1}, 10);

        int recipes = kitchen.getInputAsInteger();
        while (recipes-- > 0) kitchen.iterate();

        final List<Integer> scores = kitchen.findNext();

        System.out.println(Arrays.toString(scores.toArray()));

        final Advent14 kitchen2 = new Advent14(new int[]{5, 9, 8, 7, 0, 1}, 10);

        int subListIndex;
        do subListIndex = kitchen2.iterateAndCheck(); while (subListIndex < 0);

        System.out.println(subListIndex);
    }

    private int getInputAsInteger() {
        return Integer.valueOf(Arrays.stream(this.inputSequence).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString());
    }

    List<Integer> findNext() {
        final int inputAsInteger = this.getInputAsInteger();
        while (this.recipesAsList.size() < inputAsInteger + this.lookahead) this.iterate();
        return this.recipesAsList.subList(inputAsInteger, inputAsInteger + this.lookahead);
    }

    private int elveMove(final int elve) {
        return (elve + 1 + this.recipesAsList.get(elve)) % this.recipesAsList.size();
    }

    void iterate() {
        this.createRecipe();

        this.elve1 = this.elveMove(this.elve1);
        this.elve2 = this.elveMove(this.elve2);
    }

    int iterateAndCheck() {
        this.iterate();

        int index = -1;

        if (this.recipesAsList.size() > this.inputSequence.length) {
            final int[] recipesAsArray = this.recipesAsList.subList(this.recipesAsList.size() - this.inputSequence.length, this.recipesAsList.size())
                                                           .stream()
                                                           .mapToInt(i -> i)
                                                           .toArray();
            if (Arrays.equals(recipesAsArray, this.inputSequence)) index = this.recipesAsList.size() - this.inputSequence.length;
        }

        return index;
    }

    private void createRecipe() {
        this.recipesAsList.addAll(String.valueOf(this.recipesAsList.get(this.elve1) + this.recipesAsList.get(this.elve2))
                                        .chars()
                                        .map(Character::getNumericValue)
                                        .boxed()
                                        .collect(Collectors.toList()));
    }

    List<Integer> getRecipes() {
        return Collections.unmodifiableList(this.recipesAsList);
    }

    int getElve1() {
        return this.elve1;
    }

    void setElve1(final int elve1) {
        this.elve1 = elve1;
    }

    int getElve2() {
        return this.elve2;
    }

    void setElve2(final int elve2) {
        this.elve2 = elve2;
    }
}

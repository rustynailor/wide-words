package uk.co.rustynailor.widewords.enums;

/**
 * Created by russellhicks on 25/10/2016.
 */

public enum QuizQuestionResult {

    QUEUE ("QUEUE"),
    IN_PROGRESS ("IN_PROGRESS"),
    TIMED_OUT ("TIMED_OUT"),
    CORRECT ("CORRECT"),
    INCORRECT ("INCORRECT");


    private final String name;

    private QuizQuestionResult(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}

package uk.co.rustynailor.widewords.enums;

/**
 * Created by russellhicks on 25/10/2016.
 */

public enum QuizQuestionResult {

    QUEUE ("QUEUE"),
    CORRECT ("CORRECT"),
    INCORRECT ("INCORRECT");


    private final String name;

    QuizQuestionResult(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}

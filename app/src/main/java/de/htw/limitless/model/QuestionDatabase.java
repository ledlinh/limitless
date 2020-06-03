package de.htw.limitless.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionDatabase {
    private List<Question> mQuestionList = new ArrayList<>();
    private int mNextQuestionIndex;

    public QuestionDatabase() {
        this.mNextQuestionIndex = 0;
    }

    public void generateQuestions() {
        Question question01 = new MultipleChoicesQuestion("What is the capital of Turkey?");
        ((MultipleChoicesQuestion) question01).setUp("El Jadida", "Ankara", "Canberra", "New York", "The Alphabet starts with...?");
        ((MultipleChoicesQuestion) question01).setAnswerIndices(Arrays.asList(1));
        mQuestionList.add(question01);

        Question question02 = new MultipleChoicesQuestion("Which U.S. president does not/did not have a dog in the White House?");
        ((MultipleChoicesQuestion) question02).setUp("Trump", "Obama", "Bush", "Lincoln", "2017 was an important year");
        ((MultipleChoicesQuestion) question02).setAnswerIndices(Arrays.asList(0));
        mQuestionList.add(question02);

        Question question03 = new MultipleChoicesQuestion("8:4 as 10");
        ((MultipleChoicesQuestion) question03).setUp("101", "7", "5", "2", "When does 10 equals 1010?");
        ((MultipleChoicesQuestion) question03).setAnswerIndices(Arrays.asList(0,2));
        mQuestionList.add(question03);

        Question question04 = new MultipleChoicesQuestion("At what age did Alan Rickman die?");
        ((MultipleChoicesQuestion) question04).setUp("70", "69", "53", "38", "The one who loved Lilly Potter died quite young.");
        ((MultipleChoicesQuestion) question04).setAnswerIndices(Arrays.asList(0,3));
        mQuestionList.add(question04);

        Question question05 = new InputQuestion("What word is spelled incorrectly in every single dictionary?");
        ((InputQuestion) question05).setUp("incorrectly", "It is spelled incorrectly");
        mQuestionList.add(question05);

        Question question06 = new InputQuestion("What starts with \"e\" and ends with \"e\" but only has one letter in it?");
        ((InputQuestion) question06).setUp("envelope", "What do you use to send a letter?");
        mQuestionList.add(question06);

        Question question07 = new InputQuestion("101001101 is equal to...?");
        ((InputQuestion) question07).setUp("333", "There are different numbering systems.");
        mQuestionList.add(question07);

        Question question08 = new InputQuestion("What will you actually find at the end of every rainbow?");
        ((InputQuestion) question08).setUp("w", "As in blow, interview, straw, yellow... Get it?");
        mQuestionList.add(question08);

        Question question09 = new DeviceShakenQuestion("A baby in a crib is crying, please stop the crying");
        ((DeviceShakenQuestion) question09).setUp("A doll", "Baby food", "Sing a song", "Scream at the baby", "Do something with the crib!");

//        Question question10 = new DeviceShakenQuestion("How to make a 6 become a 9?");
//        ((DeviceShakenQuestion) question10).setUp("Rewrite it", "Plus 3", "Impossible", "No idea", "Does up-side-down and your phone have anything to do with each other? ");
//        ((DeviceShakenQuestion) question10).setAnswer("rotate 180deg");
//
//        Question question11 = new DeviceShakenQuestion("How to make the ball on the top of the screen move down without touching it?");
//        ((DeviceShakenQuestion) question11).setUp("Blow it", "Kick it", "Burn it", "Maybe in the up-side-down world", "what would you do if the ball is in a box?" );
//        ((DeviceShakenQuestion) question11).setAnswer("tilt vertically once");

        Collections.shuffle(mQuestionList);
    }

    public Question getNextQuestion() {
        Question question = mQuestionList.get(mNextQuestionIndex);
        mNextQuestionIndex++;
        return question;
    }
}

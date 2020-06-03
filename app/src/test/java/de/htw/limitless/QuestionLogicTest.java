package de.htw.limitless;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import de.htw.limitless.model.InputQuestion;
import de.htw.limitless.model.MultipleChoicesQuestion;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QuestionLogicTest {

    @Test
    public void multipleChoicesQuestion() {
        MultipleChoicesQuestion question = new MultipleChoicesQuestion("What's 1 + 4?");
        question.setUp("3", "4", "5", "101", "Sample hint");
        question.setAnswerIndices(Arrays.asList(2,3));
        List<Integer> inputIndices = Arrays.asList(0,2);
        boolean result = question.checkAnswer(inputIndices);

        Assert.assertEquals(false, result);
    }

    @Test
    public void smallerList_multipleChoicesQuestion() {
        MultipleChoicesQuestion question = new MultipleChoicesQuestion("What's 1 + 4?");
        question.setUp("3", "4", "5", "101", "Sample hint");
        question.setAnswerIndices(Arrays.asList(2,3));
        List<Integer> inputIndices = Arrays.asList(2);
        boolean result = question.checkAnswer(inputIndices);

        Assert.assertEquals(false, result);
    }

    @Test
    public void emptyInput_multipleChoicesQuestion() {
        MultipleChoicesQuestion question = new MultipleChoicesQuestion("What's 1 + 4?");
        question.setUp("3", "4", "5", "101", "Sample hint");
        question.setAnswerIndices(Arrays.asList(2,3));
        List<Integer> inputIndices = Arrays.asList();
        boolean result = question.checkAnswer(inputIndices);

        Assert.assertEquals(false, result);
    }

    @Test
    public void inputQuestionTest() {
        InputQuestion question = new InputQuestion("What's 1 + 4?");
        question.setUp("5", "Sample hint 2");

        boolean result = question.checkAnswer("5");

        Assert.assertEquals(true, result);
    }

    @Test
    public void emptyInput_inputQuestionTest() {
        InputQuestion question = new InputQuestion("What's 1 + 4?");
        question.setUp("5", "Sample hint 2");

        boolean result = question.checkAnswer(""); //null

        Assert.assertEquals(false, result);
    }

    /* TODO: Motion Questions */
}
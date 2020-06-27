import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import de.htw.limitless.model.DeviceMotionQuestion;
import de.htw.limitless.model.InputQuestion;
import de.htw.limitless.model.MultipleChoicesQuestion;
import de.htw.limitless.model.Question;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QuestionTest {
    @Test
    public void testInheritances() {
        Question inputQuestion = new InputQuestion("testI", "Question");
        Question multipleChoicesQuestion = new MultipleChoicesQuestion("testMC", "Question");
        Question motionQuestion = new DeviceMotionQuestion("testM", "Question");

        String inputType = inputQuestion.getQuestionType();
        String multipleChoicesType = multipleChoicesQuestion.getQuestionType();
        String motionQuestionType = motionQuestion.getQuestionType();

        Assert.assertEquals("input", inputType);
        Assert.assertEquals("multiple", multipleChoicesType);
        Assert.assertEquals("motion", motionQuestionType);

        ((InputQuestion) inputQuestion).setUp("Answer","Hint");
        Question newInputQuestion = inputQuestion;
        String answerToTest = ((InputQuestion) newInputQuestion).getAnswer();
        Assert.assertEquals("Answer", answerToTest);

        ((MultipleChoicesQuestion) multipleChoicesQuestion).setUp("Choice 1", "Choice 2", "Choice 3", "Choice 4", "Hint");
        Question newMultipleChoiceQuestion = multipleChoicesQuestion;
        List choicesToTest = ((MultipleChoicesQuestion) newMultipleChoiceQuestion).getChoicesList();
        Assert.assertEquals(((MultipleChoicesQuestion) multipleChoicesQuestion).getChoicesList(),((MultipleChoicesQuestion) newMultipleChoiceQuestion).getChoicesList()) ;

        ((DeviceMotionQuestion) motionQuestion).setRotatedAnswer();
        Question newMotionQuestion = motionQuestion;
        Assert.assertEquals("rotated 180 degree", ((DeviceMotionQuestion) newMotionQuestion).getAnswer());
    }
}
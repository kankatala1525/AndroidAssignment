package a.com.androidassigment;

import org.junit.Before;
import org.junit.Test;

import a.com.androidassigment.utils.Utils;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AssignmentTest {

    private Utils utils;
    @Before
    public  void setUp(){

        utils = new Utils();

    }
    @Test
    public void emailValidation() throws Exception {

        assertTrue(utils.isEmailValidate("abc@sbc.com"));

    }

    @Test
    public void emailValidationFalied() throws Exception {

        assertTrue(!utils.isEmailValidate("invalid"));

    }
}
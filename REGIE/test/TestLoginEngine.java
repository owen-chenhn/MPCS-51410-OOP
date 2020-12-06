package REGIE.test;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Assert;

import REGIE.main.*;

// Test class of LoginEngine
public class TestLoginEngine {
    @Test 
    public void loginTest() {
        // Login using correct id and password:
        User u1 = LoginEngine.login("133224", "abc12345");
        Assert.assertTrue((u1 != null));

        // uncorrect password:
        User u2 = LoginEngine.login("133224", "2222");
        Assert.assertTrue((u2 == null));

        // unknown id:
        User u3 = LoginEngine.login("i766224", "2222");
        Assert.assertTrue((u3 == null));
    }
}

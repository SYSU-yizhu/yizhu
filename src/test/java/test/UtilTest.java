package test;

import com.sysu.yizhu.util.LCUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class UtilTest extends TestBase {

    @Autowired
    private LCUtil lcUtil;

    @Test
    public void test() throws Exception {
        Assert.assertTrue(lcUtil.checkObjectId("k2DyygK4RP8nquWmNl0wD7qXhJ44vJWK"));
        Assert.assertFalse(lcUtil.checkObjectId("asdasdasdasd"));

        Assert.assertTrue(lcUtil.putLocation("k2DyygK4RP8nquWmNl0wD7qXhJ44vJWK", 0.5, 7.5));
        Assert.assertTrue(lcUtil.pushSOS(0.0, 8.0));
    }
}

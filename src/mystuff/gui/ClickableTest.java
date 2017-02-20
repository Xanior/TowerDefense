package mystuff.gui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xanior on 2016.11.28..
 */
public class ClickableTest {
    Clickable cl;

    @Test(expected = RuntimeException.class)
    public void click() throws Exception {
        cl = new Clickable("img/buttons/Button01.png", "function", 0.5f, 0.5f, 100, 100);
        Assert.assertTrue(cl.click(640,360));
    }

    @Test(expected = RuntimeException.class)
    public void getFunction() throws Exception {
        cl = new Clickable("img/buttons/Button01.png", "function", 0.5f, 0.5f, 100, 100);
        Assert.assertEquals(cl.getFunction(), "function");
    }

    @Test(expected = RuntimeException.class)
    public void disable() throws Exception {
        cl = new Clickable("img/buttons/Button01.png", "function", 0.5f, 0.5f, 100, 100);
        cl.disable();
        Assert.assertFalse(cl.click(640,360));
    }

    @Test(expected = RuntimeException.class)
    public void enable() throws Exception {
        cl = new Clickable("img/buttons/Button01.png", "function", 0.5f, 0.5f, 100, 100);
        cl.disable();
        cl.enable();
        Assert.assertTrue(cl.click(640,360));
    }

}
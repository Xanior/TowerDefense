package mystuff.gui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xanior on 2016.11.28..
 */
public class TextTest {
    private Text text;
    @Before
    public void setUp() {
        text = new Text("Sajt", 150, 310, "justtext");
    }

    @Test
    public void click() throws Exception {
        Assert.assertTrue(text.click(140, 311));
        Assert.assertTrue(text.click(160, 325));
    }

    @Test
    public void getText() throws Exception {
        Assert.assertEquals(text.getText(), "Sajt");
    }

    @Test
    public void getType() throws Exception {
        Assert.assertEquals(text.getType(), "justtext");
    }

    @Test
    public void getX() throws Exception {
        Assert.assertEquals(text.getX(), 150);
    }

    @Test
    public void getY() throws Exception {
        Assert.assertEquals(text.getY(), 310);
    }

}
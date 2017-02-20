package mystuff.gamelogic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.AppGameContainer;

import static org.junit.Assert.*;

/**
 * Created by Xanior on 2016.11.28..
 */
public class GameManagerTest {
    public Program pr;
    public GameManager gm;

    @Before
    public void setUp() {
        pr = new Program("Test");
    }

    @Test(expected = NullPointerException.class)
    public void getMyMap() throws Exception {
        pr.gm.getMyMap();
    }

    @Test(expected = NullPointerException.class)
    public void getMyMapID() throws Exception {
        Assert.assertEquals(pr.gm.getMyMapID(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void setMyMapID() throws Exception {
        pr.gm.setMyMapID(1);
        Assert.assertEquals(1, gm.getMyMapID());
    }
}
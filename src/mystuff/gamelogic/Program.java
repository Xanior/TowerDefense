package mystuff.gamelogic;

import org.newdawn.slick.*;

/**
 * Created by Xanior on 2016.10.11..
 */
public class Program extends BasicGame {

    public GameManager gm;
    public Program(String title) {
        super(title);
    }

    /**Slick2D setup
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Program("Tower Defense GHA"));
            app.setDisplayMode(640, 480, false);
            app.setUpdateOnlyWhenVisible(false);
            app.setAlwaysRender(true);
            app.setIcon("res/enemy2.png");
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    /**
     * Slick2D settings
     * @param gameContainer Slick2D Gamecontainer
     * @throws SlickException Slick2D Exception
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        gameContainer.setShowFPS(true);
        gameContainer.setVSync(true);
        gameContainer.setTargetFrameRate(60);
        gm = new GameManager(gameContainer);
    }

    /**
     * Slick2D update calls the GameManagers update function
     * @param gameContainer Slick2D param
     * @param delta elapsed time since the last update in millisecs
     * @throws SlickException Slick2D Exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        gm.update(delta);
    }


    /**
     * Slick2D render function
     * @param gameContainer Slick2D GameContainer
     * @param graphics Slick2D Graphics Class
     * @throws SlickException Slick2D Exception
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        gm.render(graphics);
    }
}

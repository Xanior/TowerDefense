package mystuff.gamelogic;

import mystuff.gui.GUIManager;
import mystuff.networking.Client;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import phii.gha.gamelogic.*;

import java.util.ArrayList;

enum state{InMenu, InGame, InLogin, InOpponentSelect, InRequestReceived, InWon, InLost}


/**
 * Created by Xanior on 2016.10.11..
 */
public class GameManager {

    private GameContainer gc;
    private GUIManager gui;
    private Image backGround;
    private Music music;
    private state gameState;
    private Client c;
    private GameLogic gameLogic;
    private int myMapID = 0; //which map is mine

    /**
     * Creates a GameManager Object, saves the GameContainer and runs the init() function.
     * @param gc the Slick2D GameContainer object to be saved.
     */
    public GameManager(GameContainer gc) {
        this.gc = gc;
        init();
    }

    /**
     * Initializes the GameManager class.
     */
    public void init() {

        gui = new GUIManager(this, gc);
        try {
            backGround = new Image("img/sandBG.jpg");
        } catch (SlickException e) {
            e.printStackTrace();
        }
        try {
            music = new Music("res/music.ogg");
            music.loop();
            music.play();
        } catch (SlickException e) {
            e.printStackTrace();
        }

        goInMainMenu();
    }

    /**
     * Updates the game and the gui.
     *
     * @param deltaTime the time since the last update in millisecs.
     */
    public void update(int deltaTime) {
        if(!music.playing()) {
            music.play();
        }
        Input input = gc.getInput();
        input.addKeyListener(gui);
        if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
            //Click handling
            gui.click(input.getMouseX(), input.getMouseY());
        }
        if(c != null && c.receivedRequest() && !isInGame()) {
            goInRequestReceived(c.getOpponent());
        }
        if(isInGame()) {
            gameLogic.update(deltaTime);
        }
    }

    /**
     * Renders the window.
     *
     * @param g a grapics class, defined in Slick2D.
     */
    public void render(Graphics g) {
        backGround.draw(0.0F, 0.0F, gc.getWidth(), gc.getHeight());
        if(isInGame()) {
            g.drawLine(gc.getWidth()/(float)2, 0.0f, gc.getWidth()/(float)2, (float)gc.getHeight());

        }
        gui.drawAll(gc.getWidth(), gc.getHeight(), g);
    }

    /**
     * Build a Tower at the specified x y coordinates
     *
     * @param x x coordinate in pixels
     * @param y y coordinate in pixels
     */
    public void buildTower(int x, int y) {
        float pX = (float)x/gc.getWidth();
        if(pX <= 0.5 && myMapID == 1 || pX >= 0.5 && myMapID == 2) {
            pX = pX * 2.0f - (float)(myMapID - 1);
            float pY = (y - 50f) / (gc.getHeight() - 50f);

            Point p = new Point(pX, pY);
            getMyMap().buildTower(1, p);
            System.out.println("building tower at: " + pX + " " + pY);
            c.sendBuildTower(1, pX, pY);
        }
    }

    /**
     * Upgrades the Tower with the ID id
     *
     * @param id the ID of the tower
     */
    public void upgradeTower(int id) {
        getMyMap().upgrade(id);
        c.sendUpgradeTower(id);
    }


    /**
     * Returns the map of the player
     *
     * @return The map of the player
     */
    public Map getMyMap() {
        if(myMapID == 1) {
            return gameLogic.getMap1();
        } else {
            return gameLogic.getMap2();
        }
    }

    /**
     * Returns the ID of the players map
     *
     * @return the ID of the players map
     */
    public int getMyMapID() {
        return myMapID;
    }

    /**
     * Sets the players map (1 or 2)
     * @param id the ID of the map to be set (1 or 2)
     */
    public void setMyMapID(int id) {
        myMapID = id;
    }

    /**
     * Get the GameLogic object
     * @return the GameLogic Object
     */
    public GameLogic getGameLogic() {
        return gameLogic;
    }

    /**
     * Is the user in the main menu?
     * @return is the user in the main menu
     */
    public boolean isInMainMenu() { return gameState == state.InMenu; }

    /**
     * Is the user ingame?
     * @return is the user ingame
     */
    public boolean isInGame() { return gameState == state.InGame; }

    /**
     * Is the user on the login screen?
     * @return is the user on the login screen
     */
    public boolean isInLogin() { return gameState == state.InLogin; }

    /**
     * Open the main menu, and set it up.
     */
    public void goInMainMenu() {
        gameState = state.InMenu;
        gui.clearButtons();
        gui.addButton("img/Buttons/PlayButton.png", "goinlogin", 0.5F, 0.1F, 300, 80);
        gui.addButton("img/Buttons/OptionsButton.png", "goinsettings", 0.5F, 0.22F, 300, 80);
        gui.addButton("img/Buttons/MoreButton.png", "goincredits", 0.5F, 0.34F, 300, 80);
        gui.addButton("img/Buttons/QuitButton.png", "exit", 0.5F, 0.46F, 300, 80);
    }

    /**
     * Go to the Winner screen.
     */
    public void goInWon() {
        gameState = state.InWon;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "goinmainmenu", 0.05F, 0.05F, 40, 40);
        gui.addText("Gratz", 100, 250, "justtext");

    }

    /**
     * Go to the Loser screen.
     */
    public void goInLost() {
        gameState = state.InLost;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "goinmainmenu", 0.05F, 0.05F, 40, 40);
        gui.addText("Noob", 100, 250, "justtext");
    }

    /**
     * Open the login screen.
     */
    public void goInLogin() {
        gameState = state.InLogin;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "exitplay", 0.05F, 0.05F, 40, 40);
        gui.addButton("img/Buttons/PlayButton.png", "sendlogin", 0.5F, 0.1F, 300, 80);
        //c = new Client("192.168.137.1", 12001, this);
        //c = new Client("152.66.182.176", 12001, this);
        c = new Client("ip.config", this);

        c.start();
    }

    /**
     * Open the opponent select screen.
     */
    public void goInOpponentSelect() {
        gameState = state.InOpponentSelect;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "exitplay", 0.05F, 0.05F, 40, 40);
        ArrayList<String> users = c.listUsers();
        users.add("RANDOM");
        int offset = 0;
        for (String user : users) {
            gui.addText(user, gc.getWidth() / 2, 150 + offset, "user");
            offset += 35;
        }
    }

    /**
     * Open the request received screen (Accept / Deny)
     * @param opponent the name of the opponent
     */
    public void goInRequestReceived(String opponent) {
        gameState = state.InRequestReceived;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "exitplay", 0.05F, 0.05F, 40, 40);
        gui.addButton("img/Buttons/PlayButton.png", "accept", 0.5F, 0.1F, 300, 80);
        gui.addButton("img/Buttons/DenyButton.png", "deny", 0.5F, 0.22F, 300, 80);
        gui.addText(opponent, gc.getWidth()/2, 400, "justtext");
    }

    /**
     * Start the game.
     */
    public void goInGame() {
        Player p1, p2;
        if(myMapID == 1) {
            p1 = new Player(gui.username, 0, 0);
            p2 = new Player(c.getOpponent(), 0, 0);
        } else {
            p1 = new Player(c.getOpponent(), 0, 0);
            p2 = new Player(gui.username, 0, 0);
        }
        gameLogic = new GameLogic(p1, p2);

        gameState = state.InGame;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "exitplay", 0.05F, 0.05F, 40, 40);
        gui.addText(gui.username + "(You)", gc.getWidth()/4 + (myMapID - 1) * gc.getWidth()/2, 5, "justtext");
        if(myMapID == 1) {
            gui.addText(c.getOpponent(), gc.getWidth()/4 + (2 - 1) * gc.getWidth()/2, 5, "justtext");
        } else {
            gui.addText(c.getOpponent(), gc.getWidth()/4 + (1 - 1) * gc.getWidth()/2, 5, "justtext");

        }

    }

    /**
     * Open the settings menu.
     */
    public void goInSettings() {
        gameState = state.InMenu;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "goinmainmenu", 0.05F, 0.05F, 40, 40);

        if(gc.isMusicOn()) {
            gui.addButton("img/Buttons/MusicButtonOn.png", "musicOff", 0.5F, 0.34F, 300, 80);
        } else {
            gui.addButton("img/Buttons/MusicButtonOff.png", "musicOn", 0.5F, 0.34F, 300, 80);
        }

        if(gc.isSoundOn()) {
            gui.addButton("img/Buttons/SoundButtonOn.png", "soundOff", 0.5F, 0.46F, 300, 80);
        } else {
            gui.addButton("img/Buttons/SoundButtonOff.png", "soundOn", 0.5F, 0.46F, 300, 80);
        }
    }

    /**
     * Open the credits(more) menu.
     */
    public void goInCredits() {
        gameState = state.InMenu;
        gui.clearButtons();
        gui.addButton("img/Buttons/Menubutton.png", "goinmainmenu", 0.05F, 0.05F, 40, 40);
        gui.addText("Special thanks to Kenney & Zámbó Jimmy", 100, 250, "justtext");
    }

    /**
     * Returns the active Client object.
     * @return the active Client object
     */
    public Client getClient() {return c;}

    /**
     * Closes the TCP-socket and stops the Client thread.
     */
    public void closeSocket() {
        try {
            c.stopThread();
            c.cleanup();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shuts down the program, also closes the TCP-socket.
     */
    public void shutDown() {
        closeSocket();
        System.exit(0);
    }
}

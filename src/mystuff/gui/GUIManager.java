package mystuff.gui;

import mystuff.gamelogic.GameManager;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import phii.gha.gamelogic.*;

import java.awt.Font;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Xanior on 2016.10.11..
 */
public class GUIManager implements KeyListener {

    private GameManager gm;
    private GameContainer gc;
    private CopyOnWriteArrayList<Clickable> clickables = new CopyOnWriteArrayList<>(); //Permanent buttons
    private CopyOnWriteArrayList<Clickable> tempButtons = new CopyOnWriteArrayList<>(); //Temporary buttons
    private CopyOnWriteArrayList<Text> texts = new CopyOnWriteArrayList<>();
    private Image[] TowerImages = new Image[3];
    private Image[] EnemyImages = new Image[4];
    private Image[] MissileImages = new Image[2];
    private TrueTypeFont ttf;
    private TrueTypeFont ttfsmall;
    public String username = "xanior"; //the username input field on the login screen
    private int lastClickX = 0, lastClickY = 0; //Used for the temporary buttons, remembers the position
    private float sixteenPixelMapped01X = 0.0f;
    private float sixteenPixelMapped01Y = 0.0f;
    private int towerID = 0;

    /**
     * Creates a GUI
     * @param gm Reference to the active GameManager
     * @param gc Reference to the active GameContainer
     */
    public GUIManager(GameManager gm, GameContainer gc) {
        this.gm = gm;
        this.gc = gc;
        try {
            TowerImages[0] = new Image("res/tower0.png");
            TowerImages[1] = new Image("res/tower1.png");
            TowerImages[2] = new Image("res/tower2.png");
            EnemyImages[0] = new Image("res/enemy0.png");
            EnemyImages[1] = new Image("res/enemy1.png");
            EnemyImages[2] = new Image("res/enemy2.png");
            EnemyImages[3] = new Image("res/enemy3.png");
            MissileImages[0] = new Image("res/missile0.png");
            MissileImages[1] = new Image("res/missile1.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
        Font font = new Font("Tahoma", Font.BOLD, 20);
        ttf = new TrueTypeFont(font, true);
        Font fontsmall = new Font("Tahoma", Font.BOLD, 10);
        ttfsmall = new TrueTypeFont(font, true);
        gc.setDefaultFont(ttf);
    }

    /**
     * draw all active buttons
     * @param width Width of the window
     * @param height Height of the window
     * @param g Slick2D graphics class
     */
    public void drawAll(int width, int height, Graphics g) {
        sixteenPixelMapped01X = 16 / (float)gc.getWidth();
        sixteenPixelMapped01Y = 16 / (float)gc.getHeight();
        for(Clickable c : clickables) {
            c.draw(width, height);
        }

        for(Clickable c : tempButtons) {
            c.draw(width, height);
        }

        if(gm.isInGame()) {
            for (Enemy e : gm.getGameLogic().getMap1().getEnemies()) {
                drawEnemy(e, 0);
            }
            for (Enemy e : gm.getGameLogic().getMap2().getEnemies()) {
                drawEnemy(e, 1);
            }
            for (Tower t : gm.getGameLogic().getMap1().getTowers()) {
                drawTower(t, 0);
            }
            for (Tower t : gm.getGameLogic().getMap2().getTowers()) {
                drawTower(t, 1);
            }
            for (Missile m : gm.getGameLogic().getMap1().getMissiles()) {
                drawMissile(m, 0);
            }
            for (Missile m : gm.getGameLogic().getMap2().getMissiles()) {
                drawMissile(m, 1);
            }
        }
        for (Text t : texts) {
            ttf.drawString(t.getX(), t.getY(), t.getText());
        }
        if(gm.isInLogin()) {
            ttf.drawString(width/2, height/2, "Username: " + username);
        }

        if(gm.isInGame()) {
            ttf.drawString(60 + (gm.getMyMapID() - 1) * gc.getWidth()/2, 0, "C: " + gm.getMyMap().getPlayer().getCreditString());
            ttf.drawString(60 + (gm.getMyMapID() - 1) * gc.getWidth()/2, 20, "H: " + gm.getMyMap().getPlayer().getHealthString());
        }
    }

    /**
     * Draws a Tower
     * @param t Reference to the Tower
     * @param side which side its on (1:Map1 or 2:Map2)
     */
    private void drawTower(Tower t, int side) {
        Point loc = t.getLocation();
        float X = ((loc.getX() - sixteenPixelMapped01X) * 0.5f + side * 0.5f) * gc.getWidth();
        float Y = (loc.getY() - sixteenPixelMapped01Y) * gc.getHeight();
        Y *= (gc.getHeight() - 50f) / gc.getHeight();
        Y += 50f;
        TowerImages[t.getType() - 1].draw(X, Y);
    }

    /**
     * Draws an Enemy
     * @param e Reference to the Enemy
     * @param side which side its on (1:Map1 or 2:Map2)
     */
    private void drawEnemy(Enemy e, int side) { //Side = 0 or 1
        Point loc = e.getLocation();
        float X = ((loc.getX() - sixteenPixelMapped01X) * 0.5f + side * 0.5f) * gc.getWidth();
        float Y = (loc.getY() - sixteenPixelMapped01Y) * gc.getHeight();
        Y *= (gc.getHeight() - 50f) / gc.getHeight();
        Y += 50f;
        EnemyImages[e.getType() - 1].draw(X, Y);
    }

    /**
     * Draws a Missile
     * @param m Reference to the Missile
     * @param side which side its on (1:Map1 or 2:Map2)
     */
    private void drawMissile(Missile m, int side) { //Side = 0 or 1
        Point loc = m.getLocation();
        float X = ((loc.getX() - sixteenPixelMapped01X) * 0.5f + side * 0.5f) * gc.getWidth();
        float Y = (loc.getY() - sixteenPixelMapped01Y) * gc.getHeight();
        Y *= (gc.getHeight() - 50f) / gc.getHeight();
        Y += 50f;
        MissileImages[m.getType() - 1].draw(X, Y);
    }

    /**
     * select which button was pressed, and what to do, returns true if a button was pressed
     * @param mouseX the x position of the mouse in pixels
     * @param mouseY the y position of the mouse in pixels
     * @return true if a button was pressed, false otherwise
     */
    public boolean click(int mouseX, int mouseY) {
        String function;
        for(Text t : texts) {
            if(t.click(mouseX, mouseY)) {
                if(t.getType().equals("user")) {
                    gm.getClient().sendOpponentRequest(t.getText());
                    return true;
                }
            }
        }

        for(Clickable c : tempButtons) {
            if(c.click(mouseX, mouseY)) {
                function = c.getFunction();
                switch (function) {
                    case "buildtower" :
                        gm.buildTower(lastClickX, lastClickY);
                        break;
                    case "upgradetower" :
                        gm.upgradeTower(towerID);
                        break;
                }
                tempButtons.clear();
                return true;
            }
        }

        for(Clickable c : clickables) {
            if(c.click(mouseX, mouseY)) { //Check all buttons
                function = c.getFunction();
                switch (function) {
                    case "goinmainmenu" : //Go to the Main Menu
                        gm.goInMainMenu();
                        break;
                    case "goinlogin" : //Go to the Login Screen
                        gm.goInLogin();
                        break;
                    case "sendlogin" : //Send the username to the server
                        gm.getClient().login(username);
                        break;
                    case "goinopponentselect" : //Go to the opponent selection screen
                        gm.goInOpponentSelect();
                        break;
                    case "goingame" : //Start the game
                        gm.goInGame();
                        break;
                    case "goinsettings" : //Go to the settings window
                        gm.goInSettings();
                        break;
                    case "goincredits" : //Go to the credits (more) screen
                        gm.goInCredits();
                        break;
                    case "accept" : //Send an accept command to the server
                        gm.getClient().acceptGame();
                        break;
                    case "deny" : //Sebd a deny command to the server
                        gm.getClient().sendMsg("DENY");
                    case "exitplay" : //Exit the current screen and stop the TCP connection
                        gm.closeSocket();
                        gm.goInMainMenu();
                        break;
                    case "soundOn" : //Turn the sound on
                        gc.setSoundOn(true);
                        //gc.setSoundVolume(0.2f);
                        gm.goInSettings();
                        break;
                    case "soundOff" : //Turn the sound off
                        gc.setSoundOn(false);
                        gm.goInSettings();
                        break;
                    case "musicOn" : //Turn the music on
                        gc.setMusicOn(true);
                        //gc.setMusicVolume(0.2f);
                        gm.goInSettings();
                        break;
                    case "musicOff" : //Turn the music off
                        gc.setMusicOn(false);
                        gm.goInSettings();
                        break;
                    case "exit" : //Exit the whole application
                        gm.shutDown();
                        break;
                }
                return true; //If one button was pressed, we shall not check the others (nullptr cuz the button might be removed by now)
            }
        }

        //tower upgrade
        //Translating the input [0,1]
        if(gm.isInGame()) {
            float pX = (float)mouseX/gc.getWidth();
            float pY = 0f;
            if(pX <= 0.5 && gm.getMyMapID() == 1 || pX >= 0.5 && gm.getMyMapID() == 2) {
                pX = pX * 2.0f - (float)(gm.getMyMapID() - 1);
                pY = (mouseY - 50f) / (gc.getHeight() - 50f);
            }

            float offset = 0.05f;
            if(lastClickX < gc.getWidth()/2) {
                offset = -0.05f;
            }

            for(Tower t : gm.getMyMap().getTowers()) {
                Point loc = t.getLocation();
                float tX = loc.getX();
                float tY = loc.getY();
                if(tX - pX < 2*sixteenPixelMapped01X && pX - tX < 2*sixteenPixelMapped01X) {
                    if(tY - pY < sixteenPixelMapped01Y && pY - tY < sixteenPixelMapped01Y) {
                        tempButtons.clear();
                        towerID = t.getID();
                        addTempButton("img/Buttons/Button05.png", "upgradetower", (float)mouseX/gc.getWidth() - offset, (float)mouseY/gc.getHeight(), 80, 40);
                        return true;
                    }
                }
            }

            if (tempButtons.size() == 0 && mouseY > 50) {
                //Add temp buttons
                lastClickX = mouseX;
                lastClickY = mouseY;
                tempButtons.clear();
                addTempButton("img/Buttons/BuildTower.png", "buildtower", (float)mouseX/gc.getWidth() - offset, (float)mouseY/gc.getHeight(), 80, 40);
                return true;
            }
        }

        tempButtons.clear(); //clear all temporary buttons, if the user clicked on anything thats not a button
        return false;
    }

    /**
     * Creates a new GUI button
     * @param imgPath The image of the button
     * @param function The function name, used to check which button was pressed and what to do
     * @param centerX X coordinate [0,1]
     * @param centerY Y coordinate [0,1]
     * @param sizeX width of the button in pixels
     * @param sizeY height of the button in pixels
     */
    public void addButton(String imgPath, String function, float centerX, float centerY, int sizeX, int sizeY) {
        Clickable button = new Clickable(imgPath, function, centerX, centerY, sizeX, sizeY);
        clickables.add(button);
    }

    /**
     * Adds a temporary button, that only last until the next click (either clicked or not)
     * @param imgPath The image of the button
     * @param function The function name, used to check which button was pressed and what to do
     * @param centerX X coordinate [0,1]
     * @param centerY Y coordinate [0,1]
     * @param sizeX width of the button in pixels
     * @param sizeY height of the button in pixels
     */
    public void addTempButton(String imgPath, String function, float centerX, float centerY, int sizeX, int sizeY) {
        Clickable button = new Clickable(imgPath, function, centerX, centerY, sizeX, sizeY);
        tempButtons.add(button);
    }

    /**
     * Adds a text to the Gui
     * @param text The text
     * @param x x coordinat in pixels
     * @param y y coordinate in pixels
     * @param type A function for the string when its clicked, "justtext" as default
     */
    public void addText(String text, int x, int y, String type) {
        texts.add(new Text(text, x, y, type));
    }

    /**
     * Clears all Guielements
     */
    public void clearButtons() {
        clickables.clear();
        tempButtons.clear();
        texts.clear();
    }

    /**
     * Keypress callback function
     * @param i keycode as int
     * @param c keycode as ascii char
     */
    @Override
    public void keyPressed(int i, char c) {
        if(gm.isInLogin()) {
            if(i == Input.KEY_BACK) {
                if (username.length() > 1) {
                    username = username.substring(0, username.length() - 1);
                } else {
                    username = "";
                }
            } else if(i == Input.KEY_ENTER) {
                gm.getClient().login(username);
            } else {
                username += c;
            }
        }
    }


    /**
     * Slick2D keyboard listener function, not used
     * @param i keycode as int
     * @param c keycode as ascii char
     */
    @Override
    public void keyReleased(int i, char c) {
    }


    /**
     * Slick2D keyboard listener function, not used
     * @param input Slick2D input class
     */
    @Override
    public void setInput(Input input) {

    }

    /**
     * Slick2D keyboard listener function
     * @return is it enabled
     */
    @Override
    public boolean isAcceptingInput() {
        return true;
    }


    /**
     * Slick2D keyboard listener function, not used
     */
    @Override
    public void inputEnded() {

    }

    /**
     * Slick2D keyboard listener function, not used
     */
    @Override
    public void inputStarted() {

    }
}

package mystuff.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by Xanior on 2016.10.11..
 */

public class Clickable {

    private int minX = -1, minY = -1, maxX = -1, maxY = -1; //Position of the Button  (1280*720)
    private float centerX, centerY;
    private int sizeX, sizeY;
    private Image img; //Image for the button
    private String function; //Name of the functionality, used in GUIManager::click()
    private boolean isActive = false; //can be clicked
    private boolean isVisible = false; //can be seen
    private boolean shouldRecalculate = true; //Recalculate the coordinates (window resize)

    /**
     * Creates a GUI button
     * @param imgPath The image of the button
     * @param function The function name, used to check which button was pressed and what to do
     * @param centerX X coordinate [0,1]
     * @param centerY Y coordinate [0,1]
     * @param sizeX width of the button in pixels
     * @param sizeY height of the button in pixels
     */
    public Clickable(String imgPath, String function, float centerX, float centerY, int sizeX, int sizeY) {
        try {
            Image tmp = new Image(imgPath);
            this.img = tmp;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        this.function = function;
        this.centerX = centerX;
        this.centerY = centerY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.isActive = true;
        this.isVisible = true;
    }

    /**
     * Should be called if the window is resized
     * @param newWidth the new width of the window
     * @param newHeight the new height of the window
     */
    public void recalculate(int newWidth, int newHeight) {
        minX = (int)(centerX * newWidth - sizeX/2);
        maxX = (int)(centerX * newWidth + sizeX/2);
        minY = (int)(centerY * newHeight - sizeY/2);
        maxY = (int)(centerY * newHeight + sizeY/2);
    }

    /**
     * Check if the button was clicked
     * @param mouseX The mouse X coordinate in pixels
     * @param mouseY The mouse Y coordinate in pixels
     * @return True if yes false if not
     */
    public boolean click(int mouseX, int mouseY) {
        return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY && isActive); //If the mouse is on the button, and its active
    }

    /**
     * Returns the function string of the button
     * @return the function string
     */
    public String getFunction() {
        return function;
    }

    /**
     * Never used but it can hide the button
     */
    public void disable() {
        isActive = false;
        isVisible = false;
    }

    /**
     * Never used but it can unhide the button
     */
    public void enable() {
        isActive = true;
        isVisible = true;
    }

    /**
     * Draws the button on the screen
     * @param width the width of the window
     * @param height the heigth of the window
     */
    public void draw(int width, int height) {
        if(shouldRecalculate) {
            recalculate(width, height);
        }

        if(isVisible) {
            img.draw(minX, minY, maxX - minX, maxY - minY);
        }
    }
}

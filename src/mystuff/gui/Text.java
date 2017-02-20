package mystuff.gui;

/**
 * Created by Xanior on 11/25/2016.
 */
public class Text {
    private String text;
    private String type;
    private int x, y;

    /**
     * A simple text to be drawn on the screen
     * @param text The text
     * @param x x coordinat in pixels
     * @param y y coordinate in pixels
     * @param type A function for the string when its clicked, "justtext" as default
     */
    public Text(String text, int x, int y, String type) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Check if the text was clicked (kinda, a 50 * 20 pixel box)
     * @param mouseX the mouse x coordinate in pixels
     * @param mouseY the mouse y coordinate in pixels
     * @return if the text was clicked
     */
    public boolean click(int mouseX, int mouseY) {
        if(mouseY - this.y > 0 && mouseY - this.y < 20) {
            if(mouseX - x < 50 && mouseX - x > -50) {
                return true;
            }
        }
        return false;
    }

    public String getText() {
        return text;
    }
    public String getType() { return type; }

    public int getX() { return x; }
    public int getY() { return y; }
}

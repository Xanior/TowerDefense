package phii.gha.gamelogic;

import java.io.Serializable;

public class Point implements Serializable{
    private float x;
    private float y;

    public Point(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public Point(Point p)
    {
        x = p.getX();
        y = p.getY();
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setX(float _x)
    {
        x = _x;
    }

    public void setY(float _y)
    {
        y = _y;
    }

    public void move(Point p)
    {
        x = p.getX();
        y = p.getY();
    }

    public void add(Point p)
    {
        x += p.getX();
        y += p.getY();
    }

    public float distanceSquare(Point p)
    {
        float dx = x - p.getX();
        float dy = y - p.getY();
        return dx * dx + dy * dy;
    }

    public float distance(Point p)
    {
        return (float) Math.sqrt(distanceSquare(p));
    }

    public float length()
    {return (float) Math.sqrt(x*x + y*y);}

    public boolean equals(Point p)
    {
        return p.x == x && p.y == y;
    }

    public String toString()
    {
        return "(" + x + ',' + y + ')';
    }

    public void fromString(String input)
    {
        String str = input.trim();
        str = str.substring(1, str.length()-1);
        String[] parts = str.split(",");
        x = Float.parseFloat(parts[0]);
        y = Float.parseFloat(parts[1]);
    }

    public Point(String input)
    {
        String str = input.trim();
        str = str.substring(1, str.length()-1);
        String[] parts = str.split(",");
        x = Float.parseFloat(parts[0]);
        y = Float.parseFloat(parts[1]);
    }

    public void normalize()
    {
        float f = length();
        x /= f;
        y /= f;
    }

    public void multiply(float value)
    {
        x *= value;
        y *= value;
    }
}

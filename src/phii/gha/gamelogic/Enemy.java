package phii.gha.gamelogic;

public class Enemy {
    private Point location;
    private Map map;
    private int ID;
    private static int nextID = 0;
    private int pointIndex;
    private int type;
    private float speed;
    private int health;
    private boolean arrived;
    private boolean dead;

    private static float[] speeds = {3.0f, 6.0f, 10.0f, 5.0f};
    private static int[]   healths = {5, 8, 13, 21};



    public Enemy(int _type, Point _location, Map _map)
    {
        location = new Point(_location);
        map = _map;
        pointIndex = 1;
        type = _type;
        arrived = false;
        health = healths[type - 1];
        speed = speeds[type - 1];
        dead = false;
        ID = nextID++;
    }

    public void update(int dt)
    {
        Point p = map.getPath().get(pointIndex);
        float d1 = location.distance(p);
        float dx = p.getX() - location.getX();
        float dy = p.getY() - location.getY();
        Point n = new Point(dx, dy);
        n.normalize();
        //System.out.println(n);
        n.multiply(speed / 100.0f * dt / 1000.0f);
        location.add(n);
        //System.out.println(n + " " + pointIndex);
        float d2 = location.distance(p);

        if (d2 > d1)
        {
            pointIndex++;
            if (pointIndex >= map.getPath().size())
            {
                //System.out.println("[INFO] [Enemy.update(int dt) Enemy arrived.");
                map.enemyArrived();
                arrived = true;
            }
        }
    }

    public Point getLocation()
    {
        return location;
    }

    public int getType() {return type;}

    public int getHealth() {return health;}

    public boolean getArrived(){return arrived;}

    public void decHealth(int value)
    {
        health -= value;
        if(health <= 0)
        {
            dead = true;
            map.enemyDied();
            //map.getPlayer().setCredit();
            map.getPlayer().giveCredit(healths[type - 1] * 5);
            map.getPlayer().giveMetal(healths[type - 1] * 2);
        }
    }

    public boolean isDead() {return dead;}

    public String toString()
    {
        return location.toString() + "&" + type + "&"  + health + "&" + pointIndex + "&" + ID;
    }

    public Enemy(String value, Map _map)
    {
        String[] parts = value.split("&");
        map = _map;
        location = new Point(parts[0]);
        type = Integer.parseInt(parts[1]);
        health = Integer.parseInt(parts[2]);
        pointIndex = Integer.parseInt(parts[3]);
        ID = Integer.parseInt(parts[4]);
        dead = health <= 0;
        if (pointIndex >= map.getPath().size())
        {
            arrived = true;
            map.enemyArrived();
        }
        else
            arrived = false;
        speed = speeds[type - 1];
    }

    public int getID(){return ID;}

    public static Enemy fromString(String value, Map map)
    {
        return new Enemy(value, map);
    }
}

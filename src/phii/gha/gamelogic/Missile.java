package phii.gha.gamelogic;

public class Missile {
    private Point location;
    private Enemy target;
    private float speed;
    private int type;
    private int damage;
    private Map map;
    private boolean destroyed;

    private static float[] speeds = {8.0f, 11.3f};
    private static int[]   damages = {1, 2};

    public Missile(int _type, Point _location, Map _map, Enemy _target)
    {
        type = _type;
        location = new Point(_location);
        map = _map;
        speed = speeds[type - 1];
        damage = damages[type -1];
        target = _target;
        destroyed = false;
    }

    public void update(int dt)
    {
        if (target == null || target.isDead())
        {
            destroyed = true;
            return;
        }
        float dx = target.getLocation().getX() - location.getX();
        float dy = target.getLocation().getY() - location.getY();
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        dx = dx * dt / 1000 * speed;
        dy = dy * dt / 1000 * speed;

        location.setX(location.getX() + dx);
        location.setY(location.getY() + dy);

        if (location.distanceSquare(target.getLocation()) < 0.0001f)
        {
            destroyed = true;
            target.decHealth(damage);
        }
    }

    public Point getLocation() {return location;}

    public int getType(){return type;}

    public float getSpeed() {return speed;}

    public int getDamage() {return damage;}

    public boolean isDestroyed(){return destroyed;}

    public String toString()
    {
        return type + "&" + location + "&" + target.getID();
    }

    public Missile(String value, Map _map)
    {
        String[] parts = value.split("&");
        type = Integer.parseInt(parts[0]);
        location = new Point(parts[1]);
        map = _map;
        speed = speeds[type - 1];
        damage = damages[type -1];
        destroyed = false;
        target = map.getEnemy(Integer.parseInt(parts[2]));
        if(target == null)
            System.out.println("[ERROR] [Missile.Missile(String, Map)] Enemy with ID " + parts[2] + " not found.");
    }
}

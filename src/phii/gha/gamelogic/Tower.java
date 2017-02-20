package phii.gha.gamelogic;

import java.util.concurrent.CopyOnWriteArrayList;

public class Tower extends Building{

    private int nextShoot; // time until next shoot
    private int shootTime;
    private TowerStrategy strategy;
    private float radius;
    private int missileType;


    private static int[] buildingTimes = {5000, 7500, 10000};
    private static int[] metalPrices = {1000, 2200, 5600};
    private static int[] creditPrices = {500, 750, 1000};
    private static int[] energyConsumptions = {120, 230, 300};
    private static String[] names = {"Basic Shooter", "Advanced Shooter", "Very Advanced Shooter"};
    private static int[] shootTimes = {1000, 700, 400};
    private static float[] radiuses = {15.0f, 17.0f, 20.0f};
    private static int[] missileTypes ={1, 1, 2};

    public int getShootTime()
    {
        return shootTime;
    }


    public Tower( int _type,  Point _l, Map _map)
    {
        super(_l, _type, buildingTimes[_type - 1], creditPrices[_type - 1], metalPrices[_type - 1], energyConsumptions[_type - 1], names[_type - 1]);
        shootTime = shootTimes[_type - 1];
        nextShoot = shootTime;
        radius = radiuses[_type - 1] / 100;
        missileType = missileTypes[_type - 1];
        strategy = TowerStrategy.FIRST;
        map = _map;
        System.out.println("[INFO] Tower created with type = " + getType() + " at the point" + location);
    }

    public Tower(Tower tower)
    {
        super(tower);
        shootTime = tower.getShootTime();
        strategy = tower.getStrategy();
    }

    public Tower(Tower tower, Point _l, Map _map)
    {
        super(tower, _l, _map);
        shootTime = tower.getShootTime();
        strategy = tower.getStrategy();
    }



    @Override
    public void update(int dt)
    {
        super.update(dt);
        if (nextShoot > 0)
        {
            nextShoot -= dt;
            if (nextShoot <= 0)
            {
                nextShoot = shootTime;
                shoot();
            }
        }
    }

    private Enemy getFirstEnemy()
    {
        CopyOnWriteArrayList<Enemy> enemies = map.getEnemies();
        float rs = radius * radius;
        Enemy result = null;
        float dmin = -1;
        float d;
        for (Enemy enemy : enemies)
        {
            d = location.distanceSquare(enemy.getLocation());
            if (d < rs && (dmin < 0 || d < dmin))
            {
                dmin = d;
                result = enemy;
            }
        }
        return result;
    }

    private Enemy getLastEnemy()
    {
        CopyOnWriteArrayList<Enemy> enemies = map.getEnemies();
        float rs = radius * radius;
        Enemy result = null;
        float dmax = -1;
        float d;
        for (int i = enemies.size() - 1; i >= 1; i--)
        {
            d = location.distanceSquare(enemies.get(i).getLocation());
            if ((d < rs) && (d > dmax))
            {
                dmax = d;
                result = enemies.get(i);
            }
        }
        return result;
    }

    private Enemy getWeakestEnemy()
    {
        CopyOnWriteArrayList<Enemy> enemies = map.getEnemies();
        float rs = radius * radius;
        int hmin = -1;
        Enemy target = null;
        for (int i = 0; i < enemies.size() - 1; i++)
        {
            if (location.distanceSquare(enemies.get(i).getLocation()) < rs &&
                    (enemies.get(i).getHealth() < hmin || hmin == -1))
            {
                target = enemies.get(i);
                hmin = target.getHealth();
            }
        }
        return target;
    }

    private Enemy getStrongestEnemy()
    {
        CopyOnWriteArrayList<Enemy> enemies = map.getEnemies();
        float rs = radius * radius;
        int hmax = -1;
        Enemy target = null;
        for (int i = 0; i < enemies.size() - 1; i++)
        {
            if (location.distanceSquare(enemies.get(i).getLocation()) < rs &&
                    (enemies.get(i).getHealth() > hmax))
            {
                target = enemies.get(i);
                hmax = target.getHealth();
            }
        }
        return target;
    }

    private void shoot()
    {
        if (map.getEnemies().size() > 0)
        {
            Enemy target = null;
            switch (strategy)
            {
                case FIRST: target = getFirstEnemy(); break;
                case LAST: target = getLastEnemy(); break;
                case WEAKEST: target = getWeakestEnemy(); break;
                case STRONGEST: target = getStrongestEnemy(); break;
            }
            if (target != null)
            {
                //Logger.getGlobal().log(Level.INFO, "Missile spawned. Tower id = {0}", getID());
                map.spawnMissile(missileType, location, target);
            }
        }
    }


    public TowerStrategy getStrategy()
    {
        return strategy;
    }

    public void upgrade()
    {
        if(type >= 1 && type <= 2 && map.getPlayer().getCredit() >= creditPrices[type])
        {
            type++;
            shootTime = shootTimes[type - 1];
            nextShoot = shootTime;
            radius = radiuses[type - 1] / 100;
            missileType = missileTypes[type - 1];
            map.getPlayer().giveCredit(-creditPrices[type-1]);
        }
    }

    public String toString()
    {
        return String.valueOf(getType()) + "&" +
                location + "&" +
                getTimeToComplete() + "&" +
                nextShoot + "&" +
                strategy + "&" +
                ID;
    }

    public static Tower fromString(String input, Map _map)
    {
        String[] parts = input.split("&");
        int _type =  Integer.parseInt(parts[0]);
        Point _l = new Point(parts[1]);
        Tower t = new Tower(_type, _l, _map);
        int ttc = Integer.parseInt(parts[2]);
        t.setTimeToComplete(ttc);
        t.nextShoot = Integer.parseInt(parts[3]);
        t.strategy = TowerStrategy.valueOf(parts[4]);
        return t;
    }

    public Tower(String value, Map _map)
    {
        String[] parts = value.split("&");
        type =  Integer.parseInt(parts[0]);
        location = new Point(parts[1]);
        timeToComplete = Integer.parseInt(parts[2]);
        nextShoot = Integer.parseInt(parts[3]);
        strategy = TowerStrategy.valueOf(parts[4]);
        ID = Integer.parseInt(parts[5]);
        priceCredit = creditPrices[type - 1];
        priceMetal = metalPrices[type - 1];
        energyConsumption = energyConsumptions[type - 1];
        name = names[type - 1];
        shootTime = shootTimes[type - 1];
        radius = radiuses[type - 1] / 100;
        missileType = missileTypes[type - 1];
        map = _map;
        done = timeToComplete == 0;
    }

}

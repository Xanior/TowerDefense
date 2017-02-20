package phii.gha.gamelogic;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Map {

    private CopyOnWriteArrayList<Tower> towers;
    private CopyOnWriteArrayList<Enemy> enemies;
    private CopyOnWriteArrayList<Missile> missiles;
    private ArrayList<Point> path;
    private boolean hasEnemyArrived;
    private boolean hasEnemyDied;
    private Player player;

    public Map(Player _player)
    {
        towers = new CopyOnWriteArrayList<>();
        enemies = new CopyOnWriteArrayList<>();
        missiles = new CopyOnWriteArrayList<>();
        path = new ArrayList<>();
        player = _player;
    }

    public void buildTower(int type, Point location)
    {
        Tower t = new Tower(type, location, this);
        if(player.getCredit() >= t.getPriceCredit()) {
            towers.add(new Tower(type, location, this));
            player.giveCredit(-t.getPriceCredit());
        }
    }

    public void spawnEnemy(int type)
    {
        enemies.add(new Enemy(type, path.get(0), this));
    }

    public void spawnMissile(int type, Point location, Enemy target){missiles.add(new Missile(type, location, this, target));}

    public void upgrade(int ID)
    {
        for (Tower tower : towers)
        {
            if (tower.getID() == ID)
            {
                tower.upgrade();
                return;
            }
        }
    }



    void update(int dt)
    {
        hasEnemyArrived = false;
        hasEnemyDied = false;
        for (Tower tower : towers)
            tower.update(dt);
        for (Enemy enemy : enemies)
            enemy.update(dt);
        for (Missile missile : missiles)
            missile.update(dt);
        int i = 0;
        if (hasEnemyDied)
        {
            while (i < enemies.size())
            {
                //enemies.
                if (enemies.get(i).isDead())
                {
                    enemies.remove(i);
                } else i++;
            }
        }
        if(hasEnemyArrived)
        {
            i = 0;
            while (i < enemies.size())
            {
                if (enemies.get(i).getArrived())
                {
                    player.decHealth(enemies.get(i).getHealth());
                    enemies.remove(i);
                }
                else i++;
            }
        }

        removeMissiles();
    }

    private void removeMissiles()
    {
        int i = 0;
        while (i < missiles.size())
        {
            if (missiles.get(i).isDestroyed())
                missiles.remove(i);
            else i++;
        }
    }

    public void enemyArrived()
    {
        hasEnemyArrived = true;
    }

    public void enemyDied(){hasEnemyDied = true;}

    public ArrayList<Point> getPath()
    {
        return path;
    }

    public CopyOnWriteArrayList<Tower> getTowers() {return towers;}

    public CopyOnWriteArrayList<Enemy> getEnemies() {return enemies;}

    public CopyOnWriteArrayList<Missile> getMissiles() {return missiles;}

    public void setPath(ArrayList<Point> value)
    {
        path = value;
    }

    public Enemy getEnemy(int ID)
    {
        for (Enemy e : enemies)
        {
            if (e.getID() == ID)
                return e;
        }
        return null;
    }

    public String toString()
    {
        String result = "";
        for(Tower t : towers)
        {
            result += t.toString();
            if(towers.get(towers.size() - 1) != t)
                result += "@";
        }
        result+=" # ";
        for(Enemy e : enemies)
        {
            result += e.toString();
            if(enemies.get(enemies.size() - 1) != e)
                result += "@";
        }
        result+=" # ";
        for(Missile m : missiles)
        {
            result += m.toString();
            if(missiles.get(missiles.size() - 1) != m)
                result += "@";
        }
        return result;
    }

    public void loadFromString(String value)
    {
        String[] lines = value.split("#");
        if(lines.length < 3)
            System.out.println("[ERROR] [Map.loadFromString(String)] too few line.");
        else if(lines.length > 3)
            System.out.println("[ERROR] [Map.loadFromString(String)] too many line.");
        else
        {
            enemies.clear();
            towers.clear();
            missiles.clear();
            String[] parts = lines[0].trim().split("@");
            for(String s : parts)
                if(!s.isEmpty()) towers.add(new Tower(s, this));
            parts = lines[1].trim().split("@");
            for(String s : parts)
                if(!s.isEmpty()) enemies.add(new Enemy(s, this));
            parts = lines[2].trim().split("@");
            for(String s : parts)
                if(!s.isEmpty()) missiles.add(new Missile(s, this));
        }
    }

    public String pathToString()
    {
        String result = "";
        for(Point p : path)
        {
            result += p.toString();
            if(path.get(path.size() - 1) != p)
                result += "@";
        }
        System.out.println("path : " + result);
        return result;
    }

    public void loadPathFromString(String value)
    {
        String[] parts = value.split("@");
        if(parts.length < 2)
            System.out.println("[ERROR] [Map.loadPathFromString(String)] too few Points in pathway.");
        else for(String s: parts)
        {
            path.add(new Point(s));
        }
    }

    public Player getPlayer()
    {
        return player;
    }
}

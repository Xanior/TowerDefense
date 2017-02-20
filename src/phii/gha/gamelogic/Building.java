package phii.gha.gamelogic;

public class Building {
    protected int ID;
    protected int type;
    private static int nextID = 1;
    protected Point location;
    protected int timeToComplete; // the time which is left until tower building is complete (in ms)
    protected boolean done;
    protected String name;

    protected int priceMetal;
    protected int priceCredit;
    protected int energyConsumption;

    protected Map map;

    public Building()
    {}

    public Building(Point _l, int _type, int _timeToComplete, int _priceCredit, int _priceMetal, int _energyConsumption, String _name)
    {
        location = new Point(_l);
        type = _type;
        ID = nextID++;
        timeToComplete = _timeToComplete;
        priceCredit = _priceCredit;
        priceMetal = _priceMetal;
        name = _name;
        energyConsumption = _energyConsumption;
        done = false;
        map = null;
    }

    public Building(Building building)
    {
        location = new Point(building.getLocation());
        type = building.getType();
        done = building.isDone();
        timeToComplete = building.getTimeToComplete();
        priceMetal = building.getPriceMetal();
        priceCredit = building.getPriceCredit();
        name = building.getName();
        energyConsumption = building.getEnergyConsumption();
        map = null;
        ID = nextID++;
    }

    public Building(Building building, Point p, Map _map)
    {
        location = new Point(p);
        type = building.getType();
        done = building.isDone();
        timeToComplete = building.getTimeToComplete();
        priceMetal = building.getPriceMetal();
        priceCredit = building.getPriceCredit();
        name = building.getName();
        energyConsumption = building.getEnergyConsumption();
        map = _map;
        ID = nextID++;
    }



    public int getType()
    {
        return type;
    }

    public int getPriceMetal()
    {
        return priceMetal;
    }

    public int getPriceCredit()
    {
        return priceCredit;
    }

    public boolean isDone()
    {
        return done;
    }

    public int getTimeToComplete()
    {
        return timeToComplete;
    }

    public String getName()
    {
        return name;
    }

    public int getEnergyConsumption()
    {
        return energyConsumption;
    }

    public Point getLocation()
    {
        return location;
    }

    public int getID()
    {
        return ID;
    }

    public void update(int dt)
    {
        if (timeToComplete > 0)
        {
            timeToComplete -= dt;
            if (timeToComplete <= 0)
            {
                timeToComplete = 0;
                done = true;
            }
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append("&");
        sb.append(location).append("&");
        sb.append(timeToComplete).append("&");
        return sb.toString();
    }

    public void fromString(String input)
    {
        String[] parts = input.split("&");
        type = Integer.parseInt(parts[0]);
        location = new Point(parts[1]);
        timeToComplete = Integer.parseInt(parts[2]);
        done = timeToComplete <= 0;
        /*priceMetal = building.getPriceMetal();
        priceCredit = building.getPriceCredit();
        name = building.getName();
        energyConsumption = building.getEnergyConsumption();
        map = _map;
        ID = nextID++;*/
    }

    protected void setTimeToComplete(int value)
    {
        timeToComplete = value;
    }
}

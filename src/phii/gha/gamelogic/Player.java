package phii.gha.gamelogic;


public class Player {
    private String name;
    private int credit;
    private int metal;
    private int health;
    private String healthString;
    private String creditString;

    public Player(String _name, int _credit, int _metal)
    {
        name = _name;
        credit = _credit;
        metal = _metal;
        healthString = "200";
        health = 200;
        creditString = Integer.toString(credit);
    }

    public String getName()
    {
        return name;
    }

    public int getCredit(){return credit;}
    public int getMetal(){return metal;}

    public void setCredit(int _credit){credit = _credit; creditString = Integer.toString(credit);}
    public void setMetal(int _metal){metal = _metal;}

    public int getHealth(){return health;}
    public void decHealth(int value){health -= value; healthString = Integer.toString(health);}
    public void setHealth(int value){health = value; healthString = Integer.toString(health);}

    public void giveCredit(int value){credit += value; creditString = Integer.toString(credit);}
    public void giveMetal(int value){credit += value;}


    public String getHealthString(){return healthString;}
    public String getCreditString(){return creditString;}
}

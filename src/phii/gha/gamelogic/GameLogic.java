package phii.gha.gamelogic;


public class GameLogic {
    private Map map1, map2;
    private Player player1, player2;

    public GameLogic(Player _player1, Player _player2)
    {
        player1 = _player1;
        player2 = _player2;
        map1 = new Map(player1);
        map2 = new Map(player2);
    }

    public void update(int dt)
    {
        map1.update(dt);
        map2.update(dt);
    }

    public Map getMap1(){return map1;}

    public Map getMap2(){return map2;}

    public String toString()
    {
        String result =
                player1.getHealth() + "&" + player1.getCredit() + "&" + player1.getMetal() + "$" +
                map1.toString() + "$" +
                player2.getHealth() + "&" + player2.getCredit() + "&" + player2.getMetal() + "$" +
                map2.toString();
        return result;
    }

    public void loadFromString(String value)
    {
        String[] lines = value.split("\\$");
        if(lines.length < 4)
            System.out.println("[ERROR] [GameLogic.loadFromString(String)] too few arguments.");
        else if(lines.length > 4)
            System.out.println("[ERROR] [GameLogic.loadFromString(String)] too many arguments.");
        else
        {
            String[] parts = lines[0].split("&");
            player1.setHealth(Integer.parseInt(parts[0]));
            player1.setCredit(Integer.parseInt(parts[1]));
            player1.setMetal(Integer.parseInt(parts[2]));

            parts = lines[2].split("&");
            player2.setHealth(Integer.parseInt(parts[0]));
            player2.setCredit(Integer.parseInt(parts[1]));
            player2.setMetal(Integer.parseInt(parts[2]));

            map1.loadFromString(lines[1]);
            map2.loadFromString(lines[3]);
        }
    }
}

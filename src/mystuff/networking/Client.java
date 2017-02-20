package mystuff.networking;

import mystuff.gamelogic.GameManager;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Created by Xanior on 10/28/2016.
 */
public class Client extends Thread{

    private String IP = "192.168.137.1";
    private int port = 12001;
    private boolean running = false;
    private PrintWriter outToServer;
    private BufferedReader inFromServer;
    private Socket clientSocket;
    private GameManager gm;
    private boolean received = false;
    private String opponent = "";

    /**
     * Create a new TCP-Client.
     *
     * @param IP IP address of the server
     * @param port port number of the server
     * @param gm A reference to the GameManager class
     */
    public Client(String IP, int port, GameManager gm) {
        this.IP = IP;
        this.port = port;
        this.gm = gm;
    }

    /**
     * Create a new TCP-Client.
     * Format:
     * IPaddress\n
     * portnumber
     *
     * @param filename The path and the name of the config file
     * @param gm A reference to the GameManager class
     */
    public Client(String filename, GameManager gm) {
        this.gm = gm;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            this.IP = br.readLine();
            this.port = Integer.parseInt(br.readLine());
            System.out.println("Connecting to server on " + IP + ":" + port);
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds a TCP socket connection with the server
     */
    private void initialize(){
            System.out.println("Initializing client...");
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(IP, port), 1000); //100ms timeout
            clientSocket.isConnected();
            outToServer = new PrintWriter(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            running = true;
            System.out.println("Client Initialized!");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
        }
    }

    /**
     * The main loop for the Client thread, checks for incoming messages
     */
    public void run() {
        initialize();

        while(running) {
            processMessage();
        }
    }

    /**
     * The message processing function
     * LOG_IN_ERROR : login failed
     * LOG_IN_SUCCESS : login succesful
     * DENY : the opponent denied your request
     * PLAY : a game has started
     * START : someone wants to play with you
     * GL : update data for the GameLogic class
     * PATH : a Path for the enemies to walk on, generated on the server side
     * WIN : you won
     * LOST : you lost
     */
    private void processMessage() {
        try {
            if(clientSocket.isConnected() && inFromServer != null) {
                String msg = inFromServer.readLine();
                System.out.println("Response from server: " + msg);

                if(msg == null){
                    System.out.println("Received message not valid!");
                } else if(msg.startsWith("LOG_IN_ERROR")) {
                    System.out.println("Login failed!");
                } else if (msg.startsWith("LOG_IN_SUCCESS")) {
                    System.out.println("Login successful!");
                    gm.goInOpponentSelect();
                } else if (msg.startsWith("DENY")) {
                    System.out.println("Opponent denied!");
                } else if (msg.startsWith("PLAY")) {
                    System.out.println("Opponent accepted!");
                    String[] split = msg.split(" ");
                    gm.setMyMapID(Integer.parseInt(split[2]));
                    gm.goInGame();
                } else if (msg.startsWith("START")) {
                    String[] split = msg.split(" ");
                    opponent = split[1];
                    received = true;
                } else if (msg.startsWith("GL")) {
                    String msg2 = inFromServer.readLine();
                    gm.getGameLogic().loadFromString(msg2);
                } else if(msg.startsWith("PATH")) {
                    String msg2 = inFromServer.readLine();
                    gm.getGameLogic().getMap1().loadPathFromString(msg2);
                    gm.getGameLogic().getMap2().loadPathFromString(msg2);
                } else if(msg.startsWith("WIN")) {
                    gm.goInWon();
                    gm.closeSocket();
                } else if(msg.startsWith("LOST")) {
                    gm.goInLost();
                    gm.closeSocket();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the string to the server
     * @param msg the string to be sent
     */
    public void sendMsg(String msg) {
        if(outToServer != null) {
            outToServer.println(msg);
            outToServer.flush();
        }
    }

    /**
     * Sends an ACCEPT command to the server
     */
    public void acceptGame() {
        sendMsg("ACCEPT");
        sendMsg("\n");
    }

    /**
     * Stops the message check loop
     */
    public void stopThread() {
        running = false;
    }

    /**
     * Send a login request
     * @param username the username of the user
     */
    public void login(String username) {
        if(outToServer != null) {
            outToServer.println("LOGIN " + username);
            outToServer.flush();
            System.out.println("Sending Login request : Username: " + username);
        } else {
            System.out.println("Login failed! No active socket!");
        }
    }

    /**
     * Check if the Client has received a play request
     * @return True if yes false if no
     */
    public boolean receivedRequest() {
        if (received) {
            received = false;
            return true;
        } else return false;
    }

    /**
     * Get the opponents name
     * @return The opponents name
     */
    public String getOpponent() { return opponent; }

    /**
     * Ask the server for the online users
     * @return the list of the users
     */
    public ArrayList<String> listUsers() {
        outToServer.println("LIST");
        outToServer.flush();
        ArrayList<String> ret = new ArrayList<>();
        String tmpUser;
        boolean running = true;
        try {
            while(running) {
                tmpUser = inFromServer.readLine();
                if(tmpUser.isEmpty()) {
                    running = false;
                } else {
                    ret.add(tmpUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Active users:");
        for(int i = 0; i < ret.size(); i++) {
            System.out.println(ret.get(i));
        }
        return ret;
    }

    /**
     * Sends a request to play with someone
     * @param opponent the username of the opponent or "RANDOM"
     */
    public void sendOpponentRequest(String opponent) {
        if(outToServer != null) {
            outToServer.println("START " + opponent);
            outToServer.flush();
            System.out.println("Sending opponent request : Opponent: " + opponent);
        } else {
            System.out.println("Unexpected Error at sendOpponentRequest()!");
        }
    }

    /**
     * Sends a tower build request to the server
     * @param type the level of the tower, should almost always be = 1
     * @param x x coordinates in [0,1]
     * @param y y coordinates in [0,1]
     */
    public void sendBuildTower(int type, float x, float y) {
        sendMsg("BUILD " + type + " " + new Float(x).toString() + " " + new Float(y).toString());
    }

    /**
     * Sends a tower upgarde request
     * @param id the ID of the tower to upgrade
     */
    public void sendUpgradeTower(int id) {
        sendMsg("UPGRADE " + id);
    }

    /**
     * Closes the socket and helps the server to shut down the connection
     */
    public void cleanup(){
        try {
            if(clientSocket != null) {
                sendMsg("CLOSED");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

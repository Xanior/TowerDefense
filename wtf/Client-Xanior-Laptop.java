package mystuff.networking;

import mystuff.gamelogic.GameManager;
import org.lwjgl.Sys;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Xanior on 10/28/2016.
 */
public class Client extends Thread{

    private String IP;
    private int port;
    private boolean initialized = false;
    private PrintWriter outToServer;
    private BufferedReader inFromServer;
    private Socket clientSocket;
    private GameManager gm;
    private boolean recieved = false;
    private String opponent = "";

    public Client(String IP, int port, GameManager gm) {
        this.IP = IP;
        this.port = port;
        this.gm = gm;
    }

    private void initialize() throws IOException {
            System.out.println("Initializing client...");
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(IP,port), 250); //100ms timeout

            outToServer = new PrintWriter(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            initialized = true;
            System.out.println("Client Initialized!");
    }

    public void run() {
        boolean running = true;
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(running) {
            processMessage();
        }
    }

    private void processMessage() {
        try {
            if(inFromServer != null && !clientSocket.isClosed()) {
                String msg = inFromServer.readLine();
                System.out.println("Response from server: " + msg);

                if(msg == null){
                    System.out.println("Recieved message not valid!");
                } else if(msg.startsWith("LOG_IN_ERROR")) {
                    System.out.println("Login failed!");
                } else if (msg.startsWith("LOG_IN_SUCCESS")) {
                    System.out.println("Login successful!");
                    gm.goInOpponentSelect();
                } else if (msg.startsWith("DENY")) {
                    System.out.println("Opponent Denied!");
                } else if (msg.startsWith("PLAY")) {
                    System.out.println("Opponent accepted!");
                    gm.goInGame();
                } else if (msg.startsWith("START")) {
                    //msg.substring()
                    opponent = "asdasd";
                    recieved = true;
                } else if(msg.startsWith("PLAY")) {
                    gm.goInGame();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if(outToServer != null) {
            outToServer.println(msg);
            outToServer.flush();
        }
    }

    public void login(String username) {
        if(outToServer != null) {
            outToServer.println("LOGIN " + username);
            outToServer.flush();
            System.out.println("Sending Login request : Username: " + username);
        } else {
            System.out.println("Login failed! No active socket!");
        }
    }

    public boolean recievedRequest() {
        if (recieved) {
            recieved = false;
            return true;
        } else return false;
    }

    public String getOpponent() { return opponent; }

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

    public void sendOpponentRequest(String opponent) {
        if(outToServer != null) {
            outToServer.println("START " + opponent);
            outToServer.flush();
            System.out.println("Sending opponent request : Opponent: " + opponent);
        } else {
            System.out.println("Unexpected Error at sendOpponentRequest()!");
        }
    }

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

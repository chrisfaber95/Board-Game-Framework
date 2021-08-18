package NetworkCore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: Add documentation
//TODO: Add additional error handling
//TODO: Add some more delicate exception handling
public class NetworkController {
    private List _listeners;

    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 7789;

    private Socket clientSocket;
    private ExecutorService threadPool;

    private BufferedReader reader;
    private DataOutputStream out;

    private HashMap<String, Object> dataBuffer;

    private String hostname;
    private int port;

    public NetworkController(String _host, int _port) {
        this.hostname = _host;
        this.port = _port;
        this.init();
    }

    public NetworkController(String _host) {
        this.hostname = _host;
        this.port = this.DEFAULT_PORT;
        this.init();
    }

    public NetworkController() {
        this.hostname = this.DEFAULT_HOST;
        this.port = this.DEFAULT_PORT;
        this.init();
    }

    private void init() {
        this._listeners = new ArrayList<NetEventListener>();
        this.threadPool = Executors.newFixedThreadPool(3);
        this.dataBuffer = new HashMap<>();
    }

    private void listen() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ( clientSocket.isConnected() ) {
                String line;
                while ((line = reader.readLine()) != null){
                    this.handle(line);
                }
            }

            reader.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void send(String com) {
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());

            // Send whatever message
            out.writeUTF(com + "\n");
            out.flush();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handle(String _com) {
        String com = _com;

        // No need to do anything if all we get is an ok.
        if(com.toLowerCase().equals("ok")) return;

        // Stupid messages
        if(com.toLowerCase().contains("strategic game server fixed")
                || com.toLowerCase().contains("copyright 2015 hanzehogeschool groningen"))
            return;

        if(com.toLowerCase().startsWith("err")) {
            com = com.substring(4, com.length());

            // Something, something - Error?
            System.out.println("[NetworkMan:ERROR]" + com);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("ERROR", com);
            this.fireNetEvent(new NetMessage( MsgType.translateMessage("ERR"), payload));

            return;
        }

        // Strip the SVR part
        if(com.toLowerCase().startsWith("svr"))
            com = com.substring(4, com.length());

        String[] dat;
        dat = com.split(" ");
        String data;
        data = com.substring(dat[0].length(), com.length()).replace(" ", "");

        switch(dat[0].toLowerCase()) {
            case "playerlist":
                System.out.println("Received Player list!");
                String[] players = data
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                this.dataBuffer.put("PLAYERLIST", new ArrayList<String>(Arrays.asList(players)) );
                break;

            case "gamelist":
                System.out.println("Received Game list!");
                String[] games = data
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                this.dataBuffer.put("GAMELIST", new ArrayList<String>(Arrays.asList(games)) );
                break;

            case "game":
                String type = dat[1];
                String payload = com.substring(5 + dat[1].length(), com.length());
                System.out.println("[NetworkMan:INCOMING]\tReceived message of type:\t" + type + "\tPayload<" + payload + ">");

                // Challenge was cancelled
                if(dat[1].toUpperCase().equals("CHALLENGE") && dat[2].toUpperCase().equals("CANCELLED")) {
                    payload = com.substring(5 + dat[1].length() + dat[2].length(), com.length());
                    type = "CHALLENGE_CANCELLED";
                }

                // Create hashmap from received payload
                HashMap<String, String> pack = new HashMap<>();
                String[] pairs = payload
                        .replace("{","")
                        .replace("}","")
                        .replace("\"", "")
                        .trim()
                        .split(",");

                for(String pair : pairs) {
                    if(!pair.isEmpty()) {
                        String[] temp = pair.split(":");
                        if(temp.length > 1)
                            pack.put(temp[0].trim(), temp[1].trim());
                        else
                            pack.put(temp[0].trim(), "");
                    }
                }

                this.fireNetEvent(new NetMessage(MsgType.translateMessage(type), pack));
                break;

            default:
                System.out.println("[NetworkMan:ERROR]Unexpected: " + dat[0] + "\t" + com);
                break;
        }

    }

    /*****************************
    ************ API *************
    ******************************/
    public synchronized void addNetEventListener( NetEventListener l ) { _listeners.add(l); }
    public synchronized void removeNetEventListener(  NetEventListener l ) { _listeners.remove(l); }
    public synchronized void fireNetEvent( NetMessage msg ) {
        for (Object _listener : _listeners) {
            ((NetEventListener) _listener).onDataReceived(new NetEvent(this, msg));
        }
    }
    public boolean start() {
        if(  !this.isConnected() ) {
            try {
                this.clientSocket = new Socket(
                        this.hostname == null ? this.DEFAULT_HOST : this.hostname,
                        this.port == 0 ? this.DEFAULT_PORT : this.port
                );
                threadPool.execute(this::listen);
                return true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else
            return true;
    }

    public boolean stop() {
        if( this.isConnected() ) {
            try {
                this.clientSocket.close();
                this.threadPool.shutdownNow();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else
            return true;
    }

    public boolean isConnected() {
        return this.clientSocket != null && this.clientSocket.isConnected();
    }

    public void setHost(String host) { this.hostname = host; }

    public void setPort(int _port) { this.port = _port; }

    public boolean reconnect() {
        if(!this.isConnected()) {
            try {
                this.clientSocket = new Socket(
                        this.hostname == null ? this.DEFAULT_HOST : this.hostname,
                        this.port == 0 ? this.DEFAULT_PORT : this.port
                );
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else return true;
    }

    public void login(String s) {
        this.send("login " + s);
        System.out.println("[NetworkMan]\tSent login request.");
    }

    public void exit() {
        this.send("Exit");
        sleep(200);
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[NetworkMan]\tSent exit request");
    }

    public ArrayList<String> getPlayers() {
        // Remove list
        if(dataBuffer.containsKey("PLAYERLIST"))
            dataBuffer.remove("PLAYERLIST");

        this.send("get playerlist");
        System.out.println("[NetworkMan]\tRequested playerlist");

        // Wait till we have data in our buffer
        while(!this.dataBuffer.containsKey("PLAYERLIST")) sleep(1);

        return (ArrayList<String>)this.dataBuffer.get("PLAYERLIST");
    }

    public ArrayList<String> getGames() {
        // Remove list
        if(dataBuffer.containsKey("GAMELIST"))
            dataBuffer.remove("GAMELIST");

        this.send("get gamelist");
        System.out.println("[NetworkMan]\tRequested gamelist");

        // Wait till we have data in our buffer
        while(!this.dataBuffer.containsKey("GAMELIST")) sleep(1);

        return (ArrayList<String>)this.dataBuffer.get("GAMELIST");
    }

    public void subscribe(String game) {
        this.send("subscribe " + game);
        System.out.println("[NetworkMan]\tSubscribed to: " + game);
    }

    public void forfeit() {
        this.send("forfeit");
        System.out.println("[NetworkMan]\tForfeited match");
    }

    public void doMove(int i) {
        this.send("move " + i);
        System.out.println("[NetworkMan]\tMade move on location: " + i);
    }

    public void challenge(String opponent, String game) {
        this.send("challenge \"" + opponent + "\" \"" + game + "\"");
        System.out.println("[NetworkMan]\tAttempted to challenge player: " + opponent + " at game: " + game);
    }

    public void acceptChallenge(int challengeNumber) {
        this.send("challenge accept " + challengeNumber);
        System.out.println("[NetworkMan]\tAttempted to accept challange with id: " + challengeNumber);
    }
}


//-----------------------------------------/
// NAME: Saif Mahmud
// VERSION: 10/20/2022
// PURPOSE: Creating a TCP base server based java chat app where users can create separate rooms and chat.
//-----------------------------------------/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket myServerSocket;
    private Socket incomingClient;
    private ArrayList<ServerConnectionManager> allClientConnections;
    protected static ArrayList<ChatRoom> allChatRooms;
    private boolean turnOffServer;
    private ExecutorService tPool;

    public static void main(String[] args) {
        System.out.println("Starting the server...\n");
        Server myServer = new Server();
        myServer.run();
        System.out.println("Server Exiting\n");
    }
    public Server(){
        turnOffServer = false;
        allClientConnections = new ArrayList<>();
        allChatRooms = new ArrayList<>();
    }

    @Override
    public void run() {

        try {
            myServerSocket = new ServerSocket(27015);
            tPool = Executors.newCachedThreadPool();

            while(!turnOffServer) {
//              accept the new client connection
                incomingClient = myServerSocket.accept();
                ServerConnectionManager newServerConnection = new ServerConnectionManager(incomingClient);
                allClientConnections.add(newServerConnection);

                //now split into 2 pools, one will be stucked into while loop/
                tPool.execute( newServerConnection );
            }
        } catch (IOException e) {
            e.printStackTrace();
            terminateServer();
        }
    }

//   down the server
    public void terminateServer(){
        try {
            myServerSocket.close();
            turnOffServer = true;
            closeAllClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  close all connected clients.
    private void closeAllClients() {
        for(int i = 0; i < allClientConnections.size(); i++){
            allClientConnections.get(i).terminateClient();
        }
    }

    public Socket getIncomingClient(){
        return incomingClient;
    }
}

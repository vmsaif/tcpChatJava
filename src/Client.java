
//-----------------------------------------/
// NAME: Saif Mahmud
// VERSION: 10/20/2022
// PURPOSE: Creating a TCP base server based java chat app where users can create separate rooms and chat.
//-----------------------------------------/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
    private String username;
    private Socket clientSocket;
    private BufferedReader input;
    private String incomingMsg;

    public static void main(String[] args) {
        System.out.println("Starting Client...");
        Client myClient = new Client();
        myClient.run();
        System.out.println("Exiting Client...");
    }

    public Client() {}

    public Client(String username){
        this.username = username;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("localhost", 27015);

            InputStreamReader inStreamReader = new InputStreamReader(clientSocket.getInputStream());
            input = new BufferedReader(inStreamReader);

            ClientManager myClientManager = new ClientManager(clientSocket);
            Thread myClientThread = new Thread(myClientManager);
            myClientThread.start();

            receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    receive the messages from the server
    private void receiveMessage() {
        try {
            while ((incomingMsg = input.readLine()) != null) {
                System.out.println(incomingMsg);
            }
        } catch (IOException e) {
            System.out.println("Connection dropped. Server is down...\n");
        }
    }

    protected String getUsername() {
        return username;
    }

    public String toString(){
        return username;
    }
}

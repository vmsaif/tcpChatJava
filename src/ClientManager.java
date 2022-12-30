
//-----------------------------------------/
// NAME: Saif Mahmud
// VERSION: 10/20/2022
// PURPOSE: Creating a TCP base server based java chat app where users can create separate rooms and chat.
//-----------------------------------------/

import java.io.*;
import java.net.Socket;

public class ClientManager implements Runnable {

    private boolean exitClient;

    private PrintWriter output;
    private final Socket clientSocket;

    public ClientManager (Socket clientSocket){
        this.clientSocket = clientSocket;
        exitClient = false;
    }

    @Override
    public void run() {
        try {
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            InputStreamReader sysIn = new InputStreamReader(System.in);
            BufferedReader buffer = new BufferedReader(sysIn);

//            send messages to the server
            while(!exitClient){
                String currMsg = buffer.readLine();
                output.println(currMsg);
            }

        } catch (Exception e){
            exitClient = true;
        }
    }
}//class

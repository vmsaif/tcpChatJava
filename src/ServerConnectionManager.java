
//-----------------------------------------/
// NAME: Saif Mahmud
// VERSION: 10/20/2022
// PURPOSE: Creating a TCP base server based java chat app where users can create separate rooms and chat.
//-----------------------------------------/

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionManager implements Runnable {

    private Client myClient;
    private Socket incomingClient;
    private String[] myLine;
    private ArrayList<Client> allClients;

    private BufferedReader input;
    private PrintWriter output;
    private String username;
    private String currMsg;
    private String commandAndRoomName;

    private ChatRoom clientChatRoom;
    public ServerConnectionManager(Socket incomingClient) {
        this.incomingClient = incomingClient;
        allClients = new ArrayList<>();
        clientChatRoom = null;
    }

    @Override
    public void run() {

        try {
            InputStreamReader inStreamReader = new InputStreamReader(incomingClient.getInputStream());

            //get from client
            input = new BufferedReader(inStreamReader);

            //to be sent to client
            output = new PrintWriter(incomingClient.getOutputStream(), true);
            output.println("Enter your username: ");

            //start reading the incoming messages
            if((username = input.readLine()).compareTo("") != 0){
                myClient = new Client(username);
                allClients.add(myClient);
                System.out.println(username + " just hopped in!!!");
                output.println("Welcome " + username + ". ");

                promptChatRoomLoop();
                parseMessage();
            } else {
                output.println("No username input found.");
            }
        } catch (Exception e){
            terminateClient();
        }
    }

    public void chatRoomJoin(ChatRoom myChatRoom) {
        clientChatRoom = myChatRoom;
        if(clientChatRoom.addClientToRoom(this)){
            output.println("Joined Chat Room: "+ clientChatRoom.getRoomName() );
        } else {
            output.println("Cannot Join Chat Room: "+ clientChatRoom.getRoomName() + ". Room is full.");
        }
    }

    // get chat room by index number
    public void chatRoomJoinByIndex(int index) {
        if(index > -1 && index < Server.allChatRooms.size()){
            clientChatRoom = Server.allChatRooms.get(index);
            chatRoomJoin(clientChatRoom);
        } else {
            output.println("Invalid Index\n");
        }
    }

    //  exit from the current chat toom
    public boolean leaveChatRoom(){
        Boolean out = false;
        out =  clientChatRoom.getConnectedClients().remove(this);

        for (int i = 0; i < clientChatRoom.getConnectedClients().size(); i++){
            clientChatRoom.getConnectedClients().get(i).output.println(username + " Left the Chat Room");;
        }
        return out;
    }

//    send the message to the current chat rooom.
    public void postMessageOnChatRoom(String username, String message){
        for (int i = 0; i < clientChatRoom.getConnectedClients().size(); i++){
            clientChatRoom.getConnectedClients().get(i).output.println(username + ": " + message);;
        }
    }

    private void promptChatRoomLoop() {
        if(Server.allChatRooms.size() < 1){
            do {
                output.println("No Existing Chat Rooms. Create one by -createRoom room_name");
                try {
                    commandAndRoomName = input.readLine();
                    addChatRoom(commandAndRoomName);
                } catch (IOException e) {
                    System.out.println(username+" Client Disconnected");
                }
            } while(Server.allChatRooms.size() < 1);
        } else {
            listChatRooms();
        }
    }
    public boolean addChatRoom(String commandAndRoomName){
        boolean out = false;
        myLine = commandAndRoomName.split(" ");
        if(commandAndRoomName.startsWith("-createRoom") && myLine.length > 1){
            String chatRoomNameInput = myLine[1];
            if(!chatRoomAlreadyExists(chatRoomNameInput)) {
                ChatRoom myChatRoom = new ChatRoom(chatRoomNameInput); // taking the roomName
                Server.allChatRooms.add(myChatRoom);
                this.output.println(myChatRoom.getRoomName() + " Room Created.");
                chatRoomJoin(myChatRoom);
            } else {
                this.output.println(chatRoomNameInput + " Already Exists.");
            }
            out = true;
        }
        return out;
    }

    //  check if the requested chat room already exists.
    public Boolean chatRoomAlreadyExists(String ChatRoomNameInput){
        boolean out = false;
        for (int i = 0; i < Server.allChatRooms.size() && !out; i++){
            if(Server.allChatRooms.get(i).getRoomName().equals(ChatRoomNameInput)){
                out = true;
            }
        }
        return out;
    }

    private void parseMessage() {

        try {
            while ((currMsg = input.readLine()) != null) {
                if (currMsg.startsWith("-rDetails")) {
                    roomDetails();
                } else if (currMsg.startsWith("-rooms")) {
                    listChatRooms();
                } else if (currMsg.startsWith("-createRoom")) {
                    addChatRoom(currMsg);
                } else if (currMsg.startsWith("-join")) {
                    String secondValue = currMsg.split(" ")[1];
                    int index = Integer.parseInt(secondValue);
                    chatRoomJoinByIndex(index);
                } else if (currMsg.startsWith("-leave")) {
                    leaveChatRoom();
                } else {
                    postMessageOnChatRoom(username, currMsg);
                }
            }
        } catch (IOException e){
            leaveChatRoom();
        }
    }

    private void roomDetails() {
        String out = "All Chat Rooms Status: \n";
        ArrayList<ChatRoom> allRooms = Server.allChatRooms;
        for(int i = 0; i < Server.allChatRooms.size(); i++){
            out = out + "[" + i + "] Room Name: " + allRooms.get(i).getRoomName() + "\n"
                    + "Room Capacity = "+ allRooms.get(i).getConnectedClients().size() +"/5\n"
                    + "Users: "+ allRooms.get(i).getConnectedClients().toString()+"\n\n";
        }
        out = out + "Join a room by -join room_index_number. eg. Join 0";
        this.output.println(out);
    }

    private void listChatRooms(){
        String out = "Available Chat Rooms: \n";
        for(int i = 0; i < Server.allChatRooms.size(); i++){
            out = out + "[" + i + "] " + Server.allChatRooms.get(i).getRoomName() + "\n";
        }
        out = out + "Join a room by -join room_index_number. eg. -join 0\nor create one by -createRoom room_name.";
        this.output.println(out);
    }
    public void terminateClient() {
        try {
            incomingClient.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.username;
    }
}//class
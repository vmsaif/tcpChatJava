
//-----------------------------------------/
// NAME: Saif Mahmud
// VERSION: 10/20/2022
// PURPOSE: Creating a TCP base server based java chat app where users can create separate rooms and chat.
//-----------------------------------------/
import java.util.ArrayList;

public class ChatRoom {

    private String chatRoomName;
    private ArrayList<ServerConnectionManager> connectedClients;
    private final int MAX_ROOM_SIZE = 5;
    public ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        connectedClients = new ArrayList<>();
    }

    public String getRoomName() {
        return chatRoomName;
    }

    public boolean addClientToRoom(ServerConnectionManager client){
        boolean out = false;
        if(connectedClients.size() < MAX_ROOM_SIZE){
            connectedClients.add(client);
            out = true;
        } else {
            // room full
        }
        return out;
    }

    protected ArrayList<ServerConnectionManager> getConnectedClients(){
        return connectedClients;
    }

    protected int getMAX_ROOM_SIZE(){
        return MAX_ROOM_SIZE;
    }

    public String toString() {
        return super.toString();
    }
}

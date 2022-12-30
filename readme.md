# TCP Chat Application
This chat application allows clients to connect to a server and participate in chat rooms.

## Features
1. View a list of existing chat rooms
2. View the number and list of connected users for each room
3. Join existing chat rooms if there is capacity
4. Create chat rooms
5. Send messages to chat rooms
6. Leave a chat room

## How to Run

### Compile

From your terminal in the root directory of this project,

    javac src\*.java

First, Start the server by running the Server class.
    
    java -cp src\ Server

Second, Start the client by running the Client class.

    java -cp src\ Client

Follow the prompts in the client interface to connect to the server and participate in chat rooms.

## What I learned
1. How to use sockets and TCP connections to create a client-server application in Java.
2. How to send and receive data over a network connection.
3. How to design and implement a simple chat application with multiple chat rooms.
4. The importance of thread safety when working with concurrent connections.
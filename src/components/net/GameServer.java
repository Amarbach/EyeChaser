/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author Artur
 */
public class GameServer extends NetCommunicator {

    private ServerSocket serverSocket;
    
    public GameServer(){
        super();
    }

    @Override
    public boolean startConnection() throws IOException {
        isConnectionOpened = true;
        serverSocket = new ServerSocket(8888);
        try{
        clientSocket = serverSocket.accept();
        } catch(SocketException e){
            return false;
        }
        super.startConnection();
        return true;
    }

    @Override
    public void close() {
        super.close();
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException e) {
        }
    }
}

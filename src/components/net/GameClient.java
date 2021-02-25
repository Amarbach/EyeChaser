/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 *
 * @author Artur
 */
public class GameClient extends NetCommunicator {
    
    private String hostIP;
    
    public GameClient(String ip){
        super();
        hostIP = ip;
    }
    
    @Override
    public boolean startConnection() throws IOException{
        isConnectionOpened = true;
        try{
            clientSocket = new Socket(hostIP, 8888);
        }
        catch(ConnectException e)
        {
            return false;
        }
        super.startConnection();
        return true;
    }
}

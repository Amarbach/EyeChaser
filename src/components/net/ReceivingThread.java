/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Artur
 */
public class ReceivingThread extends Thread{
    
    private NetCommunicator communicator;
    private ArrayList<String> inBuffer;
    
    public ReceivingThread(NetCommunicator comm, ArrayList<String> buffer){
        super("Receiveing-Net-Communication");
        communicator = comm;
        inBuffer = buffer;
    }
    
    @Override
    public void run(){
        try {
            boolean isConnected = communicator.startConnection();
            String msg = "";
            while(msg != null && communicator.isRunning() && isConnected){
                msg = communicator.read();
                if (msg != null) inBuffer.add(msg);
            }
        } catch (IOException ex) {
            //Logger.getLogger(ReceivingThread.class.getName()).log(Level.SEVERE, null, ex); 
        } finally {
            communicator.close();
        }
    }
    
}

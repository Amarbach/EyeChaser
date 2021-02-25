/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

import java.util.ArrayList;

/**
 *
 * @author Artur
 */
public class Communicator {
    
    private ArrayList<String> inBuffer;
    private NetCommunicator communicator;
    private ReceivingThread receivingThread;
    
    public Communicator(String ip){
        if (ip == null){
            communicator = new GameServer();
        }
        else{
            communicator = new GameClient(ip);
        }
        inBuffer = new ArrayList<String>();
        
        receivingThread = new ReceivingThread(communicator, inBuffer);
        receivingThread.start();
    }
    
    public void sendMessage(String msg){
        communicator.send(msg);
    }
    
    @Deprecated
    public ArrayList<String> retrieveMessages(){
        ArrayList<String> ret = new ArrayList<String>();
        if (!inBuffer.isEmpty()) {
            inBuffer.forEach((String message) -> {
                ret.add(message.toString());
            });
            inBuffer.clear();            
        }
        return ret;
    }

    public boolean isRunning(){
        return communicator.isRunning();
    }
    
    public boolean isConnectionOpened(){
        return communicator.isConnectionOpened();
    }
    
    public void close(){
        communicator.close();
    }
    
    public Message getProcessedMessage() {
        Message ret = null;
        if (inBuffer != null && !inBuffer.isEmpty()) {
            int loopBorder = inBuffer.size();
            long curMoment = System.currentTimeMillis();
            int latest = 0;
            long latestPing = Long.MAX_VALUE;
            for (int i = 0; i < loopBorder; i++) {
                String[] splitMessage = inBuffer.get(i).split(":");
                if (splitMessage.length >= 13) {
                    long msgMoment = Long.parseLong(splitMessage[14], 16);
                    long ping = curMoment - msgMoment;
                    if (ping < latestPing) {
                        latest = i;
                        latestPing = ping;
                    }
                }
            }
            ret = new Message(inBuffer.get(latest), latestPing);
            inBuffer.clear();
        }
        return ret;
    }
    
    public void clearBuffer(){
        inBuffer.clear();
    }
    
    public void restartConnection(){
        inBuffer.clear();
        receivingThread = new ReceivingThread(communicator, inBuffer);
        receivingThread.start();
    }
}

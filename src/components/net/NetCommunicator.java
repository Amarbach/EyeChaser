/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Artur
 */
abstract class NetCommunicator {
    
    private final int timeoutTime = 25000;
    
    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;

    protected boolean isRunning;
    protected boolean isConnectionOpened;
    
    public boolean startConnection() throws IOException{
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientSocket.setSoTimeout(timeoutTime);
        isRunning = true;
        
        return true;
    }
    
    public String read() throws IOException {
        String msg = "";
        try {
            msg = in.readLine();
        } catch (SocketTimeoutException e) {
            close();
        }
        return msg;
    }
    
    public void send(String msg) {
        System.out.println(msg);
        if (out != null && msg != null && !msg.equals("")) {
            out.println(msg);
        }
    }
    
    public void close(){
        try {
            isRunning = false;

            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }
        } catch (IOException e) {
        }
        isConnectionOpened = false;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public boolean isConnectionOpened(){
        return isConnectionOpened;
    }
}

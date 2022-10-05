/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chupitetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import ui.GamePanel.GameMode;
import ui.MainMenu;

import static utils.Constants.*;

/**
 *
 * @author Samuel Novoa Comesaña <samuel.novoa97@gmail.com>
 */
public class NetworkMgr extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    private final MainMenu mainMenu;
    
    private GameMode gameMode;
    private String ip;
    private int port;
    
    public NetworkMgr(MainMenu mainMenu, GameMode gameMode, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.mainMenu = mainMenu;
        this.gameMode = gameMode;
        
        start();
    }
    
    public void reset(GameMode gameMode, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.gameMode = gameMode;
        
        start();
    }
    
    private void initServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(DEFAULT_TIMEOUT);
        clientSocket = serverSocket.accept();
        
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    private void initClient(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public void send() {
        out.println("asd!");
    }
    
    @Override
    public void run() {
        try {
            if (ip.equals("")) {
                initServer(port);
            } else
                initClient(ip, port);
            
            mainMenu.start(gameMode);
            listen();
        } catch(IOException ex) {
            mainMenu.fail();
            interrupt();
        }
    }
    
    private void listen() {
        while (true) {
            try {
                System.out.println(in.readLine());
            } catch (IOException ex) {
            }
        }
    }
}

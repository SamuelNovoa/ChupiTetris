/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import chupitetris.NetworkMgr;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import ui.GamePanel.GameMode;

import utils.Accessor;

import static utils.Constants.*;

/**
 *
 * @author Samuel Novoa Comesaña <samuel.novoa97@gmail.com>
 */
public class MainMenu extends JFrame implements ActionListener {
    private final JButton singleBtn;
    private final JButton speedBtn;
    private final JButton pvpBtn;
    
    private final JTextField usernameTF;
    private final JTextField ipTF;
    
    private final FixMessage warning;
    
    private String ip;
    private int port;
    
    private boolean waitingConn;
    
    public MainMenu() {
        super();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        ip = "";
        port = 0;
        
        waitingConn = false;
        
        warning = new FixMessage();
        
        LabelPanel title = new LabelPanel("Elige el modo de juego", 26);
        title.setMaximumSize(new Dimension(600, 80));
        
        LabelPanel titleSingle = new LabelPanel("Un jugador", 24);
        titleSingle.setMaximumSize(new Dimension(600, 80));
        
        LabelPanel titleMulti = new LabelPanel("Multijugador", 24);
        titleMulti.setMaximumSize(new Dimension(600, 80));
        
        singleBtn = new JButton("<html><body=\"font-size: 24\">Iniciar juego</body></html>");
        singleBtn.setAlignmentX(CENTER_ALIGNMENT);
        singleBtn.setMaximumSize(new Dimension(250, 60));
        singleBtn.addActionListener(this);
        
        speedBtn = new JButton("<html><body=\"font-size: 24\">Iniciar modo carrera</body></html>");
        speedBtn.setAlignmentX(CENTER_ALIGNMENT);
        speedBtn.setMaximumSize(new Dimension(250, 60));
        speedBtn.addActionListener(this);
        
        pvpBtn = new JButton("<html><body=\"font-size: 24\">Iniciar modo pvp</body></html>");
        pvpBtn.setAlignmentX(CENTER_ALIGNMENT);
        pvpBtn.setMaximumSize(new Dimension(250, 60));
        pvpBtn.addActionListener(this);
        
        add(title);
        add(titleSingle);
        add(singleBtn);
        add(Box.createRigidArea(new Dimension(0, 60)));
        add(titleMulti);
        add(speedBtn);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(pvpBtn);
        
        usernameTF = new JTextField();
        ipTF = new JTextField();
        
        setMinimumSize(new Dimension(600, 600));
        setResizable(true);
        setLocation(0, 0);
        setTitle("Chupitetris");
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void start(GameMode gameMode) {
        warning.dispose();
        
        UI ui = new UI(gameMode, usernameTF.getText());
        new Thread(ui).start();
        dispose();
    }
    
    public void fail() {
        waitingConn = false;
        warning.setText(ip.equals("") ? "¡Nadie se ha conectado! ¿Es que no tienes amigos?" : "Parece que te han dejado en visto...");
        warning.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (waitingConn)
            return;
        
        if (e.getSource() == singleBtn)
            tryStart(GameMode.SINGLE_GAME);
        else if (e.getSource() == speedBtn)
            tryStart(GameMode.SPEED_GAME);
        else if (e.getSource() == pvpBtn)
            tryStart(GameMode.PVP_GAME);
    }
    
    private boolean showDialog(boolean single) {
        Object[] message = {
            "Nombre:", usernameTF,
            "IP (Dejar en blanco si vas a ser host):", ipTF
        };

        int result = JOptionPane.showConfirmDialog(null, single ? Arrays.copyOfRange(message, 0, 2) : message, "", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION)
            return false;
        
        if (usernameTF.getText().length() < 1) {
            JOptionPane.showMessageDialog(null, "¡Introduce un nombre de jugador!");
            return false;
        }
        
        if (!single && !isAddrValid(ipTF.getText())) {
            JOptionPane.showMessageDialog(null, "¡Introduce una IP válida o no introduzcas ninguna!");
            return false;
        }
        
        return true;
    }
    
    private void tryStart(GameMode gameMode) {
        if (!showDialog(gameMode == GameMode.SINGLE_GAME))
            return;

        if (gameMode == GameMode.SINGLE_GAME) {
            start(gameMode);
            return;
        }
        
        if (Accessor.getNetworkMgr() == null)
            Accessor.setNetworkMgr(new NetworkMgr(this, gameMode, ip, port));
        else
            Accessor.getNetworkMgr().reset(gameMode, ip, port);

        waitingConn = true;
        warning.setVisible(true);
        warning.setText(ip.equals("") ? "Esperando a que se una un rival..." : "Intentando conectarse a un anfitrión...");
    }

    private boolean isAddrValid(String addrStr) {
        if (addrStr.equals("")) {
            ip = "";
            port = DEFAULT_PORT;
            return true;
        }
        
        String[] addr = addrStr.split(":");
        if (!isIpValid(addr[0]))
            return false;

        if (addr.length == 1) {
            ip = addr[0];
            port = DEFAULT_PORT;
            return true;
        }
        
        if (addr.length == 2) {
            if (toInteger(addr[1]) < 0 || toInteger(addr[1]) > 65535)
                return false;
            
            ip = addr[0];
            port = toInteger(addr[1]);
            return true;
        }
        
        return false;
    }
    
    private boolean isIpValid(String ipStr) {
        if (ipStr.equals(""))
            return true;
        
        String[] tokens = ipStr.split("\\.");
        if (tokens.length != 4)
            return false;
        
        for (String token : tokens)
                if (toInteger(token) < 0 || toInteger(token) > 255)
                    return false;
        
        return true;
    }

    private int toInteger(String strNum) {
        if (strNum == null) {
            return -1;
        }
        
        int i;
        
        try {
            i = Integer.parseInt(strNum);
        } catch (NumberFormatException ex) {
            i = -1;
        }
        
        return i;
    }
}

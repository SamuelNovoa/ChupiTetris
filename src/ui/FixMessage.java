/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Samuel Novoa Comesaña <samuel.novoa97@gmail.com>
 */
public class FixMessage extends JFrame {
    JLabel label;
    
    public FixMessage() {
        super();

        label = new JLabel();
        add(label);
        
        setMinimumSize(new Dimension(400, 200));
        setResizable(false);
        setLocation(0, 0);
        setTitle("Aviso");
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(false);
    }
    
    public void setText(String text) {
        label.setText("<html><body style=\"font-size: 16px\">" + text + "</body></html>");
    }
}

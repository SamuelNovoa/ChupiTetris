/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import ui.GamePanel.GameMode;

import utils.Accessor;

import static utils.Constants.*;

/**
 * Clase principal de la interfaz. Crea los paneles, inicializa los accessor y gestiona los eventos
 * de teclado y redimensionado.
 * 
 * @author Samuel Novoa Comesaña
 */
public class UI extends JFrame implements KeyListener, ComponentListener, Runnable {
    ScorePanel scorePanel;
    GamePanel gamePanel;
    FiguresPanel figuresPanel;
    
    /**
     * Constructor de la interfaz.
     * 
     * @param gameMode Modo de juego
     * @param username Nombre del jugador
     */
    public UI(GameMode gameMode, String username) {
        super();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        
        scorePanel = new ScorePanel(gameMode);
        gamePanel = new GamePanel(gameMode);
        figuresPanel = new FiguresPanel(gameMode);
        
        add(scorePanel);
        add(gamePanel);
        add(figuresPanel);
        
        Accessor.setScorePanel(scorePanel);
        Accessor.setGamePanel(gamePanel);
        Accessor.setFiguresPanel(figuresPanel);
        
        setMinimumSize(new Dimension(RESX * CELL_SIZE + 2 * SCORE_WIDTH, RESY * CELL_SIZE));
        setResizable(true);
        setLocation(0, 0);
        setTitle("Chupitetris");
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(this);
        addComponentListener(this);
    }
    
    /**
     * Método para finalizar el juego. Reinicia todos los datos y continúa con una nueva partida.
     */
    public void gameOver() {
        scorePanel.reset();
        gamePanel.reset();
        figuresPanel.reset();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.keyPressed(e.getKeyCode());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int newGameWidth = Math.round(((float)RESX / (float)RESY) * getHeight());
        
        gamePanel.recalcSize(newGameWidth);
        scorePanel.recalcSize(newGameWidth);
        figuresPanel.recalcSize(newGameWidth);
        revalidate();
    }

    /**
     * Método para arrancar el juego. Inicia su propio hilo.
     */
    @Override
    public void run() {
        Accessor.setUI(this);
        gamePanel.run();
    }
    
    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}

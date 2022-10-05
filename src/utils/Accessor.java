/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import chupitetris.NetworkMgr;
import ui.GamePanel;
import ui.FiguresPanel;
import ui.ScorePanel;
import ui.UI;

/**
 * Clase destinada a almacenar los elementos globales del programa y ofrecer una
 * interfaz estáticca para acceder a ellos.
 *
 * @author Samuel Novoa Comesaña
 */
public class Accessor {
    private static UI ui = null;
    private static ScorePanel scorePanel = null;
    private static GamePanel gamePanel = null;
    private static FiguresPanel figuresPanel = null;
    private static NetworkMgr networkMgr = null;

    /**
     * Método para obtener el tablero.
     *
     * @return El panel de juego
     */
    public static GamePanel getGamePanel() {
        return gamePanel;
    }
    
    /**
     * Método para establecer el tablero. Se llama una única vez al inicio del
     * programa.
     *
     * @param gamePanel Tablero a almacenar
     */
    public static void setGamePanel(GamePanel gamePanel) {
        Accessor.gamePanel = gamePanel;
    }

    /**
     * Método para obtener el panel de figuras (Derecha).
     * 
     * @return El panel de figuras
     */
    public static FiguresPanel getFiguresPanel() {
        return figuresPanel;
    }

    /**
     * Método para establecer el panel de figuras (Derecha).
     * 
     * @param infoPanel Panel de figuras a almacenar
     */
    public static void setFiguresPanel(FiguresPanel infoPanel) {
        Accessor.figuresPanel = infoPanel;
    }

    /**
     * Método para obtener el panel de puntuaciones
     * 
     * @return El panel de puntuaciones
     */
    public static ScorePanel getScorePanel() {
        return scorePanel;
    }

    /**
     * Método para establecer el panel de puntuaciones.
     * 
     * @param scorePanel Panel de puntuaciones a establecer
     */
    public static void setScorePanel(ScorePanel scorePanel) {
        Accessor.scorePanel = scorePanel;
    }

    /**
     * Método para obtener la interfaz.
     * 
     * @return La interfaz
     */
    public static UI getUI() {
        return ui;
    }

    /**
     * Método para establecer la interfaz.
     * @param ui Interfaz a almacenar
     */
    public static void setUI(UI ui) {
        Accessor.ui = ui;
    }

    public static NetworkMgr getNetworkMgr() {
        return networkMgr;
    }

    public static void setNetworkMgr(NetworkMgr networkMgr) {
        Accessor.networkMgr = networkMgr;
    }
}

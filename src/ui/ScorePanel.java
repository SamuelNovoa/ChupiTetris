/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Box;

import chupitetris.FigureType;
import chupitetris.PlayerScore;
import ui.GamePanel.GameMode;

import utils.Accessor;

import static utils.Constants.*;

/**
 * Clase que modela el panel de puntuaciones. Mantiene los datos de puntuación, y gestiona
 * la pieza en hold.
 * 
 * @author Samuel Novoa Comesaña
 */
public class ScorePanel extends JPanel {
    private int level;
    private int rowCleared;
    
    private int score;
    private int foreignScore;
    private int maxScore;
    
    private final FigurePanel holdPanel;
    
    private final LabelPanel scorePanel;
    private final LabelPanel maxScorePanel;
    private final LabelPanel levelPanel;
    private final LabelPanel rowsPanel;
    
    private final GameMode gameMode;
    
    /**
     * Constructor del panel de puntuaciones
     * 
     * @param gameMode Modo de juego
     */
    public ScorePanel(GameMode gameMode) {
        level = 0;
        rowCleared = 0;
        
        this.gameMode = gameMode;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        readMaxScore();
        
        holdPanel = new FigurePanel(FigureType.NONE, true);
        
        scorePanel = new LabelPanel("Puntuación", String.valueOf(score));
        maxScorePanel = new LabelPanel("Puntuación máxima", String.valueOf(maxScore));
        levelPanel = new LabelPanel("Nivel", String.valueOf(level));
        rowsPanel = new LabelPanel("Líneas", String.valueOf(rowCleared));
        
        add(Box.createRigidArea(new Dimension(0, 40))); // Paso de rallarme a poner los espacios bien xd
        add(holdPanel);
        add(Box.createRigidArea(new Dimension(0, 60)));
        add(scorePanel);
        add(maxScorePanel);
        add(levelPanel);
        add(rowsPanel);
        
        setMinimumSize(new Dimension(SCORE_WIDTH, RESY * CELL_SIZE));
        setMaximumSize(new Dimension(SCORE_WIDTH, RESY * CELL_SIZE));
        
        
    }
    
    /**
     * Método para obtener la puntuación actual
     * 
     * @return 
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Método para sumar a la puntuación actual.
     * 
     * @param score Puntuación a sumar
     */
    synchronized public void addScore(int score) {
        this.score += score;

        scorePanel.setValue(this.score);
    }
    
    /**
     * Método para sumar un número de filas limpiadas.
     * @param rowCleared Número de filas limpiadas
     */
    public void addRowCleared(int rowCleared) {
        this.rowCleared += rowCleared;
        if (this.rowCleared > 9) { // Cada 10 filas limpiadas, aumentamos el power
            this.rowCleared -= 10;
            level++;
            levelPanel.setValue(level);
        }
        
        rowsPanel.setValue(rowCleared);
    }

    /**
     * Método para obtener el nivel.
     * 
     * @return Nivel actual
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Método para obtener el récord de puntuación.
     * 
     * @return La puntuación máxima almacenada
     */
    public int getMaxScore() {
        return maxScore;
    }
    
    /**
     * Método para establecer el récord de puntuación.
     * 
     * @param maxScore La nueva puntuación máxima
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
        maxScorePanel.setValue(String.valueOf(maxScore));
    }
    
    /**
     * Método para añadir una figura al panel de hold.
     * 
     * @param figure La figura a añadir al panel de hold
     */
    public void setHoldFigure(FigureType figure) {
        holdPanel.setFigure(figure);
        revalidate();
    }
    
    /**
     * Método para reiniciar el panel.
     */
    public void reset() {
        saveMaxScore();
        
        score = 0;
        rowCleared = 0;
        level = 0;
        
        scorePanel.setValue(score);
        rowsPanel.setValue(rowCleared);
        levelPanel.setValue(level);
        
        holdPanel.setFigure(FigureType.NONE);
    }
    
    /**
     * Método para recalcular el tamaño del panel cuando se redimensiona.
     * @param newGameWidth Nuevo ancho para el panel de juego
     */
    public void recalcSize(int newGameWidth) {
        setMinimumSize(new Dimension((int)((getParent().getWidth() - newGameWidth) / 2.0f), getParent().getHeight()));
        setMaximumSize(new Dimension((int)((getParent().getWidth() - newGameWidth) / 2.0f), getParent().getHeight()));
        revalidate();
    }
    
    /**
     * Método para leer de disco la puntuación récord. Esta puntuación se guarda en el archivo "./score".
     */
    private void readMaxScore() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("score"), "utf-8"));
            maxScore = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            maxScore = 0;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Método para almacenar en disco la puntuación record. Se guarda en el archivo "./score".
     */
    private void saveMaxScore() {
        Writer writer = null;
        
        try {
            if (score > maxScore) {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("score"), "utf-8"));

                writer.write(String.valueOf(score));
                Accessor.getScorePanel().setMaxScore(score);
            }
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}

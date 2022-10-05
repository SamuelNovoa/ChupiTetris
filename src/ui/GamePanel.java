/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.Color;

import chupitetris.Cell;
import chupitetris.Figure;
import chupitetris.Figure.MoveResult;
import chupitetris.FigureType;

import utils.Accessor;
import utils.Tools;

import static utils.Constants.*;

/**
 * Clase que modela el tablero. Mantiene la lista de celdas, y maneja los eventos de teclado.
 *
 * @author Samuel Novoa Comesaña
 */
public class GamePanel extends JPanel {
    private Figure figActive;
    private FigureType figHold;
    private boolean pause;
    private boolean holdCd;
    
    private final Cell[][] cells;
    private final GameMode gameMode;
    
    public enum GameMode {
        SINGLE_GAME,
        SPEED_GAME,
        PVP_GAME
    }

    /**
     * Constructor del tablero. Inicializa las celdas y lanza la interfaz
     * gráfica.
     * 
     * @param gameMode Modo de juego
     */
    public GamePanel(GameMode gameMode) {
        super();

        figActive = null;
        figHold = null;
        cells = new Cell[RESX][RESY];
        holdCd = false;
        
        pause = gameMode != GameMode.SINGLE_GAME;

        this.gameMode = gameMode;
        
        setBackground(Color.BLACK);
        setLayout(new GridLayout(RESY, RESX, 1, 1));
        setPreferredSize(new Dimension(RESX * CELL_SIZE, RESY * CELL_SIZE));

        for (byte i = 0; i < RESY; i++) {
            for (byte j = 0; j < RESX; j++) {
                cells[j][i] = new Cell(j, i);
                add(cells[j][i]);
            }
        }
    }

    /**
     * Método que arranca el juego y mantiene el bucle de actualización.
     */
    public void run() {
        figActive = new Figure(FigureType.getRandomFigureType());

        while (true) {
            if (figActive == null)
                figActive = new Figure(FigureType.getRandomFigureType());

            Tools.wait(Tools.getDiff());

            try {
                if (!pause)
                    update();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Método para obtener una celda dada su posición.
     *
     * @param pos Coordenadas de la celda a recuperar
     * @return La celda que ocupa esa posición
     */
    public Cell getCell(byte[] pos) {
        if (pos[0] < 0 || pos[0] >= RESX) {
            return null;
        }

        if (pos[1] < 0 || pos[1] >= RESY) {
            return null;
        }

        return cells[pos[0]][pos[1]];
    }

    /**
     * Evento lanzado cuando se pulsa una tecla.
     * 
     * @param keyCode Información sobre la tecla pulsada
     */
    public void keyPressed(int keyCode) {
        if (figActive == null) {
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (!pause)
                    figActive.move(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                if (!pause)
                    figActive.move(1, 0);
                break;
            case KeyEvent.VK_DOWN:
                if (!pause)
                    if (figActive.move(0, 1) == MoveResult.MOVE_OK) {
                        Accessor.getScorePanel().addScore(Accessor.getScorePanel().getLevel() + 1);
                }
                break;
            case KeyEvent.VK_UP:
                if (!pause)
                    figActive.rotate();
                break;
            case KeyEvent.VK_SPACE:
                if (!pause)
                    Accessor.getScorePanel().addScore(figActive.hardDrop() * (Accessor.getScorePanel().getLevel() + 1));
                break;
            case KeyEvent.VK_ESCAPE:
                Accessor.getUI().gameOver();
                break;
            case KeyEvent.VK_T:
                pause = !pause;
                break;
            case KeyEvent.VK_C:
                if (gameMode != GameMode.SINGLE_GAME)
                    Accessor.getNetworkMgr().send();
                if (!pause)
                    changeHold();
                break;
            default:
                break;
        }
    }
    
    /**
     * Método llamado al completar una figura. Limpiará las líneas completas y
     * creará una figura nueva.
     */
    synchronized public void figureComplete() {
        Cell[][] cellsT = Tools.trasposeCells(cells);
        byte rowCleared = 0;
        holdCd = false;
        
        figActive.lockCells();

        for (int i = 0; i < RESY; i++) {
            Cell[] row = cellsT[i];
            if (isComplete(row)) {
                clearRow(row);
                fallCells(i);
                i--;
                rowCleared++;
            }
        }

        ScorePanel scorePanel = Accessor.getScorePanel();
        switch (rowCleared) {
            case 0:
                break;
            case 1:
                scorePanel.addScore(100 * (scorePanel.getLevel() + 1));
                break;
            case 2:
                scorePanel.addScore(300 * (scorePanel.getLevel() + 1));
                break;
            case 3:
                scorePanel.addScore(500 * (scorePanel.getLevel() + 1));
                break;
            default:
                scorePanel.addScore(800 * (scorePanel.getLevel() + 1));
                break;
        }

        scorePanel.addRowCleared(rowCleared);
        figActive = new Figure(Accessor.getFiguresPanel().getNextFigure());
    }
    
    /**
     * Método para reiniciar el panel de juego
     */
    public void reset() {
        figActive = null;
        figHold = null;
        holdCd = false;
        
        for (Cell[] row : cells)
            for (Cell cell : row)
                cell.clear();
    }
    
    /**
     * Método para recalcular el tamaño del panel cuando se redimensiona.
     * @param newGameWidth Nuevo ancho para el panel de juego
     */
    public void recalcSize(int newGameWidth) {
        setMinimumSize(new Dimension(newGameWidth, getParent().getHeight()));
        setMaximumSize(new Dimension(newGameWidth, getParent().getHeight()));
        revalidate();
    }

    /**
     * Método para comprobar si una línea está completa.
     *
     * @param row Línea a comprobar
     * @return True en caso esté completa, false en caso contrario
     */
    private boolean isComplete(Cell[] row) {
        for (Cell cell : row) {
            if (!cell.isLocked()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Método para limpiar una línea.
     *
     * @param row Línea a limpiar
     * @param rowNum Número de la línea
     */
    private void clearRow(Cell[] row) {
        for (Cell cell : row) {
            cell.clear();
        }
    }

    /**
     * Método para hacer caer las celdas superiores a una línea dada.
     *
     * @param rowNum Número de la línea a partir de la cual deberá hacer caer
     * las celdas
     */
    private void fallCells(int rowNum) {
        Cell[][] cellsT = Tools.trasposeCells(cells);
        for (int i = rowNum - 1; i > 0; i--) {
            Cell[] cellsRow = cellsT[i];
            for (int j = 0; j < cellsRow.length; j++) {
                Cell cell = cellsT[i][j];
                if (!cell.isLocked()) {
                    continue;
                }

                if (i < RESY - 1) {
                    cellsT[i + 1][j].paint(cell.getBackground());
                    cellsT[i + 1][j].lock();
                }
                cell.clear();
            }
        }
    }
    
    /**
     * Método para intercambiar la pieza activa y en hold.
     */
    private void changeHold() {
        if (holdCd)
            return;
        
        figActive.clear();
        FigureType tempHold = figActive.getType();
        
        if (figHold == null)
            figActive = new Figure(Accessor.getFiguresPanel().getNextFigure());
        else
            figActive = new Figure(figHold);
        
        figHold = tempHold;
        Accessor.getScorePanel().setHoldFigure(figHold);
        
        holdCd = true;
    }
    
    /**
     * Método para actualizar el panel de juego.
     */
    private void update() {
        if (figActive.update() == MoveResult.MOVE_COMPLETE) {
            Tools.wait(LOCK_TIME - Tools.getDiff()); // Antes de fijar las piezas, damos tiempo extra para compensar la disminución de diff por nivel
            if (figActive.update() == MoveResult.MOVE_COMPLETE)
                figureComplete();
        }
    }
}

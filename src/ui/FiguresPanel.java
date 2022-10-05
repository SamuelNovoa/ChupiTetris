/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.LinkedList;
import java.util.Queue;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.Box;

import chupitetris.FigureType;
import ui.GamePanel.GameMode;

import static utils.Constants.*;

/**
 * Clase que modela el panel de figuras. Mantiene la lista de siguientes figuras y
 * un menú de texto con los controles.
 * 
 * @author Samuel Novoa Comesaña
 */
public class FiguresPanel extends JPanel {
    private final FigurePanel[] figurePanels;
    private final Queue<FigureType> figures;
    private final LabelPanel controls;
    
    private final GameMode gameMode;
    
    /**
     * Constructor del panel de figuras.
     * 
     * @param gameMode Modo de juego
     */
    public FiguresPanel(GameMode gameMode) {
        figurePanels = new FigurePanel[3];
        figures = new LinkedList<>();
        
        this.gameMode = gameMode;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        add(Box.createRigidArea(new Dimension(0, 40)));
        for (byte i = 0; i < 3; i++) {
            FigureType fig = FigureType.getRandomFigureType();
            figures.offer(fig);
            figurePanels[i] = new FigurePanel(fig, false);
            add(figurePanels[i]);
            add(Box.createRigidArea(new Dimension(0, i == 2 ? 100 : 40)));
        }

        controls = new LabelPanel("Controles", CONTROLS_LIST, 18);
       
        add(controls);
        
        setMinimumSize(new Dimension(SCORE_WIDTH, RESY * CELL_SIZE));
        setMaximumSize(new Dimension(SCORE_WIDTH, RESY * CELL_SIZE));
    }
    
    /**
     * Método para obtener la siguiente figura de la pila. Añadirá una nueva tras retirarla.
     * 
     * @return El siguitene FigureType en la lista
     */
    public FigureType getNextFigure() {
        FigureType newFigure = figures.poll(); // Meto cuchillo
        figures.offer(FigureType.getRandomFigureType()); // Saco tripas
        
        Object[] figArr = figures.toArray();
        
        for (int i = 0; i < figurePanels.length; i++)
            figurePanels[i].setFigure((FigureType)figArr[i]); // Actualizamos en la lista de siguientes figuras
            
        revalidate();
        return newFigure;
    }
    
    /**
     * Método para reiniciar el panel de figuras.
     */
    public void reset() {
        figures.clear();
        
        for (FigurePanel figurePanel : figurePanels) {
            FigureType figure = FigureType.getRandomFigureType();
            figurePanel.setFigure(figure);
            figures.offer(figure);
        }
        
        revalidate();
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
}

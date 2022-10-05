/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

import chupitetris.Cell;
import chupitetris.FigureType;

/**
 * Clase que modela los paneles para mostrar una única figura. Se usan para el hold y
 * para la lista de siguientes figuras.
 * 
 * @author Samuel Novoa Comesaña
 */
public class FigurePanel extends JPanel {

    private FigureType figure;
    private Cell[][] cells;
    boolean hold;

    /**
     * Constructor completo del panel de figura.
     * @param figure Tipo de figura que contendrá inicialmente
     * @param hold Define si el panel de hold o uno de los de la lista de siguientes figuras
     */
    public FigurePanel(FigureType figure, boolean hold) {
        super();
        this.hold = hold;
        setFigure(figure);
    }

    /**
     * Método para establecer y actualizar la figura que se representará en el panel.
     * 
     * @param figure Nuevo tipo de figura a mostrar
     */
    public void setFigure(FigureType figure) {
        this.figure = figure;
        cells = new Cell[figure.getSizeX()][figure.getSizeY()];

        setLayout(new GridLayout(figure.getSizeY(), figure.getSizeX())); // Se crea un grid del tamaño de la pieza
        
        Dimension dim = new Dimension(figure.getSizeX() * 70, figure.getSizeY() * 70); // Se le da el tamaño justo para contener la pieza
        
        setPreferredSize(dim); // Que puta pereza con los tamaños de las ventanas en java...
        setMinimumSize(dim);
        setMaximumSize(dim);

        removeAll();

        for (byte i = 0; i < figure.getSizeY(); i++) {
            for (byte j = 0; j < figure.getSizeX(); j++) {
                cells[j][i] = new Cell(j, i, true);
                add(cells[j][i]);
            }
        }

        revalidate();
        renderFigure();
    }

    /**
     * Método para renderizar la figura. Pintará en el panel las celdas que
     * correspondan a la figura de su color.
     */
    private void renderFigure() {
        for (Cell[] row : cells)
            for (Cell cell : row)
                cell.clear();

        if (figure == FigureType.NONE)
            return;
        
        for (byte[] pos : figure.getCoords())
            cells[pos[0] + 1][pos[1] + figure.getSizeY() - 1].paint(figure.getColor()); // Se le suma cosas porque las coordenadas relativas se definen desde el centro, pero aqui es desde una esquina
    }
}

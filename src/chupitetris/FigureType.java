/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chupitetris;

import java.awt.Color;
import java.util.Random;

/**
 * Enumerador que contiene los tipos de piezas del juego, así como las coordenadas
 * relativas que definen su forma (No son más que un conjunto de cuadrados), su color y su tamaño.
 * 
 * @author Samuel Novoa Comesaña
 */
public enum FigureType {
    I(Color.CYAN, new byte[] { 4, 1 }, new byte[][] {
        { -1, 0 },
        { 0, 0 },
        { 1, 0 },
        { 2, 0 }
    }),
    J(Color.BLUE, new byte[] { 3, 2 }, new byte[][] {
        { -1, -1 },
        { -1, 0 },
        { 0, 0 },
        { 1, 0 }
    }),
    O(Color.YELLOW, new byte[] { 2, 2 }, new byte[][] {
        { -1, 0 },
        { 0, 0 },
        { -1, -1 },
        { 0, -1 }
    }),
    L(Color.ORANGE, new byte[] { 3, 2 }, new byte[][] {
        { -1, 0 },
        { 0, 0 },
        { 1, 0 },
        { 1, -1 }
    }),
    S(Color.GREEN, new byte[] { 3, 2 }, new byte[][] {
        { -1, 0 },
        { 0, 0 },
        { 0, -1 },
        { 1, -1 }
    }),
    Z(Color.RED, new byte[] { 3, 2 }, new byte[][] {
        { -1, -1 },
        { 0, -1 },
        { 0, 0 },
        { 1, 0 }
    }),
    T(Color.MAGENTA, new byte[] { 3, 2 }, new byte[][] {
        { -1, 0 },
        { 0, 0 },
        { 0, -1 },
        { 1, 0 },
    }),
    NONE(Color.WHITE, new byte[] { 3, 2 }, new byte[][] {{}});
    
    private final Color color;
    private final byte[] figureSize;
    private final byte[][] coords;
    
    /**
     * Constructor del tipo de figura
     * 
     * @param color Color de la figura
     * @param figureSize Tamaño de la figura [x, y]
     * @param coords Array con las posiciones [x, y] de cada uno de los cuadrados que componen la figura
     */
    FigureType(Color color, byte[] figureSize, byte[][] coords) {
        this.color = color;
        this.figureSize = figureSize;
        this.coords = coords;
    }
    
    /**
     * Método para obtener las coordenadas.
     * 
     * @return Matriz de coordenadas de la figura
     */
    public byte[][] getCoords() {
        return coords;
    }

    /**
     * Método para obtener el color,
     * 
     * @return Color de la figura
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Método para obtener el tamaño de la figura en el eje X.
     * 
     * @return Tamaño de la figura en el eje X
     */
    public byte getSizeX() {
        return figureSize[0];
    }
    
    /**
     * Método para obtener el tamaño de la figura en el eje Y.
     * 
     * @return Tamaño de la figura en el eje Y
     */
    public byte getSizeY() {
        return figureSize[1];
    }
    
    /**
     * Método para obtener un tipo de figura aleatorio.
     * 
     * @return Un tipo de figura aleatorio
     */
    public static FigureType getRandomFigureType() {
        int rnd = new Random().nextInt(7);
        
        return FigureType.values()[rnd];
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chupitetris;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;


/**
 * Clase que modela una celda del tablero. Su objetivo proporcionar una capa adicional
 * de abstracción para no tener que gestionarlas como botones.
 * 
 * @author Samuel Novoa Comesaña
 */
public class Cell extends JLabel {
    private byte[] pos;
    private boolean locked;
    private boolean preview;
    
    /**
     * Constructor de la celda
     * 
     * @param x Posición Y de la celda
     * @param y Posición X de la celda
     */
    public Cell(byte x, byte y) {
        this(x, y, false);
    }
    
    /**
     * Constructor completo de la celda.
     * 
     * @param preview Determina si la celda pertenece al tablero o al menú re previsualización
     * @param x Posición X de la celda
     * @param y Posición Y de la celda
     */
    public Cell(byte x, byte y, boolean preview) {
        super();
        
        pos = new byte[] { x, y };
        locked = false;
        this.preview = preview;

        setBackground(Color.black);
        setOpaque(!preview);
        setEnabled(false);
    }
    
    /**
     * Método para limpiar la celda.
     */
    public void clear() {
        locked = false;
        if (preview)
            setOpaque(false);
        
        setBackground(Color.BLACK);
        setBorder(null);
    }
    
    /**
     * Método para pintar una celda.
     * 
     * @param color Color del que hay que pintarla
     */
    public void paint(Color color) {
        paint(color, false);
    }
    
    /**
     * Método completo para pintar una celda
     * 
     * @param color Color del que hay que pintarla
     * @param ghost Si es una celda de una pieza fantasma
     */
    public void paint(Color color, boolean ghost) {
        setOpaque(true);
        setBorder(new LineBorder(getGhostColor(color), 2, true));

        if (preview)
            setBorder(new LineBorder(Color.GRAY, 1, true));
        else
            setBorder(new LineBorder(getGhostColor(color), 2, true));
        
        if (!ghost)
            setBackground(color);
    }
    
    /**
     * Método para comprobar si una celda está bloqueada.
     * 
     * @return True en caso esté bloqueada, false en caso contrario
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Método para bloquear la celda.
     */
    public void lock() {
        locked = true;
    }
    
    /**
     * Método para obtener la posición de la celda.
     * 
     * @return La posición de la celda
     */
    public byte[] getPos() {
        return pos;
    }
    
    /**
     * Método para obtener una versión mas clarita de un color, para usar en el borde de las celdas.
     * 
     * @param color Color de la celda
     * @return Versión clareada del color dado
     */
    private Color getGhostColor(Color color) {
        int red = Math.min(255, color.getRed() == 0 ? 150 : color.getRed() * 20); // Matessss
        int green = Math.min(255, color.getGreen() == 0 ? 150 : color.getGreen() * 20);
        int blue = Math.min(255, color.getBlue() == 0 ? 180 : color.getBlue() * 20);

        return preview ? Color.WHITE : new Color(red, green, blue);
    }
}

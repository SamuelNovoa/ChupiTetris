/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chupitetris;

import utils.*;

import static utils.Constants.*;

/**
 * Clase que modela una figura completa. Su objetivo es proporcionar una capa de abstracción
 * adicional para aplicar movimientos sobe la figura completa y no las celdas que la componen.
 * 
 * @author Samuel Novoa Comesaña
 */
public class Figure {
    private byte[] pos;
    private final FigureType type;
    private double rotation;
    private byte[][] compCells;
    private byte[][] ghostCells;
    
    /**
     * Enumerador con los posibles resultados de intentar realizar un movimiento
     */
    public enum MoveResult {
        MOVE_OK, // Movimiento realizado correctamente
        MOVE_FAIL, // No se pudo realizar el movimiento
        MOVE_COMPLETE, // Es el último movimiento hacia abajo que se puede hacer
    }

    /**
     * Constructor de la figura
     * 
     * @param type FigureType Typo de figura
     */
    public Figure(FigureType type) {
        this.type = type;
        
        pos = new byte[] {4, 1};
        rotation = 2 * Math.PI;
        
        render();
    }

    /**
     * Método para obtener el tipo de figura.
     * 
     * @return Tipo de figura
     */
    public FigureType getType() {
        return type;
    }
    
    /**
     * Método que actualiza periódicamente la figura.
     * 
     * @return True en caso el movimiento se de adecuadamente, false en caso contrario
     */
    public MoveResult update() {
        return moveInternal(0, 1);
    }
    
    /**
     * Método para mover una figura.
     * 
     * @param x Movimiento en la dirección X (Derecha positivo)
     * @param y Movimiento en la dirección Y (Abajo positivo)
     * @return El resultado del movimiento
     */
    public MoveResult move(int x, int y) {
        MoveResult result = moveInternal(x, y);
        if (result == MoveResult.MOVE_COMPLETE)
            Accessor.getGamePanel().figureComplete();
        
        return result;
    }
    
    /**
    * Método para rotar la figura PI / 2 en sentido antihorario.
    */    
    public void rotate() {
        double attemptedRotation = rotation - Math.PI / 2;
        byte[][] attemptedCells = computePos(pos, attemptedRotation);
        
        if (!isValid(attemptedCells))
            return;
        
        rotation = attemptedRotation;
        render(attemptedCells);
        if (rotation < Math.PI / 2)
            rotation = 2 * Math.PI;    
    }
    
    /**
     * Método para realizar un hard drop.
     * 
     * @return El número de movimientos realizados
     */
    public int hardDrop() {
        int maxMovements = getMaxMovements();
        moveInternal(0, maxMovements);
        Accessor.getGamePanel().figureComplete();
        
        return maxMovements;
    }
    
    /**
     * Método para bloquear las celdas que componen la figura.
     */
    public void lockCells() {
        lockCells(compCells);
    }
    
    /**
     * Método para limpiar una figura, reseteando las celdas que la componen
     */
    public void clear() {
        if (compCells != null) {
            for (byte[] cellPos : compCells) {
                Cell cell = Accessor.getGamePanel().getCell(cellPos);
                if (cell != null)
                    cell.clear();
            }
        }
        
        if (ghostCells != null) {
            for (byte[] cellPos : ghostCells) {
                Cell cell = Accessor.getGamePanel().getCell(cellPos);
                if (cell != null)
                    cell.clear();
            }
        }
    }
    
    /**
     * Método para bloquear las celdas de una figura. Esto ocurre cuándo la figura ha llegado abajo y se coloca de forma fija.
     * @param cells Matriz de celdas de la figura que se debe bloquear
     */
    public void lockCells(byte[][] cells) {
        for (byte[] cellPos : cells) {
            Cell cell = Accessor.getGamePanel().getCell(cellPos);
            if (cell != null)
                cell.lock();
        }
    }
    
    /**
     * Método interno para mover la figura.
     * 
     * @param x componente X del movimiento
     * @param y componente Y del movimiento
     * @return true en caso se pueda mover, false en caso contrario
     */
    synchronized private MoveResult moveInternal(int x, int y) {
        byte[] attemptedPos = new byte[] { (byte)(pos[0] + x), (byte)(pos[1] + y )};
        byte[][] attemptedCells = computePos(attemptedPos, rotation);
        
        if (!isValid(attemptedCells)) {
            if (y == 1)
                return MoveResult.MOVE_COMPLETE;

            return MoveResult.MOVE_FAIL;
        }
        
        pos = attemptedPos;
        render(attemptedCells);
        return MoveResult.MOVE_OK;
    }
    
    /**
     * Sobrecarga del render sin parámetros. Usa la posición actual por defecto.
     */
    private void render() {
        compCells = computePos();
        
        if (!isValid(compCells)) {
            Accessor.getUI().gameOver();
            return;
        }
        
        render(compCells);
    }
    
    /**
     * Método para renderizar la figural. Primero limpiará los botones anteriores, y luego pintará los nuevos.
     * 
     * @param cells Matriz de posiciones donde se colocará la figura
     */
    private void render(byte[][] cells) {
        clear();
        compCells = cells;
        
        ghostCells = Tools.addMatrix(new byte[] { 0, (byte)getMaxMovements() }, compCells);
        for (byte[] cellPos : ghostCells) {
            Cell cell = Accessor.getGamePanel().getCell(cellPos);
            if (cell != null)
                cell.paint(type.getColor(), true);
        }
        
        for (byte[] cellPos : compCells) {
            Cell cell = Accessor.getGamePanel().getCell(cellPos);
            if (cell != null)
                cell.paint(type.getColor());
        }
    }
    
    /**
     * Método para generar la matriz de posiciones en donde se colocará la figura tras aplicarle una transformación.
     * 
     * @param pos Nueva posición de la figura
     * @param rotation Nueva rotación de la figura
     * @return La nueva matriz de posiciones
     */
    private byte[][] computePos(byte[] pos, double rot) {
        byte[][] absPos = type.getCoords();
        
        if (type != FigureType.O) { // Matemagia :D
            byte[][] rotMatrix = new byte[][] {
                { (byte)Math.cos(rot), (byte)(-1.0 * Math.sin(rot)) },
                { (byte)Math.sin(rot), (byte)Math.cos(rot) }
            };
        
            absPos = Tools.multMatrix(absPos, rotMatrix);
        }
        
        absPos = Tools.addMatrix(pos, absPos);
        return absPos;
    }
    
    /**
     * Sobrecarga del computePos sin parámetros. Usa la posición actual por defecto.
     * 
     * @return La actual matriz de posiciones
     */
    private byte[][] computePos() {
        return computePos(pos, rotation);
    }
    
    /**
     * Método para comprobar si una matriz de posiciones es válida para que ahí se coloque la figura.
     * 
     * @param cells Matriz de posiciones donde se pretende colocar la figura
     * @return Retorna true en caso pueda colocarse, false en caso contrario
     */
    private boolean isValid(byte[][] cells) {
        for (byte[] cell : cells) {
            if (cell[0] < 0 || cell[0] >= RESX)
                return false;
            
            if (cell[1] < 0 || cell[1] >= RESY)
                return false;
            
            if (Accessor.getGamePanel().getCell(cell).isLocked())
                return false;
        }
        
        return true;
    }
    
    /**
     * Método para obtener el número máximo de movimientos posibles hacia abajo.
     * 
     * @return Número de movimientos posibles hacia abajo
     */
    private int getMaxMovements() {
        byte maxMovements = 0;
        
        while (isValid(Tools.addMatrix(new byte[] { 0, maxMovements }, compCells)))
            maxMovements++;
        
        return maxMovements - 1;
    }
}
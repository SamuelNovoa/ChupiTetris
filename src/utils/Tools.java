/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import chupitetris.Cell;

import static utils.Constants.*;

/**
 * Clase destinada a ofrecer funciones estáticas globales de carácter técnico.
 * 
 * @author Samuel Novoa Comesaña
 */
public class Tools {
    /**
     * Función que sumará un vector a cada línea de una matriz.
     * 
     * @param lambda Vector a sumar
     * @param matrix Matriz a sumar
     * @return Matriz sumada
     */
    public static byte[][] addMatrix(byte[] lambda, byte[][] matrix) {
        byte[][] result = new byte[matrix.length][matrix[0].length];

        for (byte i = 0; i < matrix.length; i++) {
            result[i][0] = (byte)(matrix[i][0] + lambda[0]);
            result[i][1] = (byte)(matrix[i][1] + lambda[1]);
        }

        return result;
    }
    
    /**
     * Función que realiza el producto matricial de dos matrices.
     * 
     * @param first Primera matriz
     * @param second Segunda matriz
     * @return Matriz multiplicada
     */
    public static byte[][] multMatrix(byte[][] first, byte[][] second) {
        byte[][] result = new byte[first.length][second[0].length];
        
        if (first[0].length != second.length)
            return result;
        
        for (byte i = 0; i < first.length; i++)
            for (byte j = 0; j < second[0].length; j++)
                for (byte k = 0; k < first[0].length; k++)
                    result[i][j] += first[i][k] * second[k][j];
        
        return result;
    }
    
    /**
     * Función para dormir el hilo principal cierto tiempo.
     * 
     * @param milliseconds Tiempo en milisegundos que el hilo principal debe dormir
     */
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * Método para transponer una matriz de celdas.
     * 
     * @param cells Matriz de celdas a transponer
     * @return Matriz de celdas transpuesta
     */
    public static Cell[][] trasposeCells(Cell[][] cells) {
        Cell[][] cellsT = new Cell[RESY][RESX];
        
        for (byte i = 0; i < RESY; i++)
            for (byte j = 0; j < RESX; j++)
                cellsT[i][j] = cells[j][i];
        
        return cellsT;
    }
    
    /**
     * Método que devuelve el tiempo de actualización dependiendo del nivel
     * 
     * @return Tiempo de actualización para un nivel determinado
     */
    public static int getDiff() {
        int diff;
        switch (Accessor.getScorePanel().getLevel()) {
            case 0:
                diff = 1000;
                break;
            case 1:
                diff = 896;
                break;
            case 2:
                diff = 792;
                break;
            case 3:
                diff = 688;
                break;
            case 4:
                diff = 583;
                break;
            case 5:
                diff = 479;
                break;
            case 6:
                diff = 375;
                break;
            case 7:
                diff = 271;
                break;
            case 8:
                diff = 167;
                break;
            case 9:
                diff = 125;
                break;
            case 10:
                diff = 104;
                break;
            case 11:
                diff = 104;
                break;
            case 12:
                diff = 104;
                break;
            case 13:
                diff = 83;
                break;
            case 14:
                diff = 83;
                break;
            case 15:
                diff = 83;
                break;
            case 16:
                diff = 63;
                break;
            case 17:
                diff = 63;
                break;
            case 18:
                diff = 63;
                break;
            case 19:
                diff = 42;
                break;
            case 20:
                diff = 42;
                break;
            case 21:
                diff = 42;
                break;
            case 22:
                diff = 42;
                break;
            case 23:
                diff = 42;
                break;
            case 24:
                diff = 42;
                break;
            case 25:
                diff = 42;
                break;
            case 26:
                diff = 42;
                break;
            case 27:
                diff = 42;
                break;
            case 28:
                diff = 42;
                break;
            default:
                diff = 21;
        }

        return diff;
    }
}

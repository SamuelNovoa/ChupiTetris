/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Clase destinada a almacenar las constantes globales y dar acceso estático e ellas.
 * 
 * @author Samuel Novoa Comesaña
 */
public class Constants {
    final public static byte RESX = 12;
    final public static byte RESY = 20;
    final public static byte CELL_SIZE = 40;
    final public static int SCORE_WIDTH = 400;
    final public static int LOCK_TIME = 1000;
    final public static int DEFAULT_PORT = 8139;
    final public static int DEFAULT_TIMEOUT = 20000;
    
    final public static LinkedList<String> CONTROLS_LIST = new LinkedList<>(Arrays.asList(
        "Mover a la derecha: →",
        "Mover a la izquierda: ←",
        "Rotar: ↑",
        "Soft drop: ↓",
        "Hard drop: ⎵",
        "Hold: C",
        "Pausa: T",
        "Reiniciar: esc"
    ));
}

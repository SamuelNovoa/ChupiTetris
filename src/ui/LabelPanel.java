/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para modelar los output de texto en la interfaz. Esta clase define un par etiqueta - datos.
 * El nombre será fijo, e indicará qué dato se está mostrando, mientras que los datos podrán cambiarse
 * y se actualizarán automáticamente.
 * 
 * @author Samuel Novoa Comesaña
 */
public class LabelPanel extends JPanel {
    private final JLabel label;
    private final String name;
    private final int fontSize;
    
    private LinkedList<String> values;
    
    /**
     * Constructor del panel de texto.
     * 
     * @param name Etiqueta a mostrar
     */
    public LabelPanel(String name) {
        this(name, new LinkedList<String>(), 16);
    }
    
    public LabelPanel(String name, int fontSize) {
        this(name, new LinkedList<String>(), fontSize);
    }
    
    /**
     * Constructor del panel de texto.
     * 
     * @param name Etiqueta a mostrar
     * @param value Un único valor a mostrar
     */
    public LabelPanel(String name, String value) {
        this(name, new LinkedList<>(Arrays.asList(value)), 16);
    }
    
    /**
     * Constructor del panel de texto.
     * 
     * @param name Etiqueta a mostrar
     * @param values Una lista de valores a mostrar
     */
    public LabelPanel(String name, LinkedList values) {
        this(name, values, 16);
    }
    
    /**
     * Constructor completo del panel de texto
     * 
     * @param name Etiqueta a mostrar
     * @param values Una lista de valores a mostrar
     * @param fontSize Tamaño del texto
     */
    public LabelPanel(String name, LinkedList values, int fontSize) {
        super();
        
        this.name = name;
        this.fontSize = fontSize;
        this.values = values;
        label = new JLabel();

        updateText();
        add(label);
    }
    
    /**
     * Método para establecer un único entero.
     * 
     * @param value Entero a establecer
     */
    public void setValue(int value) {
        setValue(String.valueOf(value));
    }
    
    /**
     * Método para establecer una única cadena.
     * 
     * @param value Cadena a establecer
     */
    public void setValue(String value) {
        setValues(new LinkedList<>(Arrays.asList(value)));
    }
    
    /**
     * Método para establecer una lista de valores.
     * 
     * @param values Lista de cadenas a establecer
     */
    public void setValues(LinkedList<String> values) {
        this.values = values;
        
        updateText();
    }
    
    /**
     * Método para regenerar el contenido del panel en formato html.
     */
    private void updateText() {
        String str = "<html><body style=\"" + computeStyle() + "\">" + name;

        if (values.size() == 1) {
            str += ": " + values.getFirst();
        } else {
            str += ":<ul>";
                        
            for (String value : values)
                str += "<li>" + value + "</li>";
            
            str += "</ul>";
        }
        
        str += "</body></html>";
        label.setText(str);
    }
    
    /**
     * Método para generar el estilo CSS.
     * 
     * @return Cadena de texto con el CSS
     */
    private String computeStyle() {
        return "font-size: " + fontSize;
    }
}

package com.mycompany.examendi1224_robledo_nacho;

import java.awt.Color;
import javax.swing.JLabel;

public class componente extends JLabel {

    // Constructor con texto por defecto
    public componente() {
        super("Texto predeterminado");
        configurarEstilo();
    }

    // Constructor con texto personalizado
    public componente(String texto) {
        super(texto);
        configurarEstilo();
    }

    // Configura los estilos predeterminados
    private void configurarEstilo() {
        setBackground(new Color(85, 130, 243)); // Fondo azul
        setOpaque(true); // Fondo visible
        setHorizontalAlignment(CENTER); // Centrar el texto
        setForeground(Color.BLACK); // Color de texto predeterminado
    }

    // Cambia el color del texto según el valor ingresado
    public void cambiarColorSegunValor(int valor) {
        if (valor < 5) {
            setForeground(Color.RED); // Rojo si el valor es menor que 5
        } else {
            setForeground(Color.BLACK); // Negro si el valor es mayor o igual a 5
        }
    }
}

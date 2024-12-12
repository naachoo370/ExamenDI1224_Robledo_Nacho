package com.mycompany.examendi1224_robledo_nacho;

import java.awt.Color;
import javax.swing.JLabel;

public class componente extends JLabel {

    public componente() {
        super("Texto predeterminado");
        configurarEstilo();
    }

    public componente(String texto) {
        super(texto);
        configurarEstilo();
    }

    private void configurarEstilo() {
        setBackground(new Color(85, 130, 243)); 
        setOpaque(true); 
        setHorizontalAlignment(CENTER); 
        setForeground(Color.BLACK);
    }

    public void cambiarColorSegunValor(int valor) {
        if (valor < 5) {
            setForeground(Color.RED); 
        } else {
            setForeground(Color.BLACK); 
        }
    }
}

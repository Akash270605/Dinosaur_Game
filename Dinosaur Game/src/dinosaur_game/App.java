/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dinosaur_game;

import javax.swing.*;

public class App {
    public static void main(String args[]) throws Exception{
        int boardWidth= 750;
        int boardHeight= 250;
        
        JFrame frame= new JFrame("Dinosaur Game");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ChromeDinosaur chromeDinosaur= new ChromeDinosaur();
        frame.add(chromeDinosaur);
        frame.pack();
        chromeDinosaur.requestFocus();
        frame.setVisible(true);
    }
}

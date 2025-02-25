/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dinosaur_game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener{
    int boardWidth= 750;
    int boardHeight= 250;
    
    //images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;
        
        Block(int x, int y, int width, int height, Image img){
            this.x= x;
            this.y= y;
            this.width= width;
            this.height= height;
            this.img= img;
        }
    }
    
    //dinosaur
    int dinosaurWidth= 88;
    int dinosaurHeight = 94;
    int dinosaurX=50;
    int dinosaurY= boardHeight- dinosaurHeight;
    
    Block dinosaur;
    
    //cactus
    int cactus1Width= 34;
    int cactus2Width= 69;
    int cactus3Width= 102;
    
    int cactusHeight=70;
    int cactusX= 700;
    int cactusY= boardHeight- cactusHeight;
    ArrayList<Block> cactusArray;
    
    //physics
    int velocityX=-12; //cactus moving left speed
    int velocityY= 0;   //dinosaur jump speed
    int gravity =1;
    
    boolean gameOver =false;
    int score= 0;
    
    Timer gameLoop;
    Timer placeCactusTimer;
    
    public ChromeDinosaur(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);
        
        dinosaurImg= new ImageIcon(getClass().getResource("/img/dino-run.gif")).getImage();
        dinosaurDeadImg= new ImageIcon(getClass().getResource("/img/dino-dead.png")).getImage();
        dinosaurJumpImg= new ImageIcon(getClass().getResource("/img/dino-jump.png")).getImage();
        cactus1Img= new ImageIcon(getClass().getResource("/img/cactus1.png")).getImage();
        cactus2Img= new ImageIcon(getClass().getResource("/img/cactus2.png")).getImage();
        cactus3Img= new ImageIcon(getClass().getResource("/img/cactus3.png")).getImage();
        
        //dinosaur
        dinosaur= new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        
        //cactus
        cactusArray= new ArrayList<Block>();
        
        //game timer
        gameLoop= new Timer(1000/60, this);
        gameLoop.start();
        
        //place cactus timer
        placeCactusTimer= new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }
    
    void placeCactus(){
        if(gameOver){
            return;
        }
        
        double placeCactusChance= Math.random(); //0-0.99999
        if(placeCactusChance > .90){  //10% you get cactus3
            Block cactus =new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance > .70) { //20% you get cactus2
            Block cactus= new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance > .50){ //20% get cactus1
            Block cactus= new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }
        
        if(cactusArray.size() > 10){
            cactusArray.remove(0);
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        //dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height,null);
        
        //cactus
        for(int i=0; i<cactusArray.size(); i++){
            Block cactus= cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
        
        //score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD,32));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: "+ String.valueOf(score), 10 ,35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
    }
    
    public void move(){
        //dinosaur
        velocityY +=gravity;
        dinosaur.y +=velocityY;
        
        if(dinosaur.y > dinosaurY){
            dinosaur.y= dinosaurY;
            velocityY=0;
            dinosaur.img= dinosaurImg;
        }
        
        //cactus
        for(int i=0; i<cactusArray.size(); i++){
            Block cactus= cactusArray.get(i);
            cactus.x += velocityX;
            
            if(collision(dinosaur, cactus)){
                gameOver= true;
                dinosaur.img= dinosaurDeadImg;
            }
        }
        
        //score
        score++;
    }
    
    boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
                a.x +a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
       repaint();
       if(gameOver){
           placeCactusTimer.stop();
           gameLoop.stop();
       }
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
          //  System.out.println("JUMP!");
          if(dinosaur.y == dinosaurY){
              velocityY= -17;
              dinosaur.img= dinosaurJumpImg;
          }
          
          if(gameOver){
              //restart game by resetting conditions
              dinosaur.y= dinosaurY;
              dinosaur.img= dinosaurImg;
              velocityY=0;
              cactusArray.clear();
              score=0;
              gameOver=false;
              gameLoop.start();
              placeCactusTimer.start();
          }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    
}

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class Board extends JPanel {
   private static final int PREF_W = 500;
   private static final int PREF_H = PREF_W;
   List<Rectangle> squares = new ArrayList<Rectangle>();
   List<Oval> circles = new ArrayList<Oval>();

   public void addSquare(int x, int y, int width, int height) {
      Rectangle rect = new Rectangle(x, y, width, height);
      squares.add(rect);
   }

   public void addCircle(int x, int y, int width, int height, Color colour){
      Oval circ = new Oval(x,y,width,height,colour);
      circles.add(circ);
   }

   public void updateCircle(int index, int x, int y) {
      circles.get(index).updateX(x);
      circles.get(index).updateY(y);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      for (Rectangle rect : squares) {
         g2.setColor(new Color(33,199,205));
         g2.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
      }    
      for (Oval circle : circles){
         g2.setColor(circle.getColour());
         g2.fillOval(circle.getX(),circle.getY(),circle.getWidth(),circle.getHeight());
      }
   }

}
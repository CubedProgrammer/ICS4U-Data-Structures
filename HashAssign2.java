import structs.HashMap_03_29;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * HashAssign2.java
 * @author Kevin Zhang
 */
public class HashAssign2 {

    /**
     * Blog containing emotions
     */
    public static final class Blog {

        /**
         * Love/hate
         */
        private int lh;

        /**
         * Happiness/sadness
         */
        private int hs;

        /**
         * Excitement/boredom
         */
        private int eb;

        /**
         * Constructor for blog post emotions
         * @param lh Love/hate
         * @param hs Happiness/sadness
         * @param eb Excitement/boredom
         */
        public Blog(int lh,int hs,int eb) {

            this.lh=lh;
            this.hs=hs;
            this.eb=eb;

        }

    }

    /**
     * Displays when clicked
     * @author Kevin Zhang
     */
    public static final class ClickDisplay {

        /**
         * Colour of the display
         */
        private Color color;

        /**
         * X-coord of the click
         */
        private int x;

        /**
         * Y-coord of the click
         */
        private int y;

        /**
         * The fade of the colour
         */
        private double fade;

        /**
         * Constructs the object
         * @param color The color to display
         * @param x The x coordinate
         * @param y The y coordinate
         * @param fade The fade
         */
        public ClickDisplay(Color color,int x,int y,double fade) {

            this.color=color;
            this.x=x;
            this.y=y;
            this.fade=fade;

        }

    }

    /**
     * Class for handling mouse clicks
     * @author Kevin Zhang
     */
    public static final class MouseHandler extends MouseAdapter {

        private MapDisplay display;

        public MouseHandler(MapDisplay display) {
            this.display=display;
        }

        public void mousePressed(MouseEvent evt) {
            display.click(evt.getX(),evt.getY());
        }

    }

    /**
     * Displays the map
     * @author Kevin Zhang
     */
    public static final class MapDisplay extends Canvas {

        /**
         * Map of the city
         */
        private BufferedImage map;

        /**
         * True if the game is running
         */
        private boolean running;

        /**
         * Clicks the user made
         */
        private ArrayList<ClickDisplay> clicks;

        /**
         * Constructor for the class
         */
        public MapDisplay()throws IOException {

            this.map= ImageIO.read(getClass().getResource("/windsor.png"));
            this.running=true;
            this.clicks=new ArrayList<ClickDisplay>();

        }

        /**
         * Paints the window
         * @param g Some graphics object
         */
        public void paint(Graphics2D g) {

            g.drawImage(map,0,0,null);

            Iterator<ClickDisplay> it=clicks.iterator();
            ClickDisplay display;

            while(it.hasNext()) {

                display=it.next();
                g.setColor(display.color);
                g.setComposite(AlphaComposite.SrcOver.derive((float)display.fade));
                g.fillOval(display.x-10,display.y-10,20,20);
                display.fade-=0.03333333333333333;

            }

        }

        /**
         * Renders the window
         */
        public void render() {

            BufferStrategy bs=getBufferStrategy();
            Graphics g;

            if(bs==null) {
                createBufferStrategy(3);
                return;
            }

            g=bs.getDrawGraphics();
            paint((Graphics2D)g);
            g.dispose();
            bs.show();

        }

        /**
         * Runs the window
         */
        public void run() {

            long last=System.nanoTime();
            long now;
            double passed=0;

            while(running) {

                now=System.nanoTime();
                passed+=(now-last)/1000000.0;
                last=now;

                if(passed>16.6666667) {
                    render();
                    passed-=16.6666667;
                }

            }

        }

        /**
         * Handles a click on the map
         * @param x The x coord of the click
         * @param y The y coord of the click
         */
        public void click(int x,int y) {
        }

    }

    /**
     * Converts an emotion value to a colour value
     * @return A colour value that is equivalent to that emotion value
     */
    public static final int emotionToColour(int emotion) {
        return(emotion+100)*51/40;
    }

    /**
     * Converts the three emotions to colour
     * @return A colour representing the emotions
     */
    public static final Color emotionsToColour(int lh,int hs,int eb) {
        return new Color(emotionToColour(lh),emotionToColour(hs),emotionToColour(eb));
    }

    public static final void main(String[]args) {

        HashMap_03_29<Integer,Blog>map=new HashMap_03_29<Integer,Blog>();
        Scanner scanner=new Scanner(new BufferedReader(new InputStreamReader(HashAssign2.class.getResourceAsStream("/creeper.txt"))));

        int x,y;
        int lh,hs,eb;

        while(scanner.hasNext()) {

            x=scanner.nextInt();
            y=scanner.nextInt();
            lh=scanner.nextInt();
            hs=scanner.nextInt();
            eb=scanner.nextInt();

            map.add(x<<16|y,new Blog(lh,hs,eb));

        }

        scanner.close();

        try {

            MapDisplay display=new MapDisplay();
            JFrame frame=new JFrame("Blog Emotions");

            display.setSize(800,600);
            display.setPreferredSize(display.getSize());

            frame.add(display);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            display.run();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
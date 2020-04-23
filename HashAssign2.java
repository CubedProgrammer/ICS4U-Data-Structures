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

import static java.lang.Math.sqrt;

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

        /**
         * Map display canvas
         */
        private MapDisplay display;

        /**
         * Makes the mouse handler
         * @param display The map display canvas
         */
        public MouseHandler(MapDisplay display) {
            this.display=display;
        }

        /**
         * {@inheritDoc}
         */
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
         * Blog post locations
         */
        private HashMap_03_29<Integer,Blog>blogs;

        /**
         * Constructor for the class
         */
        public MapDisplay(HashMap_03_29<Integer, Blog>blogs)throws IOException {

            this.map = ImageIO.read(getClass().getResource("/windsor.png"));//gets the picture
            this.running=true;
            this.clicks=new ArrayList<ClickDisplay>();
            this.blogs=blogs;
            //adds the mouse listener
            addMouseListener(new MouseHandler(this));

        }

        /**
         * Paints the window
         * @param g Some graphics object
         */
        public void paint(Graphics2D g) {
            //draws the map
            g.drawImage(map,0,0,null);
            //iterator and display
            Iterator<ClickDisplay> it=clicks.iterator();
            ClickDisplay display;
            //looping through the blogs that needs to be displayed
            while(it.hasNext()) {

                display=it.next();
                //remove ones that have faded out
                if(display.fade<=0) {
                    it.remove();
                    continue;
                }
                //sets the colour and transparency
                g.setColor(display.color);
                g.setComposite(AlphaComposite.SrcOver.derive((float)display.fade));
                g.fillRect(display.x,display.y,1,1);
                //fade the display a little bit
                display.fade-=0.03333333333333333;

            }

        }

        /**
         * Renders the window
         */
        public void render() {
            //gets the buffer strategy
            BufferStrategy bs=getBufferStrategy();
            Graphics g;
            //create a triple buffer if there isn't a buffer strategy
            if(bs==null) {
                createBufferStrategy(3);
                return;
            }

            //get the graphics and draw
            g=bs.getDrawGraphics();
            paint((Graphics2D)g);
            g.dispose();
            bs.show();

        }

        /**
         * Runs the window
         */
        public void run() {
            //time variables
            long last=System.nanoTime();
            long now;
            double passed=0;

            while(running) {
                //gets time and adds onto passed
                now=System.nanoTime();
                passed+=(now-last)/1000000.0;
                last=now;

                //check if enough time has passed and render
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

            Blog blog;//blog variable

            //checking a 21 by 21 square
            for(int i=-10;i<=10;i++) {
                for(int j=-10;j<=10;j++) {
                    //checkes distance
                    if(sqrt(i*i+j*j)<=10) {
                        if(blogs.contains(j+x<<16|i+y)) {
                            blog=blogs.get(j+x<<16|i+y);//get the blog
                            clicks.add(new ClickDisplay(emotionsToColour(blog.lh,blog.hs,blog.eb),x+j,y+i,1));//and add it to the displays
                        }
                    }
                }
            }

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
        //one map for blogs and one map for counting blogs at one logation
        HashMap_03_29<Integer,Blog>map=new HashMap_03_29<Integer,Blog>();
        HashMap_03_29<Integer,Integer>cnts=new HashMap_03_29<Integer,Integer>();
        //and scanner for reading the file
        Scanner scanner=new Scanner(new BufferedReader(new InputStreamReader(HashAssign2.class.getResourceAsStream("/creeper.txt"))));

        //variables needed
        int x,y;
        int lh,hs,eb;
        Blog b;

        while(scanner.hasNext()) {
            //reads from scanner
            x=scanner.nextInt();
            y=scanner.nextInt();
            lh=scanner.nextInt();
            hs=scanner.nextInt();
            eb=scanner.nextInt();
            //add to the map of blogs, will contain total for now
            if(map.contains(x<<16|y)) {
                b=map.get(x<<16|y);
                map.add(x<<16|y,new Blog(b.lh+lh, b.hs+hs, b.eb+eb));
            } else
                map.add(x<<16|y,new Blog(lh,hs,eb));
            //increment the count
            cnts.add(x<<16|y, cnts.get(x<<16|y)==null?1:cnts.get(x<<16|y)+1);

        }
        //iterators and temporary variables for averaging the blogs
        Iterator<HashMap_03_29.KVPair<Integer, Blog>>mit=map.iterator();
        Iterator<HashMap_03_29.KVPair<Integer, Integer>>cit=cnts.iterator();
        HashMap_03_29.KVPair<Integer, Blog>pair;
        Blog v;
        int div;

        //loops through entries
        while(mit.hasNext()) {
            //get the values
            pair=mit.next();
            div=cit.next().getVal();
            //average the values
            v=pair.getVal();
            pair.setVal(new Blog(v.lh/div,v.hs/div,v.eb/div));

        }

        scanner.close();

        try {
            //the canvas and the frame
            MapDisplay display=new MapDisplay(map);
            JFrame frame=new JFrame("Blog Emotions");
            //seting display canvas
            display.setSize(800,600);
            display.setPreferredSize(display.getSize());
            //setting display frame
            frame.add(display);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            //start the display
            display.run();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
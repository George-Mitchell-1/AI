/*My free cell problem:  The problem contains blocks numbered 1 to n, one block for each number.
A state of the problem contains the following:
 - A counter n.
 - Several piles of blocks, each pile contains 0 to n blocks, with each block appearing exactly once.
The successor function will do one of the following things:
  - Take the block on top of some pile, and place it on top of another pile, such that it is placed directly on a smaller numbered block.
  - Take the block on top of some pile and place it on an empty pile.
  - Take the block on top of some pile and throw it away, assuming that the number on the block matches the counter, then decrement the counter.
The problem is solved when the counter is 0, equivalently when there are no blocks left. 
Any valid state could be an initial configuration.*/

//this is messy: midway through implementing graphics
//for now, the few lines that start the jframe are commented out

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
//import java.util.Timer;

import javax.swing.*;
import java.awt.*;

import java.awt.Graphics;

//https://www3.ntu.edu.sg/home/ehchua/programming/java/J4b_CustomGraphics.html
public class FreeCell extends JFrame {
    FrCll cur;// = ;
    boolean lock;
    // Define constants

    // java.awt.event.ActionListener taskPerformer = new java.awt.event.ActionListener() {
    //     public void actionPerformed(java.awt.event.ActionEvent evt) {
    //         repaint();
    //         proceed = true;
    //     }
    // };
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;

    // Constructor to set up the GUI components and event handlers
    // public FreeCell() {
    //     canvas = new DrawCanvas(); // Construct the drawing canvas
    //     canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

    //     // Set the Drawing JPanel as the JFrame's content-pane
    //     Container cp = getContentPane();
    //     cp.add(canvas);
    //     // or "setContentPane(canvas);"

    //     setDefaultCloseOperation(EXIT_ON_CLOSE); // Handle the CLOSE button
    //     pack(); // Either pack() the components; or setSize()
    //     setTitle("......"); // "super" JFrame sets the title
    //     setVisible(true); // "super" JFrame show
    // }

    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {
        private static final long serialVersionUID = 1L;
        // Override paintComponent to perform your own painting
        // @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // paint parent's background
            setBackground(Color.WHITE); // set background color for this JPanel
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(visited.size()), 600, 450);
            // for (int i = 0; i < visited.size(); i++) {
                
            for (int j = 0; j < n; j++) {
                // for (int k = 0; k < visited.get(i).getPiles().get(j).size(); k++) {
                // g.drawRect((640 / (n + 1)) * (j + 1), 35 * (k + 1), 40, 35);
                // g.drawString(Integer.toString(visited.get(i).getPiles().get(j).get(k)),
                // (640 / (n + 1)) * (j + 1), 35 * (k + 1));
                // }
                if (cur != null) {
                System.out.println(cur.getCost());
                System.out.println(cur.getHn());
                System.out.println("counter:"+cur.getCounter());
                if (!cur.getPiles().isEmpty())
                for (int k = 0; k < cur.getPiles().get(j).size(); k++) {
                    g.drawRect((640 / (n + 1)) * (j + 1), 50 * (k + 1), 40, 35);
                    g.drawString(Integer.toString(cur.getPiles().get(j).get(k)), (640 / (n + 1)) * (j + 1),
                            35 * (k + 1));
                }
            }
            }
            // try {
            // timer.wait(250);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // }

        }
    }

    // Graphics g = new Graphics();
    // PCP[] input = {new PCP("MOM","M"),new PCP("O","OMOMO")};
    // referenced https://www.geeksforgeeks.org/arraylist-of-arraylist-in-java/
    int n = 3;
    ArrayList<ArrayList<Integer>> inArr = new ArrayList<ArrayList<Integer>>(n);
    // inArr = {{0},{0},{5,4,3,2,1,6}};
    ArrayList<Integer> pile1init = new ArrayList<Integer>();
    ArrayList<Integer> pile2init = new ArrayList<Integer>();
    ArrayList<Integer> pile3init = new ArrayList<Integer>();

    PriorityQueue<FrCll> fringe = new PriorityQueue<FrCll>(new The_Comparator());
    public ArrayList<FrCll> visited = new ArrayList<FrCll>();

    boolean answer = false;
    boolean addedToFringe = false;
    static boolean proceed = true;
    FrCll lastTmp;

    public static void main(String[] args) {

        FreeCell loop = new FreeCell(); // anti-static
        

        // System.out.println("BFS approach:");
        loop.initialize();
        // SwingUtilities.invokeLater(new Runnable() {
        //     @Override
        //     public void run() {
        //         new FreeCell(); // Let the constructor do the job
        //     }
        // });

        // FrCll test1 = new FrCll();
        // FrCll test2 = new FrCll(test1);
        // System.out.println(test1.equals(test2));
        loop.lastTmp = new FrCll(6, loop.inArr, 0);
        System.out.println("cost: " + loop.theLoop());

        // System.out.println("DFS approach:");
        // loop.initialize();
        // System.out.println("Answer: " + loop.theLoop(false));
        //exit();
    }

    public void initialize() {
        inArr.add(pile1init);
        inArr.add(pile2init);
        inArr.add(pile3init);
        pile1init.add(5);
        pile1init.add(4);
        pile1init.add(3);
        pile1init.add(2);
        pile1init.add(1);
        pile1init.add(6);
        // pile2init.add(8);
        // pile2init.add(7);

        FrCll[] input = { new FrCll(6, inArr, 0) };
        cur = input[0];


        answer = false;
        fringe.clear();
        fringe.add(input[0]);
    }

    public int theLoop() {

        //System.out.println(fringe.comparator());
        while (true) {
            if (proceed) {
            // System.out.println("new loop");
            addedToFringe = false;

            // if (fringe.isEmpty()) {
            // return -1; //FrCll tmp = new FrCll(getNext()); tmp.setCounter(-1); return
            // tmp;
            // }

            FrCll tmp;
            do { //REMOVE THIS CHECK @TODO
                tmp = fringe.poll(); // removes p from fringe
            } while (!lastTmp.isValid(tmp));
            // fringe.clear();
             cur = tmp;
            System.out.println("Goal Test on " + tmp.toString());

            if (populate(tmp)) {
                return tmp.getCost();
            }
            //proceed = false;
            //new Timer(2500,taskPerformer).start();

            lastTmp = new FrCll(tmp);

            //for (int i=0;i<999999;i++){}

            // try {
            //     timer.wait(100);
            // } catch (InterruptedException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }

            //while(!lock){}


        }
    }
        
    }

    public FrCll getNext() { //public FrCll
        //return the best FrCll from heuristic
        return new FrCll();
    }

    public boolean populate(FrCll p) {
        //goal check, return true here, false at end
        if (p.getCounter()==0) {
            return true;
        }

        for (FrCll x : p.nextChoices()) {
            //add next choice to shit
            //System.out.println(x);
            if (!visited.contains(x)) {
               // System.out.println("adding "+x+" to the fringe");
                fringe.add(x);
                visited.add(x);
            }
        }

        return false;
    }
}

class FrCll implements Comparable<FrCll> {
    int n = 3;
    ArrayList<ArrayList<Integer> > inArr = new ArrayList<ArrayList<Integer> >(n);
    ArrayList<Integer> pile1 = new ArrayList<Integer>();
    ArrayList<Integer> pile2 = new ArrayList<Integer>();
    ArrayList<Integer> pile3 = new ArrayList<Integer>();
    
    private int counter;
    private ArrayList<ArrayList<Integer>> piles;
    private int cost;

    public int getPileLastIndex(int p) {
        return getPiles().get(p).size()-1;
    }

    public int compareTo(FrCll f) {
        return getFn()-f.getFn();
    }

    public int getFn() {
        return getHn() + getGn();
    }

    public int getHn() {
        //return counter; //heuristic time WHY ARE THESE THE SAME??
        //int depth = -1;

        //how deep is the highest number
        // for (int i=0;i<n;i++) {
        //     for (int j=0;j<getPiles().get(i).size();j++) {
        //         if (getPiles().get(i).get(j) == counter) {
        //             //System.out.println("counter*2");
        //             return counter + j;
        //         }
        //     }
        // }
        // return counter;

        //max height
        int max = 0;
        for (int i=0;i<n;i++) {
            if (getPiles().get(i).size() > max)
                max = getPiles().get(i).size();
        }
        return max;
        //return depth of key piece + counter
    }

    private int getGn() {
        return cost;
    }

    public ArrayList<FrCll> nextChoices() {
        ArrayList<FrCll> tmp = new ArrayList<FrCll>();
        for (int i=0;i<n;i++) {
            // System.out.println("i="+i);
            if (!getPiles().get(i).isEmpty()) { //if each pile has an item
                if (getPiles().get(i).get(0) == counter) {
                    FrCll tmpFC = remove(i);
                    tmp.add(tmpFC);
                    break;
                }
                for (int j=0;j<n;j++) { //for other piles
                    // System.out.println("j="+j);
                    

                    if (i != j) {
                        if (getPiles().get(i).isEmpty())
                            break;
                        // else
                        //     System.out.println(getPiles().get(i).get(0)+Boolean.toString(getPiles().get(j).isEmpty()));
                        // if (!getPiles().get(j).isEmpty()) {
                        //     System.out.println(getPiles().get(j).get(0));
                        // }
                        if (getPiles().get(j).isEmpty() || (getPiles().get(i).get(0) > getPiles().get(j).get(0))) {//is valid move
                            tmp.add(0,move(i,j));
                        }

                    }
                   
            } 
            // if (!getPiles().get(i).isEmpty()) 

        }
    }
        return tmp;
    }

    public FrCll move(int source,int dest) {
        // System.out.println("moving "+source+" to "+dest);
        FrCll temp = new FrCll(this);
        temp.getPiles().get(source).get(0);
        temp.getPiles().get(dest).add(0,temp.getPiles().get(source).get(0)); //add new one
        temp.getPiles().get(source).remove(temp.getPiles().get(source).get(0)); //remove old one
        temp.setCost(temp.getCost()+1);
        return temp;
    }

    public FrCll remove(int source) { //BROKEN
        FrCll temp = new FrCll(this);
        temp.getPiles().get(source).remove(0);
        temp.setCounter(temp.getCounter()-1);
        temp.setCost(temp.getCost()+1);
        return temp;
    }

    public boolean isValid(FrCll b) {
        return (nextChoices().contains(b)) || (this.equals(b));
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cst) {
        cost = cst;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int c) {
        counter = c;
    }

    public ArrayList<ArrayList<Integer>> getPiles() {
        return piles;
    }

    public void setPiles(ArrayList<ArrayList<Integer>> p) {
        piles = p;
    }

    public ArrayList<Integer> getPile1() {
        return piles.get(0);
    }

    public ArrayList<Integer> getPile2() {
        return piles.get(1);
    }

    public ArrayList<Integer> getPile3() {
        return piles.get(2);
    }

    FrCll(int c, ArrayList<ArrayList<Integer>> p, int cst) { //Piles is a list of the 3 piles. A pile is a list of ints
        
        counter = c;
        piles = new ArrayList<ArrayList<Integer>>(n);
        ArrayList<Integer> pile1 = new ArrayList<Integer>();
        ArrayList<Integer> pile2 = new ArrayList<Integer>();
        ArrayList<Integer> pile3 = new ArrayList<Integer>();
        piles.add(pile1);
        piles.add(pile2);
        piles.add(pile3);
        for (int i=0;i<n;i++) {
            for (int j=0;j<p.get(i).size();j++) {
                // System.out.println("i="+i+"j="+j);
                // System.out.println(p.get(i).get(j));
                if (!p.get(i).isEmpty())
                    piles.get(i).add(p.get(i).get(j));
                
            }
            
        }
        //piles = p;
        cost = cst;
        inArr.add(pile1);
        inArr.add(pile2);
        inArr.add(pile3);
    }

    FrCll() {
        this(-1,null,-1);
    }

    FrCll(FrCll that) {
        this(that.counter,that.piles,that.cost);
    }

    public String toString() {
        String tmp = Integer.toString(counter);
        for (int i=0;i<n;i++) {
            tmp += getPiles().get(i).toString();
        }
        return tmp;
    }

    //https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    @Override
    public boolean equals(Object o) { 
  
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof FrCll)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        FrCll f = (FrCll) o; 
          
        // Compare the data members and return accordingly  
        return ((Integer.compare(counter, f.counter) == 0)// && (Integer.compare(cost,f.cost) == 0)
             && piles.equals(f.piles));
    } 
}


//https://www.geeksforgeeks.org/priorityqueue-comparator-method-in-java/
class The_Comparator implements Comparator<FrCll> { 
    public int compare(FrCll f1, FrCll f2) 
    { 
        return f1.compareTo(f2); 
    } 
} 

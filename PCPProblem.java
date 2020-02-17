import java.util.Arrays;
import java.util.LinkedList;

public class PCPProblem {
    //PCP[] input = {new PCP("MOM","M"),new PCP("O","OMOMO")};
    PCP[] input = {new PCP("AA","A")};

    LinkedList<PCP> fringe = new LinkedList<PCP>();

    boolean answer = false;
    boolean addedToFringe = false;
    
    public static void main(String[] args) {
        PCPProblem loop = new PCPProblem(); //anti-static

        System.out.println("BFS approach:");
        loop.initialize();
        System.out.println("Answer: " + loop.theLoop(true));

        System.out.println("DFS approach:");
        loop.initialize();
        System.out.println("Answer: " + loop.theLoop(false));
    }

    public void initialize() {
        answer = false;
        fringe.clear();
        fringe.add(input[0]);
    }

    public PCP theLoop(boolean BFS) {
        while (true) {
            addedToFringe = false;

            if (fringe.isEmpty()) {
                return new PCP("FAILURE: No solution","");
            }
            
            System.out.println("Goal Test on " + getNext(BFS).toString());

            populate(getNext(BFS),BFS);
            if (answer) { 
                return getNext(BFS);
            }
            if (BFS) fringe.remove();
        }
    }

    public PCP getNext(boolean BFS) {
        if (!BFS) return fringe.getLast(); //dfs
        else return fringe.getFirst(); //bfs
    }

    public void populate(PCP p, boolean BFS) {
        if (p.getTop().equals(p.getBot())) {
            answer = true;
            return;
        }
        if (!BFS) fringe.removeLast();
        //System.out.println("Expanding " + p.toString());
        for (PCP x : input) {
            if ((p.getBot().length() < 12) || (p.getTop().length() < 12)) { //arbitrary cutoff
                addedToFringe = true;
                PCP temp = new PCP(p);
                temp.addPCP(x);
                fringe.add(temp);
                //System.out.println("Added " + fringe.getLast().toString() + " to fringe");
            }
        }
    }
}

class PCP {
    private String top;
    private String bot;

    public String getTop() {
        return top;
    }

    public String getBot() {
        return bot;
    }

    public void addTop(String s) {
        top += s;
    }

    public void addBot(String s) {
        bot += s;
    }

    public void addPCP(PCP p) {
        top += p.getTop();
        bot += p.getBot();
    }

    PCP() {
        top = new String();
        bot = new String();
    }

    PCP(String t,String b) {
        this(); //deep copy
        top = t;
        bot = b;
    }

    PCP(PCP p) {
        this(p.getTop(),p.getBot());
    }

    public String toString() {
        return "Top: " + this.getTop() + ", Bottom: " + this.getBot();
    }
}
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

public class Geography {

    boolean selfAware = true; //UNSAFE PRACTICE

    String[] input = {"ABC","CDE","CFG","EHE","EIJ","GHK","GLC"}; //abc cfg glc cde ehe eij REMOVED GHK
    //String[] input = {"ABC","CDE","CFG","EHI","GJC","GKG"};
    //String[] input = {"A1B","B2A","B3D","S4T","D5S","A6A","T7A","A8B", "A9B"};
    //String[] input = {"ABC","EDC","CCC","CDE"};
    LinkedList<Geog> fringe = new LinkedList<Geog>();

    boolean answer = false;
    boolean addedToFringe = false;
    
    public static void main(String[] args) {
        Geography loop = new Geography(); //anti-static

        System.out.println("BFS approach:");
        loop.initialize();
        System.out.println("Answer: " + loop.theLoop(true).getState());

        System.out.println("DFS approach:");
        loop.initialize();
        System.out.println("Answer: " + loop.theLoop(false).getState());
    }

    public void initialize() {
        answer = false;
        fringe.clear();
        fringe.add(new Geog(input[0]));
    }

    public Geog theLoop(boolean BFS) {
        while (true) {
            addedToFringe = false;

            if (fringe.isEmpty()) {
                return new Geog("FAILURE: No solution");
            }
            
            System.out.println("Goal Test on " + getNext(BFS).getState());

            populate(getNext(BFS));
            if (answer) { 
                return getNext(BFS);
            }
            if (BFS) fringe.remove();
            else if (!addedToFringe) fringe.removeLast();
        }
    }

    public Geog getNext(boolean BFS) {
        if (!BFS) return fringe.getLast();
        else return fringe.getFirst();
        //return (BFS ? fringe.getLast() : fringe.getFirst());
    }

    public void populate(Geog g) {
        //System.out.println("Expanding " + g.getState());
        if (g.getState().size() == input.length) answer = true;
        for (String x : input) {
            if (!g.contains(x) && isValid(g,x)) {
                addedToFringe = true;
                Geog temp = new Geog(g);
                temp.getState().add(x);
                fringe.add(temp);
                //System.out.println("Added " + fringe.getLast().getState() + "to fringe");
            }
        }
    }

    public boolean isValid(Geog g,String x) {
        return (g.getState().isEmpty() || (g.getLastChar() == x.charAt(0)));
    }
}

class Geog {
    private ArrayList<String> state;

    public char getLastChar() {
        String foo = state.get(state.size()-1);
        return foo.charAt(foo.length()-1);
    }

    public ArrayList<String> getState() {
        return state;
    }

    public boolean contains(String s) {
        for (String word : state) {
            if (word.equals(s)) {
                return true;
            }
        }
        return false;
    }

    Geog() {
//        System.out.println("Default constructor");
        state = new ArrayList<String>();
    }

    Geog(ArrayList<String> initial) {
//        System.out.println("AL<S> Constructor");
        state = new ArrayList<String>();
        state.addAll(initial);
    }

    Geog(String initial) {
        //System.out.println("S Constructor");
        state = new ArrayList<String>();
        state.add(initial);
    }

    Geog(Geog g) {
        // System.out.println("Copy constructor");
        this(g.getState());
    }
}
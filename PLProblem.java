import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PLProblem {
    ParkingLot cur;
    public int gridX;
    public int gridY;

    PriorityQueue<ParkingLot> fringe = new PriorityQueue<ParkingLot>(new My_Comparator());
    public ArrayList<ParkingLot> visited = new ArrayList<ParkingLot>();

    public static void main(String[] args) {
        PLProblem loop = new PLProblem();
        loop.initialize();
        System.out.println("cost: " + loop.theLoop());
    }

    public void initialize() {
        ArrayList<Car> cars = new ArrayList<Car>();
        int cst = 0;
        Car tempCar = new Car();
        
        tempCar.addPosition(1,0);
        tempCar.addPosition(2,0);
        tempCar.setHorizontal(true);
        cars.add(tempCar);
        tempCar = new Car();
        tempCar.addPosition(3,0);
        tempCar.addPosition(3,1);
        tempCar.setHorizontal(false);
        cars.add(tempCar);
        tempCar = new Car();
        tempCar.addPosition(1,2);
        tempCar.addPosition(2,2);
        tempCar.addPosition(3,2);
        tempCar.setHorizontal(true);
        cars.add(tempCar);

        ParkingLot input = new ParkingLot(cars, cst);
        
        fringe.clear();
        fringe.add(input);
    }

    public int theLoop() {
        while (true) {
            ParkingLot tmp;
            tmp = fringe.peek();
            if (fringe.isEmpty()) return -1;
            fringe.poll();  // removes p from fringe
            cur = tmp;
            System.out.println("Goal Test on " + tmp.toString());

            if (populate(tmp)) {
                return tmp.getGn();
            }
        }
    }

    public boolean populate(ParkingLot p) {
        //goal check, return true here, false at end
        ParkingSpace goalPosition = new ParkingSpace(3,0);
        if (p.getMyCar().getPositions().contains(goalPosition)) {
            return true;
        }

        for (ParkingLot x : p.nextChoices()) {
            if (!visited.contains(x)) {
            //    System.out.println("adding "+x+" to the fringe");
                fringe.add(x);
                visited.add(x);
            }
        }

        return false;
    }
}

class Car {
    boolean horizontal;
    ArrayList<ParkingSpace> positions = new ArrayList<ParkingSpace>();

    public Car() {
        horizontal = false;
    }

    public Car(ArrayList<ParkingSpace> pos, boolean horiz) {
        horizontal = horiz;
        ArrayList<ParkingSpace> tmp = new ArrayList<ParkingSpace>();
        for (ParkingSpace p : pos) {
            tmp.add(new ParkingSpace(p));
        }
        positions = tmp;
    }

    public Car(Car c) {
        this(c.getPositions(), c.getHorizontal());
        // ArrayList<ParkingSpace> tmp = new ParkingSpace[c.getLength()];
        // for (int i=0;i<c.getLength();i++) {
        // tmp[i] = new ParkingSpace(c.getParkingSpace(i));
        // }

    }

    public void addPosition(int addX, int addY) {
        positions.add(new ParkingSpace(addX, addY));
    }

    public void setHorizontal(boolean horiz) {
        horizontal = horiz;
    }

    public void move(int direction) {
        for (ParkingSpace p : positions) {
            if (horizontal) {
                if (direction > 0)
                    p.moveRight();
                else
                    p.moveLeft();
            } else {
                if (direction > 0)
                    p.moveUp();
                else {
                    p.moveDown();
                }
            }
        }
    }

    public ArrayList<ParkingSpace> getPositions() {
        return positions;
    }

    public int getLeft() {
        int min = 1000;
        for (ParkingSpace p : positions) {
            if (p.getX() < min)
                min = p.getX();
        }
        return min;
    }

    public int getRight() {
        int max = 0;
        for (ParkingSpace p : positions) {
            if (p.getX() > max)
                max = p.getX();
        }
        return max;
    }

    public int getTop() {
        int max = 0;
        for (ParkingSpace p : positions) {
            if (p.getY() > max)
                max = p.getY();
        }
        return max;
    }

    public int getBot() {
        int min = 1000;
        for (ParkingSpace p : positions) {
            if (p.getY() < min)
                min = p.getY();
        }
        return min;
    }

    public boolean getHorizontal() {
        return horizontal;
    }

}

class ParkingLot implements Comparable<ParkingLot> {
    ArrayList<Car> cars = new ArrayList<Car>();
    int cost;

    public boolean isValid(ParkingLot b) {
        return (nextChoices().contains(b)) || (this.equals(b));
    }

    public Car getCar(Car c) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).equals(c)) {
                return cars.get(i);
            }
        }
        return c;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public ArrayList<ParkingLot> nextChoices() {
        boolean openMove = true;
        ArrayList<ParkingLot> tmp = new ArrayList<ParkingLot>();
        for (Car car : cars) { //sweep through looking for collisions
            openMove = true;
            if ((car.getLeft() > 0) && car.getHorizontal()) { //stays inside the board and driving forward/backwards
                for (Car c : cars) {
                    if (c.getPositions().contains(new ParkingSpace(car.getLeft()-1,car.getTop()))) {
                        openMove = false;
                    }
                }

                if (openMove) { //messy deep copy, adds snapshot of the current state to the fringe
                    ParkingLot tmpPL = new ParkingLot(this);
                    tmpPL.getCar(car).move(-1); // move left
                    tmpPL.cost++;
                    tmp.add(new ParkingLot(cars,tmpPL.cost));
                    tmpPL.cost--;
                    tmpPL.getCar(car).move(1);
                }
            }
            openMove = true;
            if ((car.getBot() > 0) && (!car.getHorizontal())) {
                for (Car c : cars) {
                    if (c.getPositions().contains(new ParkingSpace(car.getLeft(),car.getBot()-1))) {
                        openMove = false;
                    }
                }

                if (openMove) {
                    ParkingLot tmpPL = new ParkingLot(this);
                    tmpPL.getCar(car).move(-1); // move down
                    tmpPL.cost++;
                    tmp.add(new ParkingLot(cars,tmpPL.cost));
                    tmpPL.cost--;
                    tmpPL.getCar(car).move(1); 
                }
            }
            openMove = true;
            if ((car.getTop() < 2) && (!car.getHorizontal())) {
                for (Car c : cars) {
                    if (c.getPositions().contains(new ParkingSpace(car.getLeft(),car.getTop()+1))) {
                        openMove = false;
                    }
                }

                if (openMove) {
                    ParkingLot tmpPL = new ParkingLot(this);
                    tmpPL.getCar(car).move(1); //move up
                    tmpPL.cost++;
                    tmp.add(new ParkingLot(cars,tmpPL.cost));
                    tmpPL.cost--;
                    tmpPL.getCar(car).move(-1);
                }
            }
            openMove = true;
            if ((car.getRight() < 3) && (car.getHorizontal())) {
                for (Car c : cars) {
                    if (c.getPositions().contains(new ParkingSpace(car.getRight()+1,car.getTop()))) {
                        openMove = false;
                    }
                }

                if (openMove) {
                    ParkingLot tmpPL = new ParkingLot(this);
                    tmpPL.getCar(car).move(1); //move right
                    tmpPL.cost++;
                    tmp.add(new ParkingLot(cars,tmpPL.cost));
                    tmpPL.cost--;
                    tmpPL.getCar(car).move(-1);
                }
            }
        }
        return tmp; //to be added to fringe
    }

    public int compareTo(ParkingLot p) { //fudged to be match double to int
        if (getFn() - p.getFn() > 0) {
            return 1;
        } else if (getFn() - p.getFn() < 0) {
            return -1;
        }
        return 0;
    }

    public double getFn() {
        return (getGn()*0.99 + getHn()); //weight the heuristic a little to shrink search space, since a move forward can only reduce h(n) by 1
    } 

    int getGn() {
        return cost;
    }

    private int getHn() {
        //distance of my car to goal
        return (3-getMyCar().getRight());
    }
//Dominating heuristic (same output in this configuration)
//# of blocking cars + distance of my car to goal
    //     int goalX = 4;
    //     int goalY = 1;
    //     ArrayList<ParkingSpace> temp = new ArrayList<ParkingSpace>();
    //     if (getMyCar().getHorizontal()) {
    //         for (int i=0;i<(getMyCar().getRight()-goalX);i++) {
    //             temp.add(new ParkingSpace((goalX-i),goalY));
    //         }
    //     } else {
    //         for (int i=0;i<(getMyCar().getTop()-goalY);i++) {
    //             temp.add(new ParkingSpace(goalX, (goalY-i)));
    //         }
    //     }
    //     int blockingCars = 0;
    //     for (Car c : cars) {
    //         for (ParkingSpace pos : c.getPositions()) {
    //             if (temp.contains(pos)) {
    //                 blockingCars++;
    //             }
    //         }
    //     }
    //     return blockingCars + (3-getMyCar().getRight());
    // }

    public Car getMyCar() {
        return cars.get(0);
    }

    public ParkingLot() {
        cost = 0;
    }

    public ParkingLot(ArrayList<Car> newCars, int cst) {
        for (Car car : newCars) {
            cars.add(new Car(car));
        }
        cost = cst;
    }

    public ParkingLot(ParkingLot p) {
        for (Car c : p.getCars()) {
            cars.add(new Car(c.getPositions(),c.getHorizontal()));
        }
        cost = p.getGn();
    }

    public String toString() {
        String tmp = "";
        for (Car car : cars) {
            for (ParkingSpace pos : car.getPositions()) {
                tmp += "(" + pos.getX() + "," + pos.getY() +"),";
            }
            tmp += "\n";
        }
        return tmp;
    }

    public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof ParkingLot)) { 
            return false; 
        } 
          
        ParkingLot p = (ParkingLot) o; 

        return (p.cars.equals(this.cars));
    }
}

class ParkingSpace {
    int x;
    int y;

    ParkingSpace() {
        x = -1;
        y = -1;
    }
    ParkingSpace(int newX, int newY) {
        x=newX;
        y=newY;
    }
    ParkingSpace(ParkingSpace p) {
        this(p.getX(),p.getY());
    }

    public void moveUp() {
        y++;
    }

    public void moveDown() {
        y--;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        } 

        if (!(o instanceof ParkingSpace)) { 
            return false; 
        } 
          
        ParkingSpace p = (ParkingSpace) o; 
          
        return (p.x==this.x) && (p.y==this.y);
    }
}

//https://www.geeksforgeeks.org/priorityqueue-comparator-method-in-java/
class My_Comparator implements Comparator<ParkingLot> { 
    public int compare(ParkingLot f1, ParkingLot f2) 
    { 
        return f1.compareTo(f2); 
    } 
} 

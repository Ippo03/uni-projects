import java.lang.Math;

//class Point
public class Point{                                                                                             
    private int x,y;   
    //Constructor                                                                     
    Point(int in_x, int in_y) {this.x = in_x; this.y = in_y;}  
    //Getters                                                 
    public int x() {return x;}                                                                                  
    public int y() {return y;}
    //method that calculates and returns the Euclidean distance of two points
    public double distanceTo(Point z) {return Math.sqrt(Math.pow((this.x - z.x()),2) + Math.pow((this.y - z.y()),2) );}    
    //method that returns the square of the Euclidean distance      
    public int squareDistanceTo(Point z) {return (int)(distanceTo(z) * distanceTo(z));}  
     //prints a point as (x,y)                      
    public String toString() {return "(" + this.x() + "," + this.y() + ")";} 
}                                  
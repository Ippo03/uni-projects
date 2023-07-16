//class Rectangle
public class Rectangle {                                                    
    public int xmin,xmax;
    public int ymin,ymax;
    //Default Constructor
    Rectangle() {}      
    //Constructor                                                    
    Rectangle(int in_xmin, int in_xmax, int in_ymin, int in_ymax)          
    {
        this.xmin = in_xmin;
        this.xmax = in_xmax;
        this.ymin = in_ymin;
        this.ymax = in_ymax;
    }
    //Getters
    public int xmin() {return xmin;}                                        
    public int ymin() {return ymin;}
    public int xmax() {return xmax;}
    public int ymax() {return ymax;}
    //Setters
    public void setxmin(int in_xmin) { xmin = in_xmin;}                     
    public void setxmax(int in_xmax) { xmax = in_xmax;}
    public void setymin(int in_ymin) { ymin = in_ymin;}
    public void setymax(int in_ymax) { ymax = in_ymax;}

    //method that checks if a point is inside a rectangle
    public boolean contains(Point p)                                        
    {
        if(p.x() >= xmin() && p.x() <= xmax() && p.y() >= ymin() && p.y() <= ymax()){
            return true;
        }
        return false;
    }
    //method that checks if two rectangles intersect(have common parts)
    public boolean intersects(Rectangle that){
        //creating tops of rectangle this
        Point thistop1 = new Point(this.xmin(), this.ymin());
        Point thistop2 = new Point(this.xmin(), this.ymax());
        Point thistop3 = new Point(this.xmax(), this.ymin());
        Point thistop4 = new Point(this.xmax(), this.ymax());
        //creating tops of rectangle that
        Point thattop1 = new Point(that.xmin(), that.ymin());
        Point thattop2 = new Point(that.xmin(), that.ymax());
        Point thattop3 = new Point(that.xmax(), that.ymin());
        Point thattop4 = new Point(that.xmax(), that.ymax());
        //if at least one top of that rectangle contained in this rectangle->true
        if (contains(thattop1) || contains(thattop2) || contains(thattop3) || contains(thattop4))
            return true;
        //if at least one top of this rectangle contained in that rectangle->true
        else if (that.contains(thistop1) || that.contains(thistop2) || that.contains(thistop3) || that.contains(thistop4))
            return true;
        //else->false
        return false;
    }
    //method that calculates and return the euclidean distance from a given point to the closest point of the rectangle
    public double distanceTo(Point p)                                       
    {
        //we are sure dif will get a value so initialize with a random value
        double dif = 100000;
        //if point is contained in calling rectangle the distance is 0
        if(contains(p)){
            dif = 0;
        //if we can't create a vertical line from point to rectangle
        }else if((p.x() > xmax() && p.y() > ymax()) || (p.x() > xmax() && p.y() < ymin()) || (p.x() < xmin() && p.y() < ymin()) || (p.x() < xmin() && p.y() > ymax())){
            //find using implemented methods the distance from every top of rectangle
            double min = p.distanceTo(new Point(xmin(), ymin()));
            double d1 = p.distanceTo(new Point(xmin(), ymax()));
            double d2 = p.distanceTo(new Point(xmax(), ymin()));
            double d3 = p.distanceTo(new Point(xmax(), ymax()));
            //find min distance
            if (d1 < min)
                min = d1;
            if (d2 < min)
                min = d2;
            if (d3 < min)
                min = d3;
            dif = min;
        //if yes-> we substract and get distance
        }else if (p.x() >= xmin() && p.x() <= xmax()){
            if (p.y() < ymin()){
                dif = ymin() - p.y();
            }else{
                dif = p.y() - ymax();
            }
        }else if (p.y() >= ymin() && p.y() <= ymax()){
            if (p.x() > xmax()){
                dif = p.x() - xmax();
            }else{
                dif =  xmin() - p.x();
            }
        }
        return dif;
    }
    //method that returns the square of upper distance 
    public int squareDistanceTo(Point p) {return (int)(distanceTo(p) * distanceTo(p));}    
    //prints a rectangle as [xmin,xmax] X [ymin,ymax]
    public String toString() {return "[" + xmin() + "," + xmax() + "] X [" + ymin() + "," + ymax() + "]";}     

}
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
//class TwoDTree
public class TwoDTree{                                                              
    private TreeNode head;
    int size = 0;
    boolean p_exists;
    //class TreeNode
    private class TreeNode{
        //data of node
        Point item;
        //pointer to left child
        TreeNode l;
        //pointer to right child
        TreeNode r;
        //Constructor of TreeNode
        TreeNode(Point p){item = p;}
        //Getters
        public Point getData() {return item;}
        public TreeNode getLeft() {return l;}
        public TreeNode getRight() {return r;}
        //Setters
        public void setRight(TreeNode right) {this.r = right;}
        public void setLeft(TreeNode left) {this.l = left;}
    }
    //Constructor of TwoDTree
    public TwoDTree(){head = null;}
    //methods that checks if the tree is empty
    public boolean isEmpty() { return head == null;}
    //method that returns the size of the tree
    public int size() {return size;}
    //method that checks if a given point exists in the tree
    public boolean search(Point p){
        int slevel = 0; 
        TreeNode current = head;
        while(true){
            //if empty->not exists
            if(current == null)
                return false;
            //if coordinates of current node match with coordinates of given point->exists 
            if(current.getData().x() == p.x() && current.getData().y() == p.y())
                return true;
            //check using x coordinate
            if(slevel % 2 == 0){
                slevel++;
                if(p.x() < current.getData().x()){
                    //move to left child
                    current = current.getLeft();
                }else if(p.x() >= current.getData().x()){
                    //move to right child
                    current = current.getRight();
                }
            //check using y coordinate
            }else{
                slevel++;
                if(p.y() < current.getData().y()){
                    //move to left child
                    current = current.getLeft();
                }else if(p.y() >= current.getData().y()){
                    //move to right child
                    current = current.getRight();
                }
            }
        }  
    }
    //method that inserts a point in the tree but first checks if already exists
    public void insert(Point p){
       int ilevel = 0; 
       //uses search method to checks if exists or not
       p_exists = search(p);
       //if yes->exists
       if(p_exists){
            System.out.println("Point->" + p.toString() + " already exists in the tree.");
       //if not->insert
       }else{
            //if tree empty->insert in root
            if (isEmpty()){
                head = new TreeNode(p);
                System.out.println("Point->" + p.toString() + " has been inserted in the tree");
                size++;
                return;
            }
            //if has nodes->iterate them like search method
            TreeNode current = head;
            while(true){
                //check using x coordinate
                if(ilevel % 2 == 0){
                    if(p.x() < current.getData().x()){
                        //if left child null->insert
                        if (current.getLeft() == null) {
                            current.setLeft(new TreeNode(p));
                            System.out.println("Point->" + p.toString() + " has been inserted in the tree");
                            size++;
                            return;
                        //else->move to left child
                        }else{
                            current = current.getLeft();
                            ilevel++;
                        }
                    }else if(p.x() >= current.getData().x()){
                        //if right child null->insert
                        if (current.getRight() == null) {
                            current.setRight(new TreeNode(p));
                            System.out.println("Point->" + p.toString() + " has been inserted in the tree");
                            size++;
                            return;
                        //else->move to right child
                        }else{
                            current = current.getRight();
                            ilevel++;
                        } 
                    }
                //check using y coordinate
                }else{
                    if(p.y() < current.getData().y()){
                        //if left child null->insert
                        if (current.getLeft() == null) {
                            current.setLeft(new TreeNode(p));
                            System.out.println("Point->" + p.toString() + " has been inserted in the tree");
                            size++;
                            return;
                        } else {
                            //move to left child
                            current = current.getLeft();
                            ilevel++;
                        }
                    }else if(p.y() >= current.getData().y()){
                        //if right child null->insert
                        if (current.getRight() == null) {
                            current.setRight(new TreeNode(p));
                            System.out.println("Point->" + p.toString() + " has been inserted in the tree");
                            size++;
                            return;
                        } else {
                            //move to right child
                            current = current.getRight();
                            ilevel++;
                        }
                    }
                }
            }
        }
    }
    //method that finds using recursion the nearest of a given point
    public Point nearestNeighbor(Point p){
        //at first nearest point is root
        Point nearestPoint = head.getData();
        //initialize distance with a max value
        double nearestDistance = 10000000;
        //call recusrsive method with these initial arguments
        nearestPoint = nearestNeighborRecursive(head, p, nearestPoint, nearestDistance, 0, new Rectangle(0, 100, 0, 100));
        return nearestPoint;
    }
    //recursive method
    public Point nearestNeighborRecursive(TreeNode node, Point p, Point nearestPoint, double min, int level, Rectangle rec){
        //Rectangles of left and right child
        Rectangle leftrec, rightrec;
        //TreeNodes of left and right child
        TreeNode leftnode, rightnode;
        //if we have points
        if (node != null){
            //if current node's distance from p is shorter than the distance of current closest point->update current nearest point
            if (node.getData().squareDistanceTo(p) <= nearestPoint.squareDistanceTo(p)) {
                nearestPoint = node.getData();
            }
            //even level
            if (level % 2 == 0){
                //creating left and right rectangle of current node
                leftrec = new Rectangle(rec.xmin(), node.getData().x(), rec.ymin(), rec.ymax());
                rightrec = new Rectangle(node.getData().x(), rec.xmax(), rec.ymin(), rec.ymax());
                //check if there is left child
                if (node.l != null){
                    //we check if left node is closer to p and if min distance is longer that the distance of p from left rectangle
                    if (leftrec.squareDistanceTo(p) <= min || node.getLeft().getData().squareDistanceTo(p) <= nearestPoint.squareDistanceTo(p)){
                        min = leftrec.squareDistanceTo(p);
                        leftnode = node.getLeft(); 
                        //move to left child and recursively call with informed arguments(leftnode, updated min distance,increased level)
                        nearestPoint = nearestNeighborRecursive(leftnode, p, nearestPoint, min, level + 1, leftrec);
                    }  
                }
                //check if there is right child
                if (node.r != null){
                    //we check if right node is closer to p and if min distance is longer that the distance of p from right rectangle
                    if (rightrec.squareDistanceTo(p) <= min || node.getRight().getData().squareDistanceTo(p) <= nearestPoint.squareDistanceTo(p)){
                        min = rightrec.squareDistanceTo(p);
                        rightnode = node.getRight();
                        //move to right child and recursively call with informed arguments(rightnode, updated min distance,increased level)
                        nearestPoint = nearestNeighborRecursive(rightnode, p, nearestPoint, min, level + 1, rightrec);
                    }
                }
            }
            //uneven level
            else {
                //creating left and right rectangle of current node
                rightrec = new Rectangle(rec.xmin(), rec.xmax(), node.getData().y(), rec.ymax());
                leftrec = new Rectangle(rec.xmin(), rec.xmax(), rec.ymin(), node.getData().y());
                //check if there is left child
                if (node.l != null){
                    //we check if left node is closer to p and if min distance is longer that the distance of p from left rectangle
                    if (leftrec.squareDistanceTo(p) <= min || node.getLeft().getData().squareDistanceTo(p) <= nearestPoint.squareDistanceTo(p)){
                        min = leftrec.squareDistanceTo(p);
                        leftnode = node.getLeft();
                        //move to left child and recursively call with informed arguments(leftnode, updated min distance,increased level)
                        nearestPoint = nearestNeighborRecursive(leftnode, p, nearestPoint, min, level + 1, leftrec);
                    }  
                }    
                //check if there is right child
                if (node.r != null){
                    //we check if right node is closer to p and if min distance is longer that the distance of p from right rectangle
                    if (rightrec.squareDistanceTo(p) <= min || node.getRight().getData().squareDistanceTo(p) <= nearestPoint.squareDistanceTo(p)){
                        min = leftrec.squareDistanceTo(p);
                        rightnode = node.getRight();
                        //move to right child and recursively call with informed arguments(rightnode, updated min distance,increased level)
                        nearestPoint = nearestNeighborRecursive(rightnode, p, nearestPoint, min, level + 1, rightrec);
                    }
                }
            }
        }
        return nearestPoint;
    }
    //method rangesearch that returns every point that is contained in the rectangle given
    public StringQueueImpl<Point> rangeSearch(Rectangle rect){
        //creates a queue using class StringQueueImpl from ergasia 1
        StringQueueImpl<Point> list = new StringQueueImpl<>();
        //calls recursive method and passing initialized variables
        rangeSearchRecursive(head, new Rectangle(0, 100, 0, 100), rect, list, 0);
        return list;
    }
    //recursive method
    public void rangeSearchRecursive(TreeNode cur, Rectangle currect, Rectangle that, StringQueueImpl<Point> lst, int level){
        //current node
        TreeNode node;
        //we are sure one of if statemnts is true
        //node's left child rectangle default initialization
        Rectangle leftrect = new Rectangle();
        //node's right child rectangle default initialization
        Rectangle rightrect = new Rectangle();
        //if in root->initialize left and right rectangles as presented in project report
        if (level == 0){
            leftrect = new Rectangle(currect.xmin(), cur.getData().x(), currect.xmin(), currect.xmax());
            rightrect = new Rectangle(cur.getData().x(), currect.xmax(), currect.ymin(), currect.ymax());
        }
        //if we are in uneven level->update left and right rectangle as presented
        if (level % 2 == 1){
            leftrect = new Rectangle(currect.xmin(), currect.xmax(), currect.ymin(), cur.getData().y());
            rightrect = new Rectangle(currect.xmin(), currect.xmax(), cur.getData().y(), currect.ymax());
        }
        //if we are in even level and not in root->update left and right rectangle as presented
        if (level % 2 == 0  && level > 0){
            leftrect = new Rectangle(currect.xmin(), cur.getData().x(), currect.ymin(), currect.ymax());
            rightrect = new Rectangle(cur.getData().x(), currect.xmax(), currect.ymin(), currect.ymax());
        }
        //copy node in order to use for searching left and right child
        node = cur;
        //if rectangle given intersects(using intersect method) with the rectangle either of right child or left child and also the point of current node is contained->add to list
        if (that.contains(cur.getData())){
            if(that.intersects(rightrect) || (that.intersects(leftrect))){
                lst.put(cur.getData());
            }
        }
        //recursive call with increased level for left child only if there is left child and current nodes rectangle intersects with given
        if (that.intersects(currect) && node.getLeft() != null){
            rangeSearchRecursive(node.getLeft(), leftrect, that, lst, level + 1);   
        }        
        //recursive call with increased level for right child only if there is right child and current nodes rectangle intersects with given
        if (that.intersects(currect) && node.getRight() != null){
            rangeSearchRecursive(node.getRight(), rightrect, that, lst, level + 1);
        }
        //if nothing happens->there are no points in the range
        return;
    }
    //scanner to get user input
    static Scanner scanner = new Scanner(System.in);
    public static int validate_choices(int edge_l, int edge_u){
        //user's choice
        int choice;
        //asks for user's choice and makes sure it is between the edges
        do{
            System.out.print("Enter your choice: ");  
            choice = scanner.nextInt();
            if(choice < edge_l || choice > edge_u){
                System.out.println("Choices are from " + edge_l + " to " + edge_u + ".Try again.");
            }
        }while(choice < edge_l || choice > edge_u);
        return choice;
    }
    public static int validate_points(int edge_l, int edge_u, String label){
        int temp;
        //ask's for (label) coordinate and makes sure they it is in [0,100]
        do{
            System.out.print("Enter " + label + " coordinate: ");
            temp = scanner.nextInt();
            if(temp < edge_l || temp > edge_u){
                System.out.println("Coordinates must be between " + edge_l + " and " + edge_u + ".Try again.");
            }
          }while (temp < edge_l || temp > edge_u);
          return temp;
    }
    //EXTRA
    //method that prints every node of the tree in preorder starting from the root
    public void printPreOrder() {
        preOrder(head);
    }
    //recursive method
    private void preOrder(TreeNode node) {
        //if head null->tree empty
        if (node == null)
            return;
        //print current node
        System.out.println(">" + node.getData());
        //recursive call for left subtree
        preOrder(node.getLeft());
        //recursive call for right subtree
        preOrder(node.getRight());
    }
    //method that prints every node of the tree in inorder starting from the root
    public void printInOrder() {
        inOrder(head);
    }
    //recursive method
    private void inOrder(TreeNode node) {
        //if head null->tree empty
        if (node == null) return;
        //recursive call for left subtree
        inOrder(node.getLeft());
        //print current node
        System.out.println(">" + node.getData());
        //recursive call for right subtree
        inOrder(node.getRight());
    }
    //method that prints every node of the tree postorder starting from the root
    public void printPostOrder() {
        postOrder(head);
    }
    //recursive method
    private void postOrder(TreeNode node) {
        //if head null->tree empty
        if (node == null)
            return;
        //recursive call for left subtree
        postOrder(node.getLeft());
        //recursive call for right subtree
        postOrder(node.getRight());
        //print current node
        System.out.println(">" + node.getData());
    }

     public static void main(String[] args) throws IOException {
        //lines of txt file
        int lines = 0;
        //size of tree
        int size = 0;
        //creates an object of TwoDTree
        TwoDTree tree = new TwoDTree();
        //read input file
        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line;
            System.out.println("Inserting in tree the points of the txt file.");
            while ((line = br.readLine()) != null) {
                //first line->number of points = size of tree
                if(lines == 0){
                    size = Integer.parseInt(line);
                    lines++;
                }else{
                    //other lines->points as (0 30) 
                    String [] linepa = line.split(" ");
                    //split and put in variables
                    int x = Integer.parseInt(linepa[0]);
                    int y = Integer.parseInt(linepa[1]);
                    //checks if in [0,100]
                    if(x < 0 || x > 100 || y < 0 || y > 100){
                        System.out.println("Coordinates must be between 0 and 100.");
                        System.out.println("The program has ended!!!");
                        System.exit(0);
                    }
                    //create point
                    Point p = new Point(x,y);
                    //insert in tree
                    tree.insert(p);
                    lines++;
                }
            }
            System.out.println();
            //if first line != size of tree->problem
            if(size != lines - 1){
                System.out.println("The size of the tree is incorrect.");
                System.out.println("The program has ended!!!");
                System.exit(0);
            }
            br.close();
        //catch exceptiom if file not found or read
        }catch(IOException ex){
            System.err.println("Can't read the file.Please try again.");
            System.exit(0);
        }
        //variable for user's choice
        int choice;
        //variables for point coordinates
        int in_x, in_y;
        //variables for rectangle coordinates
        int in_xmin, in_xmax, in_ymin, in_ymax;
        //main menu
        while (true) {
            System.out.println("------------Main Menu------------");
            System.out.println("1. Compute the size of the tree");
            System.out.println("2. Insert a new point");
            System.out.println("3. Search if a given point exists in the tree");
            System.out.println("4. Provide a query rectangle");
            System.out.println("5. Provide a query point");
            System.out.println("6. Print points of tree");
            System.out.println("0. Exit");
            System.out.println();
            //takes user's choice and check if it is between 0 and 6
            choice = validate_choices(0,6);
            switch (choice) {
              //if choice = 1->returns size of tree
              case 1:
                System.out.println();
                System.out.println("The size of the tree is " + tree.size());
                System.out.println();
                break;
              case 2:
                //if choice = 2->inserts a point in the tree
                System.out.println();
                System.out.println("Inserting a new point in the tree");
                //ask's for x coordinate and makes sure it is in [0,100]
                in_x = validate_points(0, 100, "x");
                //ask's for y coordinate and makes sure it is in [0,100]
                in_y = validate_points(0, 100, "y");
                //if ok->creates new point
                Point ip = new Point(in_x,in_y);
                //inserts using insert method
                tree.insert(ip);
                System.out.println();
                break;
              case 3:
                //if choice = 3->search if a point exists in the tree
                System.out.println();
                System.out.println("Searching for a point in the tree");
                //ask's for x coordinate and makes sure it is in [0,100]
                in_x = validate_points(0, 100, "x");
                //ask's for y coordinate and makes sure it is in [0,100]
                in_y = validate_points(0, 100, "y");
                //if ok->creates new point
                Point sp = new Point(in_x,in_y);
                //search method returns a boolean variable
                boolean found = tree.search(sp);
                //if true->found
                if(found){
                  System.out.println("Point->" + sp.toString() + " exists in the tree.");
                  System.out.println();
                //else->not found
                }else{
                  System.out.println("Point->" + sp.toString() + " does not exist in the tree.");
                  System.out.println();
                }
                break;
              case 4:
                //if choice = 4->returns all points that are contained in a rectangle(range) given
                System.out.println();
                System.out.println("Create a rectangle");
                //ask's for xmin coordinate and makes sure it is in [0,100]
                in_xmin = validate_points(0, 100, "xmin");
                //ask's for xmax coordinate and makes sure it is in [0,100]
                in_xmax = validate_points(0, 100, "xmax");
                //ask's for ymin coordinate and makes sure it is in [0,100]
                in_ymin = validate_points(0, 100, "ymin");
                //ask's for ymax coordinate and makes sure it is in [0,100]
                in_ymax = validate_points(0, 100, "ymax");
                //creates a rectangle
                Rectangle rec = new Rectangle(in_xmin, in_xmax, in_ymin, in_ymax);
                //uses rangesearch method that return a list 
                StringQueueImpl<Point> list = tree.rangeSearch(rec);
                //if list empty->nothing in range
                if(list.isEmpty()){
                  System.out.println();
                  System.out.println("There are no points in this range.");
                  System.out.println();
                //else->prints points in this range
                }else{
                  System.out.print("In this range " + rec.toString() + " there are these points: ");
                  list.printQueue(System.out);
                  System.out.println();
                  System.out.println();
                }
                break;
              case 5:
                  //if choice = 5->returns the point that is the closet to a given point
                  System.out.println();
                  System.out.println("Create a point");
                  //ask's for x coordinate and makes sure it is in [0,100]
                  in_x = validate_points(0, 100, "x");
                  //ask's for y coordinate and makes sure it is in [0,100]
                  in_y = validate_points(0, 100, "y");
                  //creates a point
                  Point pt = new Point(in_x, in_y);
                  //use nearest method to get the point we search for
                  Point pt1 = (tree.nearestNeighbor(pt));
                  System.out.println();
                  System.out.println("The nearest neighbour to " + pt.toString() + " is " + pt1.toString());
                  System.out.println("Point->" + pt.toString() + " differs from " + pt1.toString() + " by " + pt1.distanceTo(pt));
                  System.out.println();
                  break;
              case 6:
                  //mini print menu
                  while(true){
                    if(choice == 0){break;}
                    System.out.println();
                    System.out.println("---------Print Menu---------");
                    System.out.println("1. Preorder");
                    System.out.println("2. Inorder");
                    System.out.println("3. Postorder");
                    System.out.println("0. Main Menu");
                    System.out.println();
                    //takes user's choice and check if it is between 0 and 3
                    choice = validate_choices(0, 3);
                    switch(choice){
                        //if choice 1->use preorder
                        case 1:
                            System.out.println();
                            System.out.println("Preorder: ");  tree.printPreOrder();
                            break;
                        //if choice 2->use inorder
                        case 2:
                            System.out.println();
                            System.out.println("Inorder: ");  tree.printInOrder();
                            break;
                        //if choice 3->use postorder
                        case 3:
                            System.out.println();
                            System.out.println("Postorder: ");  tree.printPostOrder();
                            break;
                        //if choice 0->go back to main menu
                        case 0:
                            System.out.println();
                            break;
                    }
                  }
                  break;
              case 0:
                //if choice = 6->exit
                System.out.println("The program has ended!!!");
                scanner.close();
                System.exit(0);
            }
           }
      }
}
/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

import java.util.Scanner;

class App3 {
	
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		float a;
		float b;
		float c;
		float r1;
		float r2;
		System.out.println("This program reads three numbers and return the solution");
		System.out.println("of the quadratic equation with these numbers as coefficients.");
		System.out.print("Enter the first number : ");
		a = Float.parseFloat(in.next());
		System.out.print("Enter the second number : ");
		b = Float.parseFloat(in.next());
		System.out.print("Enter the third number : ");
		c = Float.parseFloat(in.next());
		boolean f = ((b*b)-(4*a*c))<0;
		if (f) {
			System.out.println("There are no real values for the quadratic equation. ");
		}
		else {
			float riza = (float) Math.sqrt((b*b)-(4*a*c));
			r1 = ((-1*b) + riza)/(2*a);
			r2 = ((-1*b) - riza)/(2*a);
			String x1 = String.format("%.3f", r1);
			String x2 = String.format("%.3f", r2);
			x1 = x1.replace("." , ",");
			x2 = x2.replace("." , ",");
			System.out.println("The first solution is : "  + x1);
			System.out.println("The second solution is : " + x2);
		}
		in.close();
	}
}
    


	
		
	
		




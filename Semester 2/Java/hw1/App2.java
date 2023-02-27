/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

import java.util.Scanner;

class App2 {
	
	public static void main(String args[]){
		int positive = 0;
		int negative = 0;
		int Items = 0;
		double Sum = 0;
		double Average = 0;
		int Max = 0;
		int Min = 0;
		Scanner in = new Scanner(System.in);
		System.out.print("Give a number: ");
	    	int number = in.nextInt();
		while (number != 0){
			Items = Items + 1;
			Sum = Sum + number;
			if (number > 0){
				positive = positive + 1;
			}else{
				negative = negative + 1;
			}
			if (number > Max){
 				Max = number;
			}
			if (number < Min){
				Min = number;
			}
			System.out.print("Give a number: ");
			number = in.nextInt();
		}
		in.close();
		Average = Sum/Items;
		String Ave = String.valueOf(Average);
		Ave = Ave.replace("." , ",");
		System.out.println("-------------------------");
		System.out.println("Items   : " + Items);
		System.out.println("Average : " + Ave);
		System.out.println("Negative: " + negative);
		System.out.println("Positive: " + positive);
		System.out.println("Max     : " + Max);
		System.out.println("Min     : " + Min);
		System.out.println("-------------------------");
	}
}


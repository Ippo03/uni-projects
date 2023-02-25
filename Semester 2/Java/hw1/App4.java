/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

class App4 {
	public static void main(String args[]){
		int n1 = 0 , n2 = 1 , sum = 1;
		String n = args[0];
		int x = Integer.parseInt(n);
		while(true){
			if (sum > x){
				System.out.println("Fibonacci number = " + sum);
				System.out.println(x + " is not a fibonacci number");
				break;
			}
			System.out.println("Fibonacci number = " + sum);
			if (sum == x){
				System.out.println(x + " is a fibonacci number");
				break;
			}
			n1 = n2;
			n2 = sum;
			sum = n1 + n2;
		}
		
	}
}


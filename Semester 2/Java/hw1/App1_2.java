/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

class App1_2 {
	
		
    // Fill your code here (Factorial Method)
	public static int factorial(int n){
		int fact = 1;
	    	for(int i = 1; i <= n; i++){
			fact = fact*i;
        	}
	    	return fact;	
	}

	public static void main(String args[]){
		
		// Fill your code here 
     		int n = Integer.parseInt(args[0]);
		if (n < 0 ){
			System.out.println("Cant find factorial.");
		} else {
			System.out.println("Factorial = " + factorial(n));
   		}
	}
}






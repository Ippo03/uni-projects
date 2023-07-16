/*
	Name:IPPOKRATIS PANTELIDIS 
	Student Number:p3210150
*/


public class bankApp  {

	public static void main(String args[]) {

    		Account acct1 = new Account("Togantzi Maria" , "100-00" , 188.46);
    		Account acct2 = new Account("Kalergis Christos" , "100-01" , 140.21);
    		Account acct3 = new Account("Maras Petros" , "100-02" , 0.00);

		System.out.println ("New accounts");
	
		acct1.toString();
		acct2.toString();
		acct3.toString();             
		
		acct1.deposit(-10.0);
        	System.out.println();
		acct2.deposit(500.1);
		System.out.println();
		acct3.withdraw(1420.75);
        	System.out.println();
        	acct3.withdraw(-10.00);
        	System.out.println();
       		acct3.withdraw(420.75);

		System.out.println ("\nadd interest ... ");
		
   		acct1.addInterest();
   		acct2.addInterest();
   		acct3.addInterest();

		System.out.println();
		
		acct1.toString();
		acct2.toString();
		acct3.toString(); 

   }

}


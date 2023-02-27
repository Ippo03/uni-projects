
/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

class Account {
	
	// Data
   	private final double RATE = 0.015;
	private String  name;
   	private String  acctNumber;
   	private double balance;
	private double newbalance;
   	
	// Constructors
   	Account (String name, String acctNumber, double balance) {
		this.name = name; 
		this.acctNumber = acctNumber;
		this.balance = balance;
   	}
	
	Account (String name, String acctNumber) {
		this.balance = 0.00;
	}

	// Methods
   	double deposit (double amount){
		System.out.println("Deposit @ Account " + this.acctNumber); 
		String sbalance = String.format("%.2f", this.balance);
		System.out.println("Balance " + sbalance); 
		System.out.println("Requested: " + amount);
		if (amount < 0){ 
			this.newbalance = this.balance;
			System.out.println("Error: Deposit amount is invalid."); 
		}else{
			this.newbalance = this.balance + amount;
			this.balance = this.newbalance; 
		}
		String snbalance = String.format("%.2f", this.newbalance);
		System.out.println("New Balance " + snbalance);
		return this.balance;
	}

	double withdraw (double amount){
		System.out.println("withDraw @ Account " + this.acctNumber); 
		String sbalance = String.format("%.2f", this.balance);
		System.out.println("Balance " + sbalance); 
		System.out.println("Requested: " + amount); 
		if (this.balance < amount){ 
			System.out.println("Error: Insufficient funds."); 
			this.newbalance = this.balance;
		}else if(amount < 0){ 
			System.out.println("Error: Withdraw amount is invalid."); 
			this.newbalance = this.balance;
		}else if (this.balance > amount){
			this.newbalance = this.balance - amount;
		}
		String snbalance = String.format("%.2f", this.newbalance);
		System.out.println("New Balance " + snbalance);
		return this.balance;
   	}

   	double addInterest(){
		if (this.balance == 0){
			this.balance = 0;
		}else{
      	this.balance = this.balance + this.balance*RATE;
		}
		return this.balance;
   	}

   	double getBalance (){
      	return this.balance;
   	}

   	String getAccountNumber (){
		return this.acctNumber;
   	}

   	public String toString(){
      	System.out.println("Account Number : " + this.acctNumber);
		System.out.println("Name : " + this.name);
		String sbalance = String.format("%.2f", this.balance);
		System.out.println("Balance " + sbalance); 
		return "";
   	}
}
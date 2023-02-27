public  class Loan extends BankProduct {
    private double _amount;
    private double _annualInterestPercentage;

    public Loan() {
        super();
    }

    public Loan(String productCode, String customerIBAN, String customerTaxCode, 
        double amount, double annualInterestPercentage, String description) {
        super(productCode, customerIBAN, customerTaxCode, description);
        _amount = amount;
        _annualInterestPercentage = annualInterestPercentage;
    }

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    
    public double getInterest() {
        return _annualInterestPercentage;
    }

    public void setInterest(double annualInterestPercentage) {
        this._annualInterestPercentage = annualInterestPercentage;
    }

    public String toString() {
        return String.format("Key: %s, Product Code: %s, Customer Iban: %s, Customer Tax Code: %s \n\t\tDescription: %s, Amount: %.02f EUR, Annual Interest: %.02f%%", 
            getKey(), getProductCode(), getCustomerIBAN(), getCustomerTaxCode(), getDescription(), _amount, _annualInterestPercentage * 100);
    }
}
 
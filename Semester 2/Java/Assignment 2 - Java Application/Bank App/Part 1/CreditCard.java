public class CreditCard extends BankProduct {
    private double _commissionPercentage;
    private double _maxTransactionAmount;
    private double _maxAnnualAmount;
    private double _balance;

    public CreditCard() {
        super();
        _balance = 0;
    }

    public CreditCard(String productCode, String customerIBAN, String customerTaxCode, double commissionPercentage, double maxTransactionAmount, double maxAnnualAmount) {
        super(productCode,customerIBAN, customerTaxCode);
        _balance = 0;
        _commissionPercentage = commissionPercentage;
        _maxTransactionAmount = maxTransactionAmount;
        _maxAnnualAmount = maxAnnualAmount;
    }

    public double getCommissionPercentage() {
        return _commissionPercentage;
    }

    public void setCommissionPercentage(double commissionPercentage) {
        _commissionPercentage = commissionPercentage;
    }

    public double getMaxTransactionAmount() {
        return _maxTransactionAmount;
    }
    
    public void setMaxTransactionAmount(double maxTransactionAmount) {
        _maxTransactionAmount = maxTransactionAmount;
    }

    public double getMaxAnnualAmount() {
        return _maxAnnualAmount;
    }

    public void setMaxAnnualAmount(double maxAnnualAmount) {
        _maxAnnualAmount = maxAnnualAmount;
    }

    public void updateBalance(double amount) {
        _balance += amount;

    }

    public double getBalance() {
        return _balance;
    }

    public String toString() {
        return String.format("Key: %s, Product Code: %s, Customer Iban: %s, Customer Tax Code: %s, Commission: %.02f%%, Max Transaction Amount: %.02f EUR, Max Annual Amount: %.02f EUR",
            getKey(), getProductCode(), getCustomerIBAN(), getCustomerTaxCode(), _commissionPercentage * 100, _maxTransactionAmount, _maxAnnualAmount);
    }
}

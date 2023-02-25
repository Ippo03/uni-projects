
public class CreditCardTransaction {
    private String _productCode;
    private double _amount;
    private String _reason;
    private String _key;

    private static int _creditCardTransanctionNumber;

    protected CreditCardTransaction() {
        _creditCardTransanctionNumber ++;
        _key = String.format("CCT%04d", _creditCardTransanctionNumber);
    }

    public CreditCardTransaction(String productCode, double amount, String reason) {
        this();
        _productCode = productCode;
        _amount = amount;
        _reason = reason;
    }

    public String getKey() {
        return _key;
    }

    public  String getProductCode() {
        return _productCode;
    }

    public void setProductCode(String value) {
        _productCode = value;
    }

    public double getTransactionAmount() {
        return _amount;
    }

    public void setTransactionAmount(double value) {
        _amount = value;
    }

    public String getReason() {
        return _reason;
    }

    public void setReason(String value) {
        _reason = value;
    }

    public String toString() {
        return String.format("Key: %s, Bank Product Code: %s, Transaction Amount: %.02f EUR, Reason: %s", _key, _productCode, _amount, _reason);
    }

}

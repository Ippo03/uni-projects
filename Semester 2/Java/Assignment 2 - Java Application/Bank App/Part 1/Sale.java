
public class Sale {
    private String _salespersonCode;
    private String _bankProductCode;
    private String _reason;
    private String _key;

    private static int _salesNumber;

    public Sale() {       
        _salesNumber ++;
        _key = String.format("SL%04d", _salesNumber);
    }

    public Sale(String salespersonCode, String bankProductCode, String reason) {
        _salespersonCode = salespersonCode;
        _bankProductCode = bankProductCode;
        _reason = reason;

    }

    public String getKey() {
        return _key;
    }

    public String getSalesPersonCode() {
        return _salespersonCode;
    }

    public void setSalesPersonCode(String value) {
        _salespersonCode = value;
    }

    public String getBankProductCode() {
        return _bankProductCode;
    }

    public void setBankProductCode(String value) {
        _bankProductCode = value;
    }

    public String getReason() {
        return _reason;
    }

    public void setReason(String value) {
        _reason = value;
    }

    public String toString() {
        return String.format("Key: %s, Salesperson Code: %s, Bank Product Sale: %s, Reason: %s", _key, _salespersonCode, _bankProductCode, _reason);
    }

}

public class LoanCommission {
    private String _key;
    private double _amount;
    private double _commission;

    public LoanCommission(String key, double amount) {
        _key = key;
        _amount = amount;
    }

    public String getKey() {
        return _key;
    }

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double value) {
        _amount = value;
    }

    public double getCommission() {
        return _commission;
    }

    public void setCommission(double value) {
        _commission = value;
    }

    public String toString() {
        return String.format("Key: %s, Amount: %.02f EUR, Commission: %.02f EUR",
            _key, _amount, _commission);
    }
}
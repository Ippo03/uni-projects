public class SalesPerson {
    private String _lastName;
    private String _firstName;
    private String _code;
    private String _taxCode;
    private String _key;

    private static int _salesPersonNumber;

    public SalesPerson() {
        _salesPersonNumber ++;
        _key = String.format("SP%04d", _salesPersonNumber);
    }

    public SalesPerson(String lastName, String firstName, String code, String taxCode) {
        this();
        _lastName = lastName;
        _firstName = firstName;
        _code = code;
        _taxCode = taxCode;

    }

    public String getKey() {
        return _key;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String value) {
        _lastName = value;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String value) {
        _firstName = value;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String value) {
        _code = value;
    }

    public String getTaxCode() {
        return _taxCode;
    }

    public void setTaxCode(String value) {
        _taxCode = value;
    }

    @Override
    public String toString() {
        return String.format("Key: %s, Last Name: %s, First Name: %s, Code: %s, Tax Code: %s",
            _key, _lastName, _firstName, _code, _taxCode);
    }
}

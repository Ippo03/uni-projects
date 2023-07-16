import java.util.HashMap;

public class SalesPersonFileType extends ListFileType<SalesPerson> {

    @Override
    public String openingBlock() {
        return "{";
    }

    @Override
    public String closingBlock() {
        return "}";
    }

    @Override
    public String itemListToken() {
        return "SALESMAN_LIST";
    }

    @Override
    public String itemToken() {
        return "SALESMAN";
    }

    @Override
    public String[] getMandatoryProperties() {
        return new String[] { "CODE", "SURNAME", "FIRSTNAME" };
    }

    @Override
    public SalesPerson read(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        if (!validate(properties)) {
            missingMandatoryProperty();
        }

        PropertyResult codeResult = getProperty(properties, "CODE");
        PropertyResult lastNameResult = getProperty(properties, "SURNAME");
        PropertyResult firstNameResult = getProperty(properties, "FIRSTNAME");
        PropertyResult taxCodeResult = getProperty(properties, "AFM");
     
        return new SalesPerson(lastNameResult.getValue(), firstNameResult.getValue(),
            codeResult.getValue(), taxCodeResult.getValue());
    }

    @Override
    public HashMap<String, String> write(SalesPerson item) throws FileReaderInvalidEntryException {
        HashMap<String, String> result = new HashMap<>();

        result.put("CODE", item.getCode());
        result.put("SURNAME", String.format("\"%s\"", item.getLastName()));
        result.put("FIRSTNAME", String.format("\"%s\"", item.getFirstName()));
        result.put("AFM", item.getTaxCode());

        return result;
    }
    
}

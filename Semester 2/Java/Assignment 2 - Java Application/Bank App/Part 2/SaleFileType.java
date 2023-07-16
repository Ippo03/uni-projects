import java.util.HashMap;

public class SaleFileType extends ListFileType<Sale> {

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
        return "SALES_LIST";
    }

    @Override
    public String itemToken() {
        return "SALES";
    }

    @Override
    public String[] getMandatoryProperties() {
        return new String[] { "SALESMAN_CODE", "BANKITEM_TYPE", "BANKITEM_CODE" };
    }

    @Override
    public Sale read(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        if (!validate(properties)) {
            missingMandatoryProperty();
        }

        PropertyResult salesPersonCodeResult = getProperty(properties, "SALESMAN_CODE");
        PropertyResult bankItemTypeResult = getProperty(properties, "BANKITEM_TYPE");
        PropertyResult bankItemCodeResult = getProperty(properties, "BANKITEM_CODE");
        PropertyResult reasonResult = getProperty(properties, "REASON");

        return new Sale(salesPersonCodeResult.getValue(), bankItemCodeResult.getValue(), 
            reasonResult.getValue(), bankItemTypeResult.getValue());
    }

    @Override
    public HashMap<String, String> write(Sale item) throws FileReaderInvalidEntryException {
        HashMap<String, String> result = new HashMap<>();

        result.put("SALESMAN_CODE", item.getSalesPersonCode());
        result.put("BANKITEM_TYPE", item.getProductType());
        result.put("BANKITEM_CODE", item.getBankProductCode());
        result.put("REASON", String.format("\"%s\"", item.getReason()));

        return result;
    }
}

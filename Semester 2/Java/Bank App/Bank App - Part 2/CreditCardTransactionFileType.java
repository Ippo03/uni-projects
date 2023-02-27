import java.util.HashMap;

public class CreditCardTransactionFileType extends ListFileType<CreditCardTransaction> {

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
        return "TRN_LIST";
    }

    @Override
    public String itemToken() {
        return "TRN";
    }

    @Override
    public String[] getMandatoryProperties() {
        return new String[] { "BANKITEM_CODE", "VAL" };
    }

    @Override
    public CreditCardTransaction read(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        if (!validate(properties)) {
            missingMandatoryProperty();
        }

        PropertyResult productCodeResult = getProperty(properties, "BANKITEM_CODE");
        PropertyResult amountResult = getProperty(properties, "VAL");
        PropertyResult reasonResult = getProperty(properties, "JUSTIFICATION");

        double amount = 0.0;
        if (amountResult.success()) {
            try {
                amount = Double.parseDouble(amountResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }

        return new CreditCardTransaction(productCodeResult.getValue(), amount, reasonResult.getValue());
    }

    @Override
    public HashMap<String, String> write(CreditCardTransaction item) throws FileReaderInvalidEntryException {
        HashMap<String, String> result = new HashMap<>();

        result.put("BANKITEM_CODE", item.getProductCode());
        result.put("VAL", String.format("%.02f", item.getTransactionAmount()));
        result.put("JUSTIFICATION", String.format("\"%s\"", item.getReason()));

        return result;
    }
    
}

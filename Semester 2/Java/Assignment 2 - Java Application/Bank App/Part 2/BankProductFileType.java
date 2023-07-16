import java.util.HashMap;

public class BankProductFileType extends ListFileType<BankProduct> {
    private static final String EXCEPTION_UNSUPPORTED_PRODUCT_TYPE = "Unsupported product type";

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
        return "BANKITEM_LIST";
    }

    @Override 
    public String itemToken() {
        return "BANKITEM";
    }

    @Override
    public String[] getMandatoryProperties() {
        return new String[] { "CODE", "TYPE", "DESCR" };
    }

    @Override
    public BankProduct read(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        if (!validate(properties)) {
            missingMandatoryProperty();
        }

        PropertyResult pType = getProperty(properties, "TYPE");
        if (pType.getValue().toLowerCase().equals("loan")) {
            return readLoan(properties);
        }

        if (pType.getValue().toLowerCase().equals("card")) {
            return readCard(properties);
        }

        unsupportedProductType();
        return null;
    }

    protected void unsupportedProductType() throws FileReaderInvalidEntryException {
        throw new FileReaderInvalidEntryException(EXCEPTION_UNSUPPORTED_PRODUCT_TYPE);
    }

    private BankProduct readCard(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        PropertyResult codeResult = getProperty(properties, "CODE");
        PropertyResult descrResult = getProperty(properties, "DESCR");
        PropertyResult ibanResult = getProperty(properties, "IBAN");
        PropertyResult taxCodeResult = getProperty(properties, "AFM");
        PropertyResult commissionResult = getProperty(properties, "COMMISSION");
        PropertyResult maxTransactionResult = getProperty(properties, "MAX_TRANSACTION");
        PropertyResult maxAnnualResult = getProperty(properties, "MAX_ANNUAL");

        double commission = 0.0;
        if (commissionResult.success()) {
            try {
                commission = Double.parseDouble(commissionResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }

        double maxTransaction = 0.0;
        if (maxTransactionResult.success()) {
            try {
                maxTransaction = Double.parseDouble(maxTransactionResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }

        double maxAnnual = 0.0;
        if (maxAnnualResult.success()) {
            try {
                maxAnnual = Double.parseDouble(maxAnnualResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }

        return new CreditCard(codeResult.getValue(), ibanResult.getValue(), 
            taxCodeResult.getValue(), commission, maxTransaction, maxAnnual, descrResult.getValue());
    }

    private BankProduct readLoan(HashMap<String, String> properties) throws FileReaderInvalidEntryException {
        PropertyResult codeResult = getProperty(properties, "CODE");
        PropertyResult descrResult = getProperty(properties, "DESCR");
        PropertyResult ibanResult = getProperty(properties, "IBAN");
        PropertyResult taxCodeResult = getProperty(properties, "AFM");
        PropertyResult amountResult = getProperty(properties, "AMOUNT");
        PropertyResult interestResult = getProperty(properties, "INTEREST");

        double amount = 0.0;
        if (amountResult.success()) {
            try {
                amount = Double.parseDouble(amountResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }
        
        double interest = 0.0;
        if (interestResult.success()) {
            try {
                interest = Double.parseDouble(interestResult.getValue());
            } catch(Exception ex) {
                unableToParseItem();
            }
        }

        return new Loan(codeResult.getValue(), ibanResult.getValue(), 
            taxCodeResult.getValue(), amount, interest, descrResult.getValue());
    }

    @Override
    public HashMap<String, String> write(BankProduct item) throws FileReaderInvalidEntryException {
        if (item instanceof Loan) {
            return writeLoan((Loan) item);
        }

        if (item instanceof CreditCard) {
            return writeCreditCard((CreditCard) item);
        }

        unsupportedProductType();
        return null;
    }

    private HashMap<String, String> writeLoan(Loan item) {
        HashMap<String, String> result = new HashMap<>();
        
        result.put("TYPE", "Loan");
        result.put("CODE", item.getProductCode());
        result.put("DESCR", String.format("\"%s\"", item.getDescription()));
        result.put("AFM", item.getCustomerTaxCode());
        result.put("IBAN", item.getCustomerTaxCode());
        result.put("AMOUNT", String.format("%.02f", item.getAmount()));
        result.put("INTEREST", String.format("%.02f", item.getInterest()));
        
        return result;
    }

    private HashMap<String, String> writeCreditCard(CreditCard item) {
        HashMap<String, String> result = new HashMap<>();
        
        result.put("TYPE", "Card");
        result.put("CODE", item.getProductCode());
        result.put("DESCR", String.format("\"%s\"", item.getDescription()));
        result.put("AFM", item.getCustomerTaxCode());
        result.put("IBAN", item.getCustomerTaxCode());
        result.put("COMMISSION", String.format("%.02f", item.getCommissionPercentage()));
        result.put("MAX_TRANSACTION", String.format("%.02f", item.getMaxTransactionAmount()));
        result.put("MAX_ANNUAL", String.format("%.02f", item.getMaxAnnualAmount()));
        
        return result;
    }
}

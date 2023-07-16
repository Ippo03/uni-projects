public class CreditCardTransactionFileReader extends ListFileReader<CreditCardTransaction> {
    protected CreditCardTransactionFileReader() {
        super(new CreditCardTransactionFileType());
    }
}

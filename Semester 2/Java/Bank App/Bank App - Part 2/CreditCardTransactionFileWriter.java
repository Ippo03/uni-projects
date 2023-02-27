public class CreditCardTransactionFileWriter extends ListFileWriter<CreditCardTransaction> {
    protected CreditCardTransactionFileWriter() {
        super(new CreditCardTransactionFileType());
    }
}

public class BankProductFileWriter extends ListFileWriter<BankProduct> {
    protected BankProductFileWriter() {
        super(new BankProductFileType());
    }
}

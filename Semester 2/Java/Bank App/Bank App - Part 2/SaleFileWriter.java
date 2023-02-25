public class SaleFileWriter extends ListFileWriter<Sale> {
    protected SaleFileWriter() {
        super(new SaleFileType());
    }
}

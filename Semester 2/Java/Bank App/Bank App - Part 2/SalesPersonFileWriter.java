public class SalesPersonFileWriter extends ListFileWriter<SalesPerson> {
    protected SalesPersonFileWriter() {
        super(new SalesPersonFileType());
    }
}
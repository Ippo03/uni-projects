import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ListFileWriter<TItem> {
    private ListFileType<TItem> _fileType;

    protected ListFileWriter(ListFileType<TItem> fileType) {
        _fileType = fileType;
    }

    public void writeAll(String filename, ArrayList<TItem> items) throws IOException, FileReaderInvalidEntryException {
        FileWriter wr = null;

        try {
            wr = new FileWriter(filename);

            line(wr, _fileType.itemListToken(), 0);
            line(wr, _fileType.openingBlock(), 0);
            
            for (TItem item : items) {
                line(wr, _fileType.itemToken(), 1);
                line(wr, _fileType.openingBlock(), 1);
                HashMap<String, String> properties = _fileType.write(item);
                for (String key : properties.keySet()) {
                    line(wr, String.format("%s %s", key, properties.get(key)), 2);
                }
                line(wr, _fileType.closingBlock(), 1);
            }
            
            line(wr, _fileType.closingBlock(), 0);
        } finally {
            if (wr != null) {
                wr.flush();
                wr.close();
            }
        }
    }

    private void line(FileWriter writer, String lineText, int blockLevel) throws IOException {
        if (blockLevel > 0) {
            writer.write("\t".repeat(blockLevel));
        }
        writer.write(lineText);
        writer.write("\n");
    }
}

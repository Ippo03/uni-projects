import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public abstract class ListFileReader<TItem> {
    private static final String INVALID_FILE_SYNTAX = "Invalid file syntax";

    private ListFileType<TItem> _fileType;

    protected ListFileReader(ListFileType<TItem> fileType) {
        _fileType = fileType;
    }

    public ArrayList<TItem> readAll(String fileName) throws IOException, FileReaderException {
        var result = new ArrayList<TItem>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName, StandardCharsets.ISO_8859_1));
            String line = null;
            int blockLevel = 0;
            boolean inItem = false, inList = false;
            HashMap<String, String> itemProperties = null;

            while((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                String token = st.nextToken();

                while (token.isEmpty() && st.hasMoreTokens()) {
                    token = st.nextToken();
                }

                if (token.toLowerCase().equals(_fileType.itemListToken().toLowerCase())) {
                    inList = true;
                } else if (token.equals(_fileType.openingBlock())) {
                    blockLevel ++;

                    if (blockLevel > 2) {
                        throw new FileReaderException(INVALID_FILE_SYNTAX);
                    }
                } else if (token.equals(_fileType.closingBlock())) {
                    blockLevel --;

                    if (blockLevel == 1) {
                        if (inItem) {
                            inItem = false;
                            TItem item = null;
                            try {
                                item = _fileType.read(itemProperties);
                                result.add(item);
                            } catch (FileReaderInvalidEntryException fr) {
                                System.out.println(String.format(
                                    "Error while reading item: %s. Will ignore this item.", 
                                    fr.getMessage()));
                            }
                            
                            continue;
                        }

                        throw new FileReaderException(INVALID_FILE_SYNTAX);
                    }
                    
                    if (blockLevel == 0) {
                        if (inList) {
                            inList = false;
                            continue;
                        }

                        throw new FileReaderException(INVALID_FILE_SYNTAX);
                    }
                } else if (token.toLowerCase().equals(_fileType.itemToken().toLowerCase())) {
                    if (!inList || blockLevel != 1) {
                        throw new FileReaderException(INVALID_FILE_SYNTAX);
                    }

                    inItem = true;
                    itemProperties = new HashMap<>();
                } else {
                    String key = token;
                    itemProperties.put(key, line.split(token)[1].substring(1));
                }
            }

            if (blockLevel > 0 || inItem || inList) {
                throw new FileReaderException(INVALID_FILE_SYNTAX);
            }
        } finally { 
            if (reader != null) {
                reader.close();
            }
        }

        return result;
    } 
}

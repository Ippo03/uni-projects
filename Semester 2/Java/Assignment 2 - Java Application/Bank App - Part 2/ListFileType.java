import java.util.HashMap;

public abstract class ListFileType<TItem> {
    private static final String EXCEPTION_MISSING_MANDATORY_PROPERTY = "Missing mandatory property";
    private static final String EXCEPTION_UNABLE_TO_PARSE_ITEM = "Unable to parse item";

    public abstract String openingBlock();
    public abstract String closingBlock();
    public abstract String itemListToken();
    public abstract String itemToken();

    public abstract String[] getMandatoryProperties();

    public abstract TItem read(HashMap<String, String> properties) throws FileReaderInvalidEntryException;
    public abstract HashMap<String, String> write(TItem item) throws FileReaderInvalidEntryException;

    protected PropertyResult getProperty(HashMap<String, String> properties, String propertyName) {
        String value = null;
        boolean success = false;
        
        if (properties.containsKey(propertyName)) {
            value = properties.get(propertyName);
            success = (value != null);
        }

        if (properties.containsKey(propertyName.toLowerCase())) {
            value = properties.get(propertyName.toLowerCase());
            success = (value != null);
        }

        value = value.replace("\"", "");

        return new PropertyResult(success, value == null ? "" : value);
    }

    protected boolean validate(HashMap<String, String> properties) {
        for(String propertyName : getMandatoryProperties()) {
            if (!getProperty(properties, propertyName).success()) 
                return false;
        }

        return true;
    }

    protected void missingMandatoryProperty() throws FileReaderInvalidEntryException {
        throw new FileReaderInvalidEntryException(EXCEPTION_MISSING_MANDATORY_PROPERTY);
    }

    protected void unableToParseItem() throws FileReaderInvalidEntryException {
        throw new FileReaderInvalidEntryException(EXCEPTION_UNABLE_TO_PARSE_ITEM);
    }
}

class PropertyResult {
    private boolean _success;
    private String _value;

    public PropertyResult(boolean success, String value) {
        _success = success;
        _value = value;
    }

    public boolean success() {
        return _success;
    }

    public String getValue() {
        return _value;
    }
}
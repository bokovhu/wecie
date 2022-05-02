package hu.iit.bme.wecie.engine.asset;

import java.util.HashMap;
import java.util.Map;

public class Asset<W> {

    protected final Map<String, Object> parameters = new HashMap<> ();
    private final AssetType type;
    private final String key;
    private final LoadableResource resource;
    protected W wrappedData;

    public Asset (AssetType type, String key, LoadableResource resource) {
        this.type = type;
        this.key = key;
        this.resource = resource;
    }

    public AssetType getType () {
        return type;
    }

    public String getKey () {
        return key;
    }

    public LoadableResource getResource () {
        return resource;
    }

    public W getWrappedData () {
        return wrappedData;
    }

    public Object getParam (String key) {
        return parameters.get (key);
    }

}

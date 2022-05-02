package hu.iit.bme.wecie.engine.asset;

public class MutableAsset<W> extends Asset<W> {

    public MutableAsset (AssetType type, String key, LoadableResource resource) {
        super (type, key, resource);
    }

    public void setWrappedData (W wrappedData) {
        this.wrappedData = wrappedData;
    }

    public MutableAsset<W> setParam (String key, Object val) {
        this.parameters.put (key, val);
        return this;
    }

}

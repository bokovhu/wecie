package hu.iit.bme.wecie.engine.asset;

public interface AssetLoader<W> {

    void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset);

    void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset);

}

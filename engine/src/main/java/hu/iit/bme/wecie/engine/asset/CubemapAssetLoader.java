package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.opengl.texture.ImageData;
import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;

public class CubemapAssetLoader implements AssetLoader<Texture> {

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        // Find 6 images
        assetSystem.<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/px",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("px.png")
        ).<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/nx",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("nx.png")
        ).<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/py",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("py.png")
        ).<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/ny",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("ny.png")
        ).<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/pz",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("pz.png")
        ).<ImageData>enque (
                "cubemaps/" + asset.getKey () + "/nz",
                AssetType.IMAGEDATA,
                asset.getResource ().child ("nz.png")
        );

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        Texture cubemap = TextureFactory.cubemapFromImageData (
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/px"),
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/nx"),
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/py"),
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/ny"),
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/pz"),
                assetSystem.getImageData ("cubemaps/" + asset.getKey () + "/nz")
        );
        ((MutableAsset<Texture>) asset).setWrappedData (cubemap);
        assetSystem.cubemaps.put (asset.getKey (), (Asset<Texture>) asset);

        System.out.println ("Loaded cubemap " + asset.getKey ());

    }

}

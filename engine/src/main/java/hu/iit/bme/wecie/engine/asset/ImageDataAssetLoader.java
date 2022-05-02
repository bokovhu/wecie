package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.opengl.texture.ImageData;
import hu.iit.bme.wecie.engine.opengl.texture.ImageDataFactory;

import javax.imageio.ImageIO;

public class ImageDataAssetLoader implements AssetLoader<ImageData> {

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        try {

            ((MutableAsset<ImageData>) asset).setWrappedData (
                    ImageDataFactory.fromBufferedImage (
                            ImageIO.read (
                                    asset.getResource ().openInputStream ()
                            )
                    )
            );

            assetSystem.imageDataAssets.put (
                    asset.getKey (),
                    (Asset<ImageData>) asset
            );
            System.out.println ("Loaded image data " + asset.getKey ());

        } catch (Exception exc) {
            exc.printStackTrace ();
        }

    }

}

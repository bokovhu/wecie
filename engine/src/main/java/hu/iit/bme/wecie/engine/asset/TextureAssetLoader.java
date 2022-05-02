package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;

import javax.imageio.ImageIO;

public class TextureAssetLoader implements AssetLoader<Texture> {

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        // Nothing to do here

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        try {

            ((MutableAsset<Texture>) asset).setWrappedData (
                    TextureFactory.fromBufferedImage (
                            ImageIO.read (
                                    asset.getResource ().openInputStream ()
                            )
                    )
            );
            assetSystem.textures.put (
                    asset.getKey (),
                    (Asset<Texture>) asset
            );
            System.out.println ("Loaded texture " + asset.getKey ());

        } catch (Exception exc) {

            exc.printStackTrace ();

        }

    }

}

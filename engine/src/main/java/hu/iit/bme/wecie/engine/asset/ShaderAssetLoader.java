package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.opengl.shader.Shader;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderFactory;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShaderAssetLoader implements AssetLoader<Shader> {

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        if (asset.getParam ("shaderType") == null) {
            throw new IllegalArgumentException ("shaderType must be set for shader assets!");
        }

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        StringBuilder shaderSource = new StringBuilder ();

        try (
                InputStreamReader isr = new InputStreamReader (asset.getResource ().openInputStream ());
                BufferedReader br = new BufferedReader (isr)
        ) {

            String line = null;
            while ((line = br.readLine ()) != null) {
                shaderSource.append (line.strip ())
                        .append ("\n");
            }

        } catch (IOException e) {
            e.printStackTrace ();
        }

        Shader shader = ShaderFactory.create (
                (ShaderType) asset.getParam ("shaderType"),
                shaderSource.toString ()
        );
        ((MutableAsset<Shader>) asset).setWrappedData (shader);
        assetSystem.shaders.put (
                asset.getKey (),
                (Asset<Shader>) asset
        );

        System.out.println ("Loaded shader " + asset.getKey ());

    }

}

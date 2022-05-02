package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderType;

import java.util.HashMap;
import java.util.Map;

public class ProgramAssetLoader implements AssetLoader<Program> {

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        Map<String, Object> vertexParams = new HashMap<> ();
        vertexParams.put ("shaderType", ShaderType.vertex);

        Map<String, Object> fragmentParams = new HashMap<> ();
        fragmentParams.put ("shaderType", ShaderType.fragment);

        assetSystem.enque (
                "program/" + asset.getKey () + "/vertex",
                AssetType.SHADER,
                asset.getResource ().child ("vertex.glsl"),
                vertexParams
        ).enque (
                "program/" + asset.getKey () + "/fragment",
                AssetType.SHADER,
                asset.getResource ().child ("fragment.glsl"),
                fragmentParams
        );

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        Program program = ProgramFactory.create (
                assetSystem.getShader ("program/" + asset.getKey () + "/vertex"),
                assetSystem.getShader ("program/" + asset.getKey () + "/fragment")
        );
        ((MutableAsset<Program>) asset).setWrappedData (program);
        assetSystem.programs.put (
                asset.getKey (),
                (Asset<Program>) asset
        );

        System.out.println ("Loaded program " + asset.getKey ());

    }

}

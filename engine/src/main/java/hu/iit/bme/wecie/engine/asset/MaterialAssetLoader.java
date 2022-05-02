package hu.iit.bme.wecie.engine.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.iit.bme.wecie.engine.drawing.three.Material;
import hu.iit.bme.wecie.engine.drawing.three.MaterialFactory;
import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMaterialProperty;
import org.lwjgl.assimp.Assimp;

import java.nio.FloatBuffer;

public class MaterialAssetLoader implements AssetLoader<Material> {

    private MaterialDescriptor materialDescriptorFromAIMaterial (AIMaterial aiMaterial) {

        MaterialDescriptor materialDescriptor = new MaterialDescriptor ();

        PointerBuffer aiMaterialProperties = aiMaterial.mProperties ();
        for (int i = 0; i < aiMaterial.mNumProperties (); i++) {

            AIMaterialProperty aiMaterialProperty = AIMaterialProperty.create (aiMaterialProperties.get (i));
            MaterialFactory.ProcessedAIMaterialProperty prop = MaterialFactory.processAIMaterialProperty (
                    aiMaterialProperty);

            final String matKey = prop.key;
            if (Assimp._AI_MATKEY_TEXTURE_BASE.equals (matKey)) {
                String textureFile = new String (prop.stringData);
                switch (aiMaterialProperty.mSemantic ()) {
                    case Assimp.aiTextureType_DIFFUSE:
                        materialDescriptor.albedoMap = textureFile;
                        break;
                    case Assimp.aiTextureType_NORMALS:
                    case Assimp.aiTextureType_HEIGHT:
                        materialDescriptor.normalMap = textureFile;
                        break;
                    case Assimp.aiTextureType_SPECULAR:
                    case Assimp.aiTextureType_REFLECTION:
                        materialDescriptor.metallicMap = textureFile;
                        break;
                    case Assimp.aiTextureType_SHININESS:
                    case Assimp.aiTextureType_UNKNOWN:
                        materialDescriptor.roughnessMap = textureFile;
                        break;
                    case Assimp.aiTextureType_AMBIENT:
                        materialDescriptor.aoMap = textureFile;
                        break;
                }
            } else if (Assimp.AI_MATKEY_COLOR_DIFFUSE.equals (matKey)) {
                FloatBuffer fb = prop.dataBuffer.asFloatBuffer ();
                Vector4f color = new Vector4f (fb);
                materialDescriptor.albedoColor = new Vector3f (color.x, color.y, color.z);
            } else if (Assimp.AI_MATKEY_COLOR_SPECULAR.equals (matKey)) {
                FloatBuffer fb = prop.dataBuffer.asFloatBuffer ();
                Vector4f color = new Vector4f (fb);
                materialDescriptor.metallic = color.x;
                materialDescriptor.roughness = color.y;
            }

        }

        return materialDescriptor;

    }

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        MaterialDescriptor materialDescriptor = null;

        LoadableResource albedoMapResource = null;
        LoadableResource metallicMapResource = null;
        LoadableResource roughnessMapResource = null;
        LoadableResource aoMapResource = null;
        LoadableResource normalMapResource = null;

        // See if there's a material descriptor
        if (asset.getResource ().childExists ("material.json")) {

            ObjectMapper objectMapper = new ObjectMapper ();
            try {

                materialDescriptor = objectMapper.readValue (
                        asset.getResource ().child ("material.json").openInputStream (),
                        MaterialDescriptor.class
                );

            } catch (Exception exc) {
                exc.printStackTrace ();
            }
        } else if (asset.getParam ("materialDescriptor") != null) {
            materialDescriptor = (MaterialDescriptor) asset.getParam ("materialDescriptor");
        } else if (asset.getParam ("aiMaterial") != null) {
            materialDescriptor = materialDescriptorFromAIMaterial ((AIMaterial) asset.getParam ("aiMaterial"));
        }

        if (materialDescriptor != null) {
            if (materialDescriptor.albedoMap != null) {
                albedoMapResource = asset.getResource ().child (materialDescriptor.albedoMap);
            } else {
                Texture albedoMap = TextureFactory.empty (
                        1,
                        1,
                        new Vector4f (materialDescriptor.albedoColor, 1f)
                );
                MutableAsset<Texture> albedoTextureAsset = new MutableAsset<> (
                        AssetType.TEXTURE,
                        "mat/" + asset.getKey () + "/albedo",
                        null
                );
                albedoTextureAsset.setWrappedData (albedoMap);
                assetSystem.textures.put (
                        albedoTextureAsset.getKey (),
                        albedoTextureAsset
                );
            }

            if (materialDescriptor.metallicMap != null) {
                metallicMapResource = asset.getResource ().child (materialDescriptor.metallicMap);
            } else {
                Texture metallicMap = TextureFactory.empty (
                        1,
                        1,
                        new Vector4f (
                                materialDescriptor.metallic,
                                materialDescriptor.metallic,
                                materialDescriptor.metallic,
                                1f
                        )
                );
                MutableAsset<Texture> metallicTextureAsset = new MutableAsset<> (
                        AssetType.TEXTURE,
                        "mat/" + asset.getKey () + "/metallic",
                        null
                );
                metallicTextureAsset.setWrappedData (metallicMap);
                assetSystem.textures.put (
                        metallicTextureAsset.getKey (),
                        metallicTextureAsset
                );
            }

            if (materialDescriptor.roughnessMap != null) {
                roughnessMapResource = asset.getResource ().child (materialDescriptor.roughnessMap);
            } else {
                Texture roughnessMap = TextureFactory.empty (
                        1,
                        1,
                        new Vector4f (
                                materialDescriptor.roughness,
                                materialDescriptor.roughness,
                                materialDescriptor.roughness,
                                1f
                        )
                );
                MutableAsset<Texture> roughnessTextureAsset = new MutableAsset<> (
                        AssetType.TEXTURE,
                        "mat/" + asset.getKey () + "/roughness",
                        null
                );
                roughnessTextureAsset.setWrappedData (roughnessMap);
                assetSystem.textures.put (
                        roughnessTextureAsset.getKey (),
                        roughnessTextureAsset
                );
            }

            if (materialDescriptor.aoMap != null) {
                aoMapResource = asset.getResource ().child (materialDescriptor.aoMap);
            }

            if (materialDescriptor.normalMap != null) {
                normalMapResource = asset.getResource ().child (materialDescriptor.normalMap);
            }

        } else {

            albedoMapResource = asset.getResource ().child ("albedo.png");
            metallicMapResource = asset.getResource ().child ("metallic.png");
            roughnessMapResource = asset.getResource ().child ("roughness.png");
            if (asset.getResource ().childExists ("ao.png")) {
                aoMapResource = asset.getResource ().child ("ao.png");
            }
            if (asset.getResource ().childExists ("normal.png")) {
                normalMapResource = asset.getResource ().child ("normal.png");
            }

        }

        if (albedoMapResource != null) {
            assetSystem.enque (
                    "mat/" + asset.getKey () + "/albedo",
                    AssetType.TEXTURE,
                    albedoMapResource
            );
        }
        if (metallicMapResource != null) {
            assetSystem.enque (
                    "mat/" + asset.getKey () + "/metallic",
                    AssetType.TEXTURE,
                    metallicMapResource
            );
        }
        if (roughnessMapResource != null) {
            assetSystem.enque (
                    "mat/" + asset.getKey () + "/roughness",
                    AssetType.TEXTURE,
                    roughnessMapResource
            );
        }
        if (aoMapResource != null) {
            assetSystem.enque (
                    "mat/" + asset.getKey () + "/ao",
                    AssetType.TEXTURE,
                    aoMapResource
            );
        }
        if (normalMapResource != null) {
            assetSystem.enque (
                    "mat/" + asset.getKey () + "/normal",
                    AssetType.TEXTURE,
                    normalMapResource
            );
        }

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        Material material = MaterialFactory.create (
                null,
                assetSystem.getTexture ("mat/" + asset.getKey () + "/albedo"),
                null,
                assetSystem.getTexture ("mat/" + asset.getKey () + "/metallic"),
                null,
                assetSystem.getTexture ("mat/" + asset.getKey () + "/roughness"),
                assetSystem.getTexture ("mat/" + asset.getKey () + "/ao"),
                assetSystem.getTexture ("mat/" + asset.getKey () + "/normal")
        );
        ((MutableAsset<Material>) asset).setWrappedData (material);
        assetSystem.materials.put (
                asset.getKey (),
                (Asset<Material>) asset
        );

        System.out.println ("Loaded material " + asset.getKey ());

    }

}

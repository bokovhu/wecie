package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMaterialProperty;
import org.lwjgl.assimp.Assimp;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public final class MaterialFactory {

    private MaterialFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Material create (
            Vector3f albedoColor,
            Texture albedoMap,
            Float metallic,
            Texture metallicMap,
            Float roughness,
            Texture roughnessMap,
            Texture ambientOcclusionMap,
            Texture normalMap
    ) {

        Texture albedoTexture = albedoMap != null
                ? albedoMap
                : TextureFactory.empty (1, 1, new Vector4f (albedoColor, 1.0f));
        Texture metallicTexture = metallicMap != null
                ? metallicMap
                : TextureFactory.empty (1, 1, new Vector4f (metallic, metallic, metallic, 1.0f));
        Texture roughnessTexture = roughnessMap != null
                ? roughnessMap
                : TextureFactory.empty (1, 1, new Vector4f (roughness, roughness, roughness, 1.0f));

        MaterialImpl material = new MaterialImpl ();

        material.setAlbedoMap (albedoTexture);
        material.setMetallicMap (metallicTexture);
        material.setRoughnessMap (roughnessTexture);
        material.setAmbientOcclusionMap (ambientOcclusionMap);
        material.setNormalMap (normalMap);

        return material;

    }

    public static ProcessedAIMaterialProperty processAIMaterialProperty (AIMaterialProperty property) {

        ProcessedAIMaterialProperty processed = new ProcessedAIMaterialProperty ();
        processed.aiMaterialProperty = property;
        processed.key = property.mKey ().dataString ();
        ByteBuffer bb = property.mData ();
        processed.dataBuffer = bb;
        final byte[] bytes = new byte[property.mDataLength ()];
        byte[] stringBytes = new byte[bytes.length >= 5 ? bytes.length - 5 : 0];
        try {

            for (int j = 0; j < bytes.length; j++) {
                bytes[j] = bb.get ();
            }

            // Null terminated string data, first 4 bytes are the length of the string
            // Must convert into non-terminated string data
            for (int j = 4; j < bytes.length - 1; j++) {
                stringBytes[j - 4] = bytes[j];
            }

            bb.rewind ();

        } catch (UnsupportedOperationException exc) {
            exc.printStackTrace ();
        }
        processed.data = bytes;
        processed.stringData = stringBytes;
        return processed;

    }

    public static String determineAIMaterialName (AIMaterial aiMaterial) {

        PointerBuffer aiMaterialProperties = aiMaterial.mProperties ();
        for (int i = 0; i < aiMaterial.mNumProperties (); i++) {

            AIMaterialProperty aiMaterialProperty = AIMaterialProperty.create (aiMaterialProperties.get (i));
            ProcessedAIMaterialProperty prop = processAIMaterialProperty (aiMaterialProperty);

            if (Assimp.AI_MATKEY_NAME.equals (prop.key)) {
                return new String (prop.stringData);
            }

        }

        throw new IllegalStateException ();

    }

    public static final class ProcessedAIMaterialProperty {

        public String key;
        public byte[] data;
        public byte[] stringData;
        public ByteBuffer dataBuffer;
        public AIMaterialProperty aiMaterialProperty;

    }

}

package hu.iit.bme.wecie.engine.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.iit.bme.wecie.engine.drawing.renderable.IndexedRenderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.drawing.renderable.Vertex;
import hu.iit.bme.wecie.engine.drawing.three.*;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class ModelAssetLoader implements AssetLoader<Model> {

    private AIScene loadAIScene (LoadableResource resource) {

        try {

            byte[] resourceBytes = IOUtils.toByteArray (resource.openInputStream ());
            ByteBuffer byteBuffer = MemoryUtil.memCalloc (resourceBytes.length);
            byteBuffer.put (resourceBytes).flip ();

            AIScene aiScene = Assimp.aiImportFileFromMemory (
                    byteBuffer,
                    Assimp.aiProcess_Triangulate
                    | Assimp.aiProcess_GenSmoothNormals
                    | Assimp.aiProcess_ForceGenNormals,
                    "obj"
            );

            return aiScene;

        } catch (IOException e) {
            throw new RuntimeException (e);
        }

    }

    @Override
    public void preProcessAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        String sceneFilename = "model.obj";

        if (asset.getResource ().childExists ("model.json")) {

            try {

                ObjectMapper objectMapper = new ObjectMapper ();

                ModelDescriptor modelDescriptor = objectMapper.readValue (
                        asset.getResource ().child ("model.json").openInputStream (),
                        ModelDescriptor.class
                );

                Set<String> enquedMaterials = new HashSet<> ();
                Map<Integer, String> matKeysToIndices = new HashMap<> ();
                int matIndex = 0;

                if (modelDescriptor.materials != null) {
                    for (Map.Entry<String, MaterialDescriptor> entry : modelDescriptor.materials.entrySet ()) {

                        Map<String, Object> matParams = new HashMap<> ();
                        matParams.put ("materialDescriptor", entry.getValue ());
                        final String matKey = "model/" + asset.getKey () + "/mat/" + entry.getKey ();
                        assetSystem.enque (
                                matKey,
                                AssetType.MATERIAL,
                                asset.getResource (),
                                matParams
                        );
                        enquedMaterials.add (matKey);

                    }
                }

                if (modelDescriptor.modelFile != null) {
                    sceneFilename = modelDescriptor.modelFile;
                }

                AIScene aiScene = loadAIScene (asset.getResource ().child (sceneFilename));
                asset.setParam ("aiScene", aiScene);

                PointerBuffer aiMaterials = aiScene.mMaterials ();
                if (aiMaterials != null) {
                    for (int i = 0; i < aiScene.mNumMaterials (); i++) {

                        AIMaterial aiMaterial = AIMaterial.create (aiMaterials.get (i));
                        final String aiMaterialName = MaterialFactory.determineAIMaterialName (aiMaterial);
                        final String matKey = "model/" + asset.getKey () + "/mat/" + aiMaterialName;

                        matKeysToIndices.put (i, matKey);

                        if (enquedMaterials.add (matKey)) {

                            Map<String, Object> matParams = new HashMap<> ();
                            matParams.put ("aiMaterial", aiMaterial);
                            assetSystem.enque (
                                    matKey,
                                    AssetType.MATERIAL,
                                    asset.getResource (),
                                    matParams
                            );

                        }

                    }
                }

                asset.setParam ("materialMap", matKeysToIndices);

            } catch (Exception exc) {
                exc.printStackTrace ();
            }

        } else {
            throw new IllegalArgumentException ("model.json must exist!");
        }

    }

    @Override
    public void loadAsset (AssetSystem assetSystem, MutableAsset<?> asset) {

        AIScene aiScene = (AIScene) asset.getParam ("aiScene");
        Map<Integer, String> matKeysToIndices = (Map<Integer, String>) asset.getParam ("materialMap");
        PointerBuffer aiMeshes = aiScene.mMeshes ();
        List<Mesh> meshes = new ArrayList<> ();

        if (aiMeshes != null) {
            for (int i = 0; i < aiScene.mNumMeshes (); i++) {

                AIMesh aiMesh = AIMesh.create (aiMeshes.get (i));

                List<Vertex> vertices = new ArrayList<> ();
                List<Vector3f> positions = new ArrayList<> ();
                List<Vector3f> normals = new ArrayList<> ();
                List<Vector2f> texCoords = new ArrayList<> ();

                AIVector3D.Buffer aiVertices = aiMesh.mVertices ();
                AIVector3D.Buffer aiNormals = aiMesh.mNormals ();
                AIVector3D.Buffer aiTexCoords = aiMesh.mTextureCoords (0);

                while (aiVertices.hasRemaining ()) {

                    AIVector3D aiVec = aiVertices.get ();
                    positions.add (new Vector3f (aiVec.x (), aiVec.y (), aiVec.z ()));

                }

                if (aiNormals != null) {
                    while (aiNormals.hasRemaining ()) {

                        AIVector3D aiVec = aiNormals.get ();
                        normals.add (new Vector3f (aiVec.x (), aiVec.y (), aiVec.z ()));

                    }
                }

                if (aiTexCoords != null) {
                    while (aiTexCoords.hasRemaining ()) {

                        AIVector3D aiVec = aiTexCoords.get ();
                        texCoords.add (new Vector2f (aiVec.x (), aiVec.y ()));

                    }
                }

                AIFace.Buffer aiFaces = aiMesh.mFaces ();
                AIString meshName = aiMesh.mName ();

                Material material = assetSystem.getMaterial (matKeysToIndices.get (aiMesh.mMaterialIndex ()));

                IndexedRenderable indexedRenderable = RenderableFactory.createIndexed ();

                for (int j = 0; j < aiMesh.mNumFaces (); j++) {

                    AIFace aiFace = aiFaces.get (j);
                    IntBuffer aiIndices = aiFace.mIndices ();
                    final int i1 = aiIndices.get ();
                    final int i2 = aiIndices.get ();
                    final int i3 = aiIndices.get ();

                    vertices.add (
                            new Vertex ()
                                    .position (positions.get (i1))
                                    .normal (normals.get (i1))
                                    .texCoord (texCoords.get (i1))
                    );

                    vertices.add (
                            new Vertex ()
                                    .position (positions.get (i2))
                                    .normal (normals.get (i2))
                                    .texCoord (texCoords.get (i2))
                    );

                    vertices.add (
                            new Vertex ()
                                    .position (positions.get (i3))
                                    .normal (normals.get (i3))
                                    .texCoord (texCoords.get (i3))
                    );

                }

                indexedRenderable.getVertexData ()
                        .add (vertices)
                        .uploadToVBO ();

                Mesh mesh = MeshFactory.create (
                        indexedRenderable,
                        meshName != null ? meshName.dataString () : "mesh",
                        material
                );
                meshes.add (mesh);

            }
        }

        Model model = ModelFactory.create (meshes);
        ((MutableAsset<Model>) asset).setWrappedData (model);
        assetSystem.models.put (
                asset.getKey (),
                (Asset<Model>) asset
        );

        System.out.println ("Loaded model " + asset.getKey ());

    }

}

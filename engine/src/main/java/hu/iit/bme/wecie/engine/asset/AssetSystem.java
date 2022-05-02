package hu.iit.bme.wecie.engine.asset;

import hu.iit.bme.wecie.engine.drawing.three.Material;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.Shader;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderType;
import hu.iit.bme.wecie.engine.opengl.texture.ImageData;
import hu.iit.bme.wecie.engine.opengl.texture.Texture;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class AssetSystem {

    private static final AssetSystem INSTANCE = new AssetSystem ();
    Map<String, Asset<Texture>> textures = new HashMap<> ();
    Map<String, Asset<Texture>> cubemaps = new HashMap<> ();
    Map<String, Asset<Model>> models = new HashMap<> ();
    Map<String, Asset<Shader>> shaders = new HashMap<> ();
    Map<String, Asset<Program>> programs = new HashMap<> ();
    Map<String, Asset<Material>> materials = new HashMap<> ();
    Map<String, Asset<ImageData>> imageDataAssets = new HashMap<> ();
    private int numTotalAssetsToLoad = 0;
    private int numAssetsLoaded = 0;
    private Map<AssetType, AssetLoader<?>> assetLoaders = new HashMap<> ();
    private Deque<MutableAsset<?>> preProcessingQueue = new ArrayDeque<> ();
    private Deque<MutableAsset<?>> loadingQueue = new ArrayDeque<> ();

    private AssetSystem () {

        registerAssetLoaders ();

    }

    public static AssetSystem getInstance () {
        return INSTANCE;
    }

    private void registerAssetLoaders () {

        assetLoaders.put (AssetType.TEXTURE, new TextureAssetLoader ());
        assetLoaders.put (AssetType.IMAGEDATA, new ImageDataAssetLoader ());
        assetLoaders.put (AssetType.CUBEMAP, new CubemapAssetLoader ());
        assetLoaders.put (AssetType.MATERIAL, new MaterialAssetLoader ());
        assetLoaders.put (AssetType.SHADER, new ShaderAssetLoader ());
        assetLoaders.put (AssetType.PROGRAM, new ProgramAssetLoader ());
        assetLoaders.put (AssetType.MODEL, new ModelAssetLoader ());

    }

    public AssetSystem clear () {

        textures.forEach ((k, v) -> v.getWrappedData ().delete ());
        textures.clear ();

        models.forEach ((k, v) -> v.getWrappedData ().getMeshes ().forEach (m -> m.getRenderable ().delete ()));
        models.clear ();

        shaders.forEach ((k, v) -> v.getWrappedData ().delete ());
        shaders.clear ();

        programs.forEach ((k, v) -> v.getWrappedData ().delete ());
        programs.clear ();

        materials.clear ();

        return this;

    }

    public AssetSystem enqueTexture (String key, String res, ClassLoader classLoader) {
        preProcessingQueue.addLast (
                new MutableAsset<> (
                        AssetType.TEXTURE,
                        key,
                        new ClasspathResource (classLoader, res)
                )
        );
        return this;
    }

    public AssetSystem enqueTexture (String key, File file) {
        preProcessingQueue.addLast (
                new MutableAsset<> (
                        AssetType.TEXTURE,
                        key,
                        new FileResource (file)
                )
        );
        return this;
    }

    public <W> AssetSystem enque (String key, AssetType assetType, LoadableResource resource) {
        preProcessingQueue.addLast (
                new MutableAsset<W> (
                        assetType,
                        key,
                        resource
                )
        );
        return this;
    }

    public <W> AssetSystem enque (
            String key,
            AssetType assetType,
            LoadableResource resource,
            Map<String, Object> params
    ) {
        MutableAsset<W> asset = new MutableAsset<W> (
                assetType,
                key,
                resource
        );

        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet ()) {
                asset.setParam (entry.getKey (), entry.getValue ());
            }
        }

        preProcessingQueue.addLast (asset);
        return this;
    }

    public AssetSystem enqueModel (String key, String res, ClassLoader classLoader) {
        preProcessingQueue.addLast (
                new MutableAsset<Texture> (
                        AssetType.MODEL,
                        key,
                        new ClasspathResource (classLoader, res)
                )
        );
        return this;
    }

    public AssetSystem enqueModel (String key, File file) {
        preProcessingQueue.addLast (
                new MutableAsset<Model> (
                        AssetType.MODEL,
                        key,
                        new FileResource (file)
                )
        );
        return this;
    }

    public AssetSystem enqueShader (String key, String res, ClassLoader classLoader, ShaderType shaderType) {
        preProcessingQueue.addLast (
                new MutableAsset<Shader> (
                        AssetType.SHADER,
                        key,
                        new ClasspathResource (classLoader, res)
                ).setParam ("shaderType", shaderType)
        );
        return this;
    }

    public AssetSystem enqueShader (String key, File file, ShaderType shaderType) {
        preProcessingQueue.addLast (
                new MutableAsset<Shader> (
                        AssetType.SHADER,
                        key,
                        new FileResource (file)
                ).setParam ("shaderType", shaderType)
        );
        return this;
    }

    public AssetSystem enqueProgram (String key, String res, ClassLoader classLoader) {
        preProcessingQueue.addLast (
                new MutableAsset<Program> (
                        AssetType.PROGRAM,
                        key,
                        new ClasspathResource (classLoader, res)
                )
        );
        return this;
    }

    public AssetSystem enqueProgram (String key, File file) {
        preProcessingQueue.addLast (
                new MutableAsset<Program> (
                        AssetType.PROGRAM,
                        key,
                        new FileResource (file)
                )
        );
        return this;
    }

    public AssetSystem enqueMaterial (String key, String res, ClassLoader classLoader) {
        preProcessingQueue.addLast (
                new MutableAsset<Material> (
                        AssetType.MATERIAL,
                        key,
                        new ClasspathResource (classLoader, res)
                )
        );
        return this;
    }

    public AssetSystem enqueMaterial (String key, File file) {
        preProcessingQueue.addLast (
                new MutableAsset<Material> (
                        AssetType.MATERIAL,
                        key,
                        new FileResource (file)
                )
        );
        return this;
    }

    public AssetSystem enqueCubemap (String key, String res, ClassLoader classLoader) {
        preProcessingQueue.addLast (
                new MutableAsset<Texture> (
                        AssetType.CUBEMAP,
                        key,
                        new ClasspathResource (classLoader, res)
                )
        );
        return this;
    }

    public AssetSystem enqueCubemap (String key, File file) {
        preProcessingQueue.addLast (
                new MutableAsset<Texture> (
                        AssetType.CUBEMAP,
                        key,
                        new FileResource (file)
                )
        );
        return this;
    }

    public boolean isFullyLoaded () {
        return preProcessingQueue.isEmpty ()
                && loadingQueue.isEmpty ();
    }

    public AssetSystem performPreProcessing () {

        while (!preProcessingQueue.isEmpty ()) {

            MutableAsset<?> asset = preProcessingQueue.removeFirst ();
            AssetLoader<?> assetLoader = assetLoaders.get (asset.getType ());
            assetLoader.preProcessAsset (
                    this,
                    asset
            );
            loadingQueue.addFirst (asset);

        }

        numTotalAssetsToLoad = loadingQueue.size ();

        return this;

    }

    public AssetSystem loadNext () {

        MutableAsset<?> asset = loadingQueue.removeFirst ();
        AssetLoader<?> assetLoader = assetLoaders.get (asset.getType ());
        assetLoader.loadAsset (this, asset);
        ++numAssetsLoaded;

        return this;
    }

    public float getProgress () {
        return (1.0f / numTotalAssetsToLoad) * (float) numAssetsLoaded;
    }

    public Texture getTexture (String key) {
        if (!textures.containsKey (key)) {
            return null;
        }
        return textures.get (key).getWrappedData ();
    }

    public Model getModel (String key) {
        if (!models.containsKey (key)) {
            return null;
        }
        return models.get (key).getWrappedData ();
    }

    public Shader getShader (String key) {
        if (!shaders.containsKey (key)) {
            return null;
        }
        return shaders.get (key).getWrappedData ();
    }

    public Program getProgram (String key) {
        if (!programs.containsKey (key)) {
            return null;
        }
        return programs.get (key).getWrappedData ();
    }

    public Material getMaterial (String key) {
        if (!materials.containsKey (key)) {
            return null;
        }
        return materials.get (key).getWrappedData ();
    }

    public ImageData getImageData (String key) {
        if (!imageDataAssets.containsKey (key)) {
            return null;
        }
        return imageDataAssets.get (key).getWrappedData ();
    }

    public Texture getCubemap (String key) {
        if (!cubemaps.containsKey (key)) {
            return null;
        }
        return cubemaps.get (key).getWrappedData ();
    }

}

package hu.iit.bme.wecie.engine.opengl.shader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ShaderFactory {

    private static final Logger log = LoggerFactory.getLogger (ShaderFactory.class);

    private ShaderFactory () {
        throw new UnsupportedOperationException ();
    }

    private static String readShaderSource (InputStream is) {

        try (
                InputStreamReader isr = new InputStreamReader (is);
                BufferedReader br = new BufferedReader (isr)
        ) {

            List<String> lines = new ArrayList<> ();
            String line = null;
            while ((line = br.readLine ()) != null) {
                lines.add (line);
            }

            return String.join ("\n", lines);

        } catch (IOException e) {
            log.warn ("Could not read shader source from input stream!", e);
        }
        return "";

    }

    public static Shader create (ShaderType type, String source) {

        ShaderImpl shader = new ShaderImpl (type, source);
        shader.generate ()
                .compile ();

        if (!shader.isCompiled ()) {
            throw new IllegalStateException ("Shader could not be compiled! Info log: " + shader.getInfoLog ());
        }

        return shader;

    }

    public static Shader vertex (String source) {

        return create (ShaderType.vertex, source);

    }

    public static Shader fragment (String source) {

        return create (ShaderType.fragment, source);

    }

    public static Shader vertexFromFile (File file) {

        return fromFile (ShaderType.vertex, file);

    }

    public static Shader fragmentFromFile (File file) {

        return fromFile (ShaderType.fragment, file);

    }

    public static Shader vertexFromResource (String resourceName, ClassLoader classLoader) {

        return fromResource (ShaderType.vertex, resourceName, classLoader);

    }

    public static Shader fragmentFromResource (String resourceName, ClassLoader classLoader) {

        return fromResource (ShaderType.fragment, resourceName, classLoader);

    }

    public static Shader fromResource (ShaderType type, String resourceName, ClassLoader classLoader) {

        try {

            URL url = classLoader.getResource (resourceName);
            if (url == null) {
                log.warn ("Shader resource does not exist: {}", resourceName);
                return null;
            }

            return create (type, readShaderSource (url.openStream ()));

        } catch (Exception exc) {
            log.warn ("Could not load shader from resource: {}", resourceName);
            log.warn ("Exception: ", exc);
            return null;
        }

    }

    public static Shader fromFile (ShaderType type, File file) {

        try (FileInputStream fis = new FileInputStream (file)) {

            return create (type, readShaderSource (fis));

        } catch (IOException e) {
            log.warn ("Could not load shader from file {}", file.getAbsolutePath ());
            log.warn ("Exception: ", e);
            return null;
        }

    }

}

package hu.iit.bme.wecie.engine.opengl.shader;

import java.io.File;

public final class ProgramFactory {

    private ProgramFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Program create (
            String vertexShaderSource,
            String fragmentShaderSource
    ) {

        return create (
                ShaderFactory.vertex (vertexShaderSource),
                ShaderFactory.fragment (fragmentShaderSource)
        );

    }

    public static Program createFromFiles (File vertexFile, File fragmentFile) {

        return create (
                ShaderFactory.vertexFromFile (vertexFile),
                ShaderFactory.fragmentFromFile (fragmentFile)
        );

    }

    public static Program createFromResources (
            String vertexResource,
            String fragmentResource,
            ClassLoader classLoader
    ) {

        return create (
                ShaderFactory.vertexFromResource (vertexResource, classLoader),
                ShaderFactory.fragmentFromResource (fragmentResource, classLoader)
        );

    }

    public static Program create (Shader vertex, Shader fragment) {
        return create (vertex, fragment, null, null, null);
    }

    public static Program create (
            Shader vertex,
            Shader fragment,
            Shader geometry,
            Shader tessControl,
            Shader tessEval
    ) {

        ProgramImpl program = new ProgramImpl ();

        program.generate ();

        if (vertex != null) {
            program.attach (vertex);
        }
        if (fragment != null) {
            program.attach (fragment);
        }
        if (geometry != null) {
            program.attach (geometry);
        }
        if (tessControl != null) {
            program.attach (tessControl);
        }
        if (tessEval != null) {
            program.attach (tessEval);
        }

        program.link ();

        if (!program.isLinked ()) {
            throw new IllegalStateException ("Program could not be linked! Info log: " + program.getInfoLog ());
        }

        program.validate ();

        if (!program.isValidated ()) {
            throw new IllegalStateException ("Program could not be validated! Info log: " + program.getInfoLog ());
        }

        return program;

    }

}

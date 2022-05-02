package hu.iit.bme.wecie.engine.asset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ClasspathResource implements LoadableResource {

    private final ClassLoader classLoader;
    private final String resourceName;

    public ClasspathResource (ClassLoader classLoader, String resourceName) {
        this.classLoader = classLoader;
        this.resourceName = resourceName;
    }

    public ClassLoader getClassLoader () {
        return classLoader;
    }

    public String getResourceName () {
        return resourceName;
    }

    @Override
    public InputStream openInputStream () throws IOException {
        URL url = classLoader.getResource (resourceName);
        if (url != null) {
            return url.openStream ();
        }
        throw new FileNotFoundException (resourceName);
    }

    @Override
    public LoadableResource child (String childName) {
        return new ClasspathResource (
                this.classLoader,
                this.resourceName + "/" + childName
        );
    }

    @Override
    public boolean childExists (String childName) {

        try (InputStream is = child (childName).openInputStream ()) {
            return true;
        } catch (IOException fnfEx) {
            return false;
        }

    }

}

package hu.iit.bme.wecie.engine.asset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResource implements LoadableResource {

    private final File file;

    public FileResource (File file) {
        this.file = file;
    }

    public File getFile () {
        return file;
    }

    @Override
    public InputStream openInputStream () throws IOException {
        return new FileInputStream (getFile ());
    }

    @Override
    public LoadableResource child (String childName) {
        return new FileResource (
                new File (
                        this.file,
                        childName
                )
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

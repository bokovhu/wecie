package hu.iit.bme.wecie.engine.asset;

import java.io.IOException;
import java.io.InputStream;

public class NullResource implements LoadableResource {

    @Override
    public InputStream openInputStream () throws IOException {
        throw new UnsupportedOperationException ();
    }

    @Override
    public LoadableResource child (String childName) {
        return null;
    }

    @Override
    public boolean childExists (String childName) {
        return false;
    }

}

package hu.iit.bme.wecie.engine.asset;

import java.io.IOException;
import java.io.InputStream;

public interface LoadableResource {

    InputStream openInputStream () throws IOException;

    LoadableResource child (String childName);

    boolean childExists (String childName);

}

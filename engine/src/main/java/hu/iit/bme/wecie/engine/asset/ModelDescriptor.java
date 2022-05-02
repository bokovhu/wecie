package hu.iit.bme.wecie.engine.asset;

import java.io.Serializable;
import java.util.Map;

public class ModelDescriptor implements Serializable {

    public Map<String, MaterialDescriptor> materials;
    public String modelFile;

}

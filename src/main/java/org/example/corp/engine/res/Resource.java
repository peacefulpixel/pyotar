package org.example.corp.engine.res;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.ResourceInitializationException;

import java.io.File;

public interface Resource {
    void load(File file) throws ResourceInitializationException;
    void sample();
}

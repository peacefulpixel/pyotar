package org.example.corp.engine.res;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.ResourceInitializationException;

import java.io.File;

public abstract class Resource {
    protected boolean successfullyLoaded = false;

    abstract void load(File file) throws ResourceInitializationException;
    abstract void sample();

    public boolean isSuccessfullyLoaded() {
        return successfullyLoaded;
    }
}

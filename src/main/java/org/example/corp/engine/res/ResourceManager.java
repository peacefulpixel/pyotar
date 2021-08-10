package org.example.corp.engine.res;

import org.example.corp.engine.exception.ResourceInitializationException;
import org.example.corp.engine.util.LoggerUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Basic resource manager.
 * Loads and caches resources when it being requested. Type of requested resource will be defined on it's loading and
 * will be dependent on file extension (e.g. .png is {@link Image})
 */
public class ResourceManager {
    private static final Map<String, Resource> resourceCache = new HashMap<>();
    private static final Logger logger = LoggerUtils.getLogger(ResourceManager.class);

    /**
     * Returns resource from cache or loads it if needed
     * @param type Type of resource to cast and verify compatibility of types
     * @param path Path to resource
     * @param <T> Same type
     * @return casted resource or null if it's not castable or failed to load
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T get(Class<T> type, String path) {
        Resource resource;
        if ((resource = resourceCache.get(path)) == null) {
            resource = forceLoad(path);
        }

        if (resource == null) {
            logger.warning("Unable to recognize resource type by it's file ending: " + path);
            return null;
        } else if (!resource.getClass().equals(type)) {
            logger.warning("Unable to get resource: incompatible types: type=" + type.toString()
                    + ", resource=" + resource.getClass());
            return null;
        }

        return (T) resource;
    }

    /**
     * Force load of the resource. If resource was already loaded, it will be reloaded and cached again.
     * It's not recommended to use this function unless resource reloading is required.
     * @see ResourceManager#get(Class, String)
     * @param path Path to resource
     * @return Loaded resource, which could be accessed through {@link ResourceManager#get(Class, String)} or null
     *         when resource type couldn't be recognized
     */
    public static Resource forceLoad(String path) {
        Resource resource = null;

        if (path.endsWith(".png")) {
            resource = new Image();
        } else if (path.endsWith(".vs") || path.endsWith(".fs")) {
            resource = new Shader();
        }

        if (resource == null) {
            logger.warning("Unable to define resource type: " + path);
            return null;
        }

        try {
            resource.load(new File(path));
        } catch (ResourceInitializationException e) {
            logger.warning("Unable to load resource " + path + ". Sampling it");
            resource.sample();
        }

        resourceCache.put(path, resource);
        return resource;
    }
}

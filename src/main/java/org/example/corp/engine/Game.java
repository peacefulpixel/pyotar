package org.example.corp.engine;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.logging.Logger;

import static org.example.corp.engine.shader.ShaderProgramsManager.DEFAULT_PROGRAM;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

//TODO: Add text drawing support
//TODO: Gui items
//TODO: Controls
//TODO: Add shapes drawing support
//TODO: Add audio support
//TODO: Cursor
//TODO: Animations
public class Game {

    private final Logger logger = Logger.getLogger(Game.class.getName());

    private Window window;
    private World world;

    private void initWindow() throws EngineException {
        logger.fine("Initializing game window");
        window = Window.MAIN_WINDOW;
        window.init();

        // Make the OpenGL context current
        glfwMakeContextCurrent(window.getGlfwId());
        window.putOnMiddleOfScreen();
        window.show();
    }

    private void initShaders() throws EngineException {
        ShaderProgramsManager.createShaderProgram(DefaultShaderProgram.class);
    }

    public void init() throws EngineException {
        logger.info("Initializing the engine");

        GLFWErrorCallback.createPrint(System.err).set();
        logger.fine("Error output stream has been set");

        logger.fine("Initializing GLFW");
        if (!glfwInit())
            throw new EngineException("Unable to initialize GLFW");

        initWindow();

        GL.createCapabilities();
        window.initAfterSettingsContext();

        initShaders();
        glActiveTexture(GL_TEXTURE0);

        world.init();
    }

    private void loop() throws EngineException {
        while (!window.isShouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            world.loop();
            world.render();

            glfwSwapBuffers(window.getGlfwId());
            glfwPollEvents();

            GameTime.refreshDelta();
        }
    }

    public Game(World world) {
        this.world = world;
    }

    public void start() throws EngineException {
        logger.info("Starting");
        loop();

        logger.info("Freeing");
        free();
    }

    public void free() {
        world.free();
        ShaderProgramsManager.free();
        glfwFreeCallbacks(window.getGlfwId());
        glfwDestroyWindow(window.getGlfwId());
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}

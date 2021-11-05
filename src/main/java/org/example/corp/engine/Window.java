package org.example.corp.engine;

import org.example.corp.engine.controls.Key;
import org.example.corp.engine.controls.Keyboard;
import org.example.corp.engine.controls.Mouse;
import org.example.corp.engine.controls.MouseButton;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.*;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.WindowInitializationException;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.LoggerUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.example.corp.engine.controls.KeyAction.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public static final Window MAIN_WINDOW = new Window();

    public static final String WINDOW_TITLE = "Game name";
    public static final int WINDOW_DWIDTH = 1280;
    public static final int WINDOW_DHEIGHT = 720;

    private final Logger logger = LoggerUtils.getLogger(Window.class);

    private final FinalObject<Long> glfwId = new FinalObject<>();
    private int width;
    private int height;
    private boolean vSyncEnabled = false;

    public void init() throws EngineException {
        init(WINDOW_DWIDTH, WINDOW_DHEIGHT, WindowType.BORDERED);
    }

    public void init(int width, int height, WindowType type) throws WindowInitializationException {
        logger.fine("Creating window: width=" + width + ", height=" + height + ", type=" + type.name());

        this.width = width;
        this.height = height;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        long monitor = NULL;
        if (type != WindowType.BORDERED) {
            monitor = glfwGetPrimaryMonitor();
            if (monitor == NULL) {
                throw new WindowInitializationException("Can't get primary monitor");
            }

            if (type == WindowType.BORDERLESS) {
                GLFWVidMode vidMode = glfwGetVideoMode(monitor);
                if (vidMode == null) {
                    throw new WindowInitializationException("Can't get video mode");
                }

                glfwWindowHint(GLFW_RED_BITS, vidMode.redBits());
                glfwWindowHint(GLFW_GREEN_BITS, vidMode.greenBits());
                glfwWindowHint(GLFW_BLUE_BITS, vidMode.blueBits());
                glfwWindowHint(GLFW_REFRESH_RATE, vidMode.refreshRate());
            }
        }

        glfwId.set(glfwCreateWindow(width, height, WINDOW_TITLE, monitor, NULL));

        if (glfwId.get() == NULL) {
            throw new WindowInitializationException("Unable to create GLFW window");
        }
    }

    public void initAfterSettingsContext() {
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LESS);
        glClearColor(0.5f, 0.0f, 0.5f, 0.0f);

        glEnable(GL_MULTISAMPLE);
        glMatrixMode(GL_PROJECTION);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        initCallbacks();
    }

    private void initCallbacks() {
        logger.info("Initializing GLFW callbacks");
        glfwSetKeyCallback(getGlfwId(), (windowId, key, scancode, action, mods) -> {
            //TODO: use scancode?
            Key keyObject = Key.getKetByGlfwId(key);

            if (action == GLFW_PRESS) {
                Keyboard.setKeyAction(keyObject, PRESSED);
                EventManager.provideEvent(new KeyPressedEvent(keyObject, mods));
            }

            if (action == GLFW_RELEASE) {
                Keyboard.setKeyAction(keyObject, RELEASED);
                EventManager.provideEvent(new KeyReleasedEvent(keyObject, mods));
            }

            if (action == GLFW_REPEAT) {
                Keyboard.setKeyAction(keyObject, REPEAT);
                EventManager.provideEvent(new KeyRepeatEvent(keyObject, mods));
            }

        });

        glfwSetCursorPosCallback(getGlfwId(), (wId, mouseX, mouseY) -> {
            double prevX = Mouse.mouseX;
            double prevY = Mouse.mouseY;
            Mouse.mouseX = mouseX;
            Mouse.mouseY = mouseY;
            EventManager.provideEvent(new MouseMovedEvent(mouseX, mouseY, prevX, prevY));
        });

        glfwSetMouseButtonCallback(getGlfwId(), (wId, buttonId, action, mods) -> {
            MouseButton button = MouseButton.getMouseButtonByGlfwId(buttonId);
            Mouse.setButtonPressed(button, action == GLFW_PRESS);

            if (action == GLFW_PRESS) {
                EventManager.provideEvent(new MouseButtonPressedEvent(Mouse.mouseX, Mouse.mouseY, button));
            } else {
                EventManager.provideEvent(new MouseButtonReleasedEvent(Mouse.mouseX, Mouse.mouseY, button));
            }
        });

        glfwSetScrollCallback(getGlfwId(), (wId, xOffset, yOffset) ->
                EventManager.provideEvent(new MouseScrollEvent(Mouse.mouseX, Mouse.mouseY, yOffset > 0)));

        glfwSetWindowSizeCallback(glfwId.get(), (wid, w, h) -> {
            EventManager.provideEvent(new WindowResizedEvent(width, height, w, h));
            width = w;
            height = h;
            refreshViewport();
        });
    }

    public synchronized void refreshViewport(float width, float height, double aspect) {
        glViewport(0, 0, (int) width, (int) height);
        glOrtho(-aspect/2, aspect/2, -1, 1, -1, 1);
        ShaderProgramsManager.bindAndPerform(program -> program.setOrtho(width/2, height/2));
    }

    public void refreshViewport() {
        refreshViewport(width, height, (double) width / height);
    }

    public long getGlfwId() {
        return glfwId.get();
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(glfwId.get(), x, y);
    }

    public void putOnMiddleOfScreen() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (vidMode == null) {
            logger.warning("Unable to retrieve vidMode");
            return;
        }

        setPosition((vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
    }

    public void validateSize() throws EngineException {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(glfwId.get(), pWidth, pHeight);

            if (pWidth.get(0) != width || pHeight.get(0) != height)
                throw new EngineException("Failed to validate window resolution: Our=" + width + "x" + height
                        + ", GLFW=" + pWidth.get(0) + "x" + pHeight.get(0));
        }
    }

    public void enableVSync() {
        vSyncEnabled = true;
        glfwSwapInterval(1);
    }

    public void show() {
        glfwShowWindow(glfwId.get());
    }

    public boolean isShouldClose() {
        return glfwWindowShouldClose(glfwId.get());
    }

    public void setShouldClose(boolean isShouldClose) {
        glfwSetWindowShouldClose(glfwId.get(), isShouldClose);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        glfwSetWindowSize(glfwId.get(), width, height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        glfwSetWindowSize(glfwId.get(), width, height);
    }

    public enum WindowType {
        FULLSCREEN, BORDERED, BORDERLESS
    }
}

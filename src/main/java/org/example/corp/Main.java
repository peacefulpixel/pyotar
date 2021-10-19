package org.example.corp;

import org.example.corp.engine.*;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.WindowResizedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.FontParsingException;
import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.graphics.font.FontParser;
import org.example.corp.engine.graphics.font.bitmap.BitmapFontParser;
import org.example.corp.engine.res.BitmapFontResource;
import org.example.corp.engine.res.ResourceManager;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ExtraShaderProgram;

public class Main {

    public static void main(String[] args) throws FontParsingException {
        Layer layer = new Layer(DefaultShaderProgram.class);
        Layer exLayer = new Layer(ExtraShaderProgram.class);
        Stage stage = new Stage();
        stage.addLayer(layer);
        stage.addLayer(exLayer);
        Game game = new Game(stage);
        try {
            game.init();
        } catch (EngineException e) {
            e.printStackTrace();
        }

        Camera camera = new Camera();
        EventManager.addEventListener(WindowResizedEvent.class, e -> {
            camera.setSize(e.newWidth, e.newHeight);
        });

        Window.MAIN_WINDOW.setCamera(camera);
//        Window.MAIN_WINDOW.enableVSync();
//        world.addEntity(new InitialLogicalEntity());
        layer.addEntity(new MemoryTestLogicalEntity());
        exLayer.addEntity(new InitialLogicalEntity());

        BitmapFontParser parser = new BitmapFontParser(ResourceManager.get(BitmapFontResource.class, "res/font/default.fnt"));
        parser.parse();

        try {
            game.start();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}

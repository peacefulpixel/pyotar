package org.example.corp;

import org.example.corp.engine.*;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.WindowResizedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.FontParsingException;
import org.example.corp.engine.graphics.font.Font;
import org.example.corp.engine.graphics.font.bitmap.BitmapFontParser;
import org.example.corp.engine.res.BitmapFontResource;
import org.example.corp.engine.res.ResourceManager;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ExtraShaderProgram;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static Font FONT;

    public static void main(String[] args) throws FontParsingException {
        Stage stage = new Stage();
        Game game = new Game(stage);
        try {
            game.init();
        } catch (EngineException e) {
            e.printStackTrace();
        }

        Layer layer = new Layer(DefaultShaderProgram.class);
        Layer exLayer = new Layer(ExtraShaderProgram.class);
        stage.addLayer(layer);
        stage.addLayer(exLayer);

        EventManager.addEventListener(WindowResizedEvent.class, e -> {
            stage.doToEachLayer(l -> l.getCamera().setSize(e.newWidth, e.newHeight));
        });

        LinkedBlockingQueue<Integer> tq = new LinkedBlockingQueue<>();
        tq.add(1);
        tq.add(2);
        System.out.println(tq.poll() + " " + tq.poll());

//        Window.MAIN_WINDOW.setCamera(camera);
//        Window.MAIN_WINDOW.enableVSync();
//        world.addEntity(new InitialLogicalEntity());
        layer.addEntity(new MemoryTestLogicalEntity());
        exLayer.addEntity(new InitialLogicalEntity());

        BitmapFontParser parser = new BitmapFontParser(ResourceManager.get(BitmapFontResource.class, "res/font/default.fnt"));
        FONT = parser.parse();

        try {
            game.start();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}

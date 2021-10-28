package org.example.corp;

import org.example.corp.engine.Window;
import org.example.corp.engine.entity.LogicalEntity;
import org.example.corp.engine.entity.RenderableEntity;
import org.example.corp.engine.entity.TextEntity;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.FpsChangedEvent;
import org.example.corp.engine.event.impl.MouseMovedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.res.ResourceManager;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Logger;

public class MemoryTestLogicalEntity extends LogicalEntity {

    Logger logger = LoggerUtils.getLogger(MemoryTestLogicalEntity.class);
    long instanceCounter = 0;

    private InitialLogicalEntity initialLogicalEntity = new InitialLogicalEntity();

    @Override
    public void init() {
        EventManager.addEventListener(FpsChangedEvent.class, e -> {
            logger.info("FPS: " + e.newFps + " INSTANCES: " + instanceCounter);
        });
        RenderableEntity.DEFAULT_SCALING = 1.0f;
        try {
            TestGameEntity3 entity1 = new TestGameEntity3(
                    new Texture(ResourceManager.get(Image.class, "res/img/test_building1.png")));
            TestGameEntity3 entity2 = new TestGameEntity3(
                    new Texture(ResourceManager.get(Image.class, "res/img/test_building2.png")));

            entity1.setX(entity1.getX() - 50);
            entity1.setDepth(0.2f);
            entity2.setDepth(0.1f);
            entity2.setX(0.f);
            entity2.setY(0.f);
            entity2.setAxis(0, 0);
            layer.addEntities(entity1);
            layer.addEntities(entity2);
            TestGameEntity2 x = new TestGameEntity2();
//            Window.MAIN_WINDOW.getCamera().setSize(100, 50);
            TextEntity textEntity = Main.FONT.text("Hello world! Nigger lol lmao");
            textEntity.setX(100f);
            layer.addEntity(textEntity);
//            x.frame(8, 8, 16, 16);
            EventManager.addEventListener(MouseMovedEvent.class, e -> {
                if (e.prevX < e.x) {
                    x.setSize(x.getWidth() - 1, x.getHeight() - 1);
                } else if (e.x < e.prevX) {
                    x.setSize(x.getWidth() + 1, x.getHeight() + 1);
                }
            });
            layer.addEntity(x);
        } catch (EngineException e) {
            e.printStackTrace();
        }

//        try {
//            for (int x = 0; x < 2000; x++) {
//                Entity entity = new TestGameEntity();
//                world.addEntity(entity);
//                instanceCounter++;
//            }
//        } catch (EngineException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void loop() {
//        try {
//            world.destroyEntities(TestGameEntity.class);
//            for (int x = 0; x < 10; x++) {
//                Entity entity = new TestGameEntity();
//                world.addEntity(entity);
//                instanceCounter++;
//            }
//        } catch (EngineException e) {
//            e.printStackTrace();
//        }
        initialLogicalEntity.loop();
    }
}

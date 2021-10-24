package org.example.corp;

import org.example.corp.engine.entity.LogicalEntity;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.FpsChangedEvent;
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

        try {
            TestGameEntity3Vao entity1 = new TestGameEntity3Vao(
                    new Texture(ResourceManager.get(Image.class, "res/img/test_building1.png")));
            TestGameEntity3Vao entity2 = new TestGameEntity3Vao(
                    new Texture(ResourceManager.get(Image.class, "res/img/test_building2.png")));
            entity1.setX(entity1.getX() - 50);
            entity1.setDepth(0.2f);
            entity2.setDepth(0.1f);
            layer.addEntities(entity1);
            layer.addEntities(entity2);
            TestGameEntity2Vao x = new TestGameEntity2Vao();
            x.frame(8, 8, 16, 16);
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

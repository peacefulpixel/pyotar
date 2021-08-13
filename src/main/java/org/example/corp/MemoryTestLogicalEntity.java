package org.example.corp;

import org.example.corp.engine.entity.Entity;
import org.example.corp.engine.entity.LogicalEntity;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.FpsChangedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Logger;

public class MemoryTestLogicalEntity extends LogicalEntity {

    Logger logger = LoggerUtils.getLogger(MemoryTestLogicalEntity.class);
    long instanceCounter = 0;

    private InitialLogicalEntity initialLogicalEntity = new InitialLogicalEntity();

    @Override
    public void init() {
        EventManager.addEventListener(FpsChangedEvent.class, e -> {
            System.out.printf("FPS: %4d INSTANCES: %d\n", e.newFps, instanceCounter);
        });
    }

    @Override
    public void loop() {
        try {
            world.destroyEntities(TestGameEntity.class);
            for (int x = 0; x < 10; x++) {
                Entity entity = new TestGameEntity();
                world.addEntity(entity);
                instanceCounter++;
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
        initialLogicalEntity.loop();
    }
}

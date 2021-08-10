package org.example.corp;

import org.example.corp.engine.entity.LogicalEntity;
import org.example.corp.engine.exception.EngineException;

public class InitialLogicalEntity extends LogicalEntity {

    @Override
    public void init() {
        try {
            world.addEntity(new TestGameEntity());
            world.addEntity(new TestGameEntity());
            world.addEntity(new TestGameEntity2());
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}

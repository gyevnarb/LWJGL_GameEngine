package engine;

import java.util.List;

import engine.entities.Entity;

public interface IHud {
	List<Entity> getEntities();

    default void cleanup() {
    	List<Entity> entities = getEntities();
        for (Entity entity : entities) {
            entity.getMesh().cleanUp();
        }
    }
}

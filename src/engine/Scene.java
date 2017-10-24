package engine;

import java.util.*;

import engine.entities.Entity;
import engine.entities.SkyBox;
import engine.graphics.Mesh;
import engine.graphics.lights.SceneLight;

public class Scene {
	
	private List<Entity> uniqueEntities;
    
    private SkyBox skyBox;
    
    private SceneLight sceneLight;

    private Map<Mesh, List<Entity>> meshMap;
    
    public Scene() {
    	uniqueEntities = new ArrayList<>();
    	meshMap = new HashMap<>();
    }
    
    public List<Entity> getEntities() {
        return uniqueEntities;
    }

    public void addEntities(List<Entity> entities) {
        for (Entity e : entities) {
            Mesh mesh = e.getMesh();
            List<Entity> list = meshMap.get(mesh);
            if (list == null) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(e);
        }
    }
    
    public Map<Mesh, List<Entity>> getEntityMeshes() {
    	return meshMap;
    }
    
    public List<Entity> getUniqueEntites() {
    	return uniqueEntities;
    }
    
    public void addUniqueEntity(Entity entity) {
    	this.uniqueEntities.add(entity);
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
}

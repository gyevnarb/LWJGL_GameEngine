package game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import engine.*;
import engine.entities.Entity;
import engine.entities.TextEntity;
import engine.graphics.*;
import engine.utilities.*;

public class Hud implements IHud {

	private static final Font FONT = new Font("Times New Roman", Font.PLAIN, 30);

    private static final String CHARSET = "ISO-8859-1";
	
    private final List<Entity> entities;

    private final TextEntity statusTextEntity;
    
    private final Entity compassEntity;

    public Hud(String statusText) throws Exception {
    	entities = new ArrayList<Entity>();
        this.statusTextEntity = new TextEntity(statusText, new FontTexture(FONT, CHARSET));
        this.statusTextEntity.getMesh().getMaterial().setColor(new Vector3f(1, 1, 1));
        entities.add(statusTextEntity);
        
        // Create compass
        Mesh mesh = OBJLoader.loadMesh("/resources/models/compass.obj");
        Material material = new Material();
        material.setColor(new Vector3f(1, 0, 0));
        mesh.setMaterial(material);
        compassEntity = new Entity(mesh);
        compassEntity.setScale(40.0f);
        // Rotate to transform it to screen coordinates
        compassEntity.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
        entities.add(compassEntity);
    }

    public void rotateCompass(float deg) {
    	this.compassEntity.setRotation(0, 0, 180 + deg);
    }
    
    public void setStatusText(String statusText) {
        this.statusTextEntity.setText(statusText);
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    public void updateSize(Window window) {
        this.statusTextEntity.setPosition(10f, window.getHeight() - 50f, 0);
        this.compassEntity.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}

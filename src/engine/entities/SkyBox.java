package engine.entities;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Texture;
import engine.utilities.OBJLoader;

public class SkyBox extends Entity {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxTexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
	
}

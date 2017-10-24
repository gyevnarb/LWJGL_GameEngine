package engine.graphics;

import org.joml.Vector3f;

/**
 * Basic material to hold information about either the texture or the diffuse color, and the reflectance of an Entity
 * @author bgyevnar
 *
 */
public class Material {
	
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f color;
    
    private float reflectance;

    private Texture texture;
    
    /**
     * Default constructor the sets the diffuse color to the DEFAULT_COLOR = (1,1,1) and reflectance to 0
     */
    public Material() {
        color = DEFAULT_COLOR;
        reflectance = 0;
    }
    
    /**
     * Constructor to set the diffuse color and reflectance of the material
     * @param color The diffuse color of the material
     * @param reflectance The reflectance of the material
     */
    public Material(Vector3f color, float reflectance) {
        this();
        this.color = color;
        this.reflectance = reflectance;
    }

    /**
     * Constructor to set the texture of this material. Reflectance is set to 
     * @param texture The texture of the material
     */
    public Material(Texture texture) {
    	this.texture = texture;
    	this.reflectance = 0.0f;
    }
    
    /**
     * Constructor to set the texture and reflectance of the material
     * @param texture The Texture of the material
     * @param reflectance The reflectance of the material
     */
    public Material(Texture texture, float reflectance) {
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }

    /**
     * 
     * @return Vector3f containing the diffuse color of the material
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Method to set the diffuse color of the material
     * @param color The new color to set the diffuse color to
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * 
     * @return Float value of the reflectance
     */
    public float getReflectance() {
        return reflectance;
    }

    /**
     * Method to set the reflectance of the material
     * @param reflectance The new value of the reflectance
     */
    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }
    
    /**
     * 
     * @return True if the material has a texture specified, false otherwise 
     */
    public boolean isTextured() {
        return this.texture != null;
    }

    /**
     * 
     * @return The Texture associated with this material
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Method to set the texture associated with this material
     * @param texture The new texture of the material
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}

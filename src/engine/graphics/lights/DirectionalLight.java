package engine.graphics.lights;

import org.joml.Vector3f;

/**
 * A class to store information about a directional light
 * During the fragment shader processing, the color contribution of the directional light is calculated and is added to the total light contribution,
 * which is finally multiplied by the base color to produce the final fragColor
 * @author bgyevnar
 *
 */
public class DirectionalLight {

    private Vector3f color;

    private Vector3f direction;

    private float intensity;

    /**
     * Constructor to set the color, direction and intensity of the directional light
     * @param color Vector3f The color of the directional light
     * @param direction Vector3f The uniform direction of the directional light
     * @param intensity Float The intensity of the directional light 
     */
    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.setColor(color);
        this.setDirection(direction);
        this.setIntensity(intensity);
    }

    /**
     * Copy constructor
     * @param light DirectionalLight The light to copy the value of the instance variables from
     */
    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }

    /**
     * Method to get the color of the directional light
     * @return Vector3f The color of the directional light
     */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * Method to set the color of the directional light
	 * @param color Vector3f The new color of the directional light
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}

	/**
	 * Method to get the direction of the directional light
	 * @return Vcetor3f The direction of the directional light
	 */
	public Vector3f getDirection() {
		return direction;
	}

	/**
	 * Method to set the direction of the directional light
	 * @param direction Vector3f The new direction of the directional light
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	/**
	 * Method to get the intensity of the directional light
	 * @return float The intensity of the directional light
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * Method to set the intensity of the directional light
	 * @param intensity float The new intensity of the directional light
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
    
    
}
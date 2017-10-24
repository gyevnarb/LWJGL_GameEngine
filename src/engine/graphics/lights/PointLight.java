package engine.graphics.lights;

import org.joml.Vector3f;

/**
 * Class containing information about a point light
 * During the fragment shader processing, the color contribution of the point light is calculated and is added to the total light contribution,
 * which is finally multiplied by the base color to produce the final fragColor
 * @author bgyevnar
 *
 */
public class PointLight {

	private Vector3f color;

	private Vector3f position;

	protected float intensity;

	private Attenuation attenuation;

	/**
	 * Constructor to set the color, position and intensity of the point light
	 * @param color Vector3f The color of the point light
	 * @param position Vector3f The position of the point light
	 * @param intensity float The intensity of the point light
	 */
	public PointLight(Vector3f color, Vector3f position, float intensity) {
		attenuation = new Attenuation(1, 0, 0);
		this.color = color;
		this.position = position;
		this.intensity = intensity;
	}

	/**
	 * Constructor to set the point light and its attenuation. Calls the PointLight(color, pos, intensity) construcor
	 * @param attenuation Attenuation The attenuation of the point light
	 */
	public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
		this(color, position, intensity);
		this.attenuation = attenuation;
	}

	/**
	 * Copy constructor of the point light
	 * @param pointLight PointLight The point light to copy the values from
	 */
	public PointLight(PointLight pointLight) {
		this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()), pointLight.getIntensity(),
				pointLight.getAttenuation());
	}

	/**
	 * Method to get the color of the point light
	 * @return Vector3f The color of the point light
	 */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * Method to set the color of the point light
	 * @param color Vector3f The new color of the point light
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}

	/**
	 * Method to get the position of the point light
	 * @return Vector3f The position of the point light
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Method to set the position of the point light
	 * @param position Vector3f The new position of the point light
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Method to get the intensity of the point light
	 * @return float The new intensity of the point light
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * Method to set the intensity of the point light
	 * @param intensity float The new intensity of the point light
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/**
	 * Method to get the attenuation of the point light
	 * @return Attenuation The attenuation of the point light
	 */
	public Attenuation getAttenuation() {
		return attenuation;
	}

	/**
	 * Method to set the attenuation of the point light
	 * @param attenuation Attenuation The new attenuation of the point light
	 */
	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}

	/**
	 * Nested class to hold information about the attenuation of the point light.
	 * This class stores coefficients of the attenuation, the constant term, the linear coefficient and the exponential coefficient.
	 * During the fragment shader processing of the point light, the overall color contribution of the point light is divided by the attenuation, which is:
	 * A = constant + linear * distFromLightSource + exponential * distFromLightSource^2
	 * @author bgyevnar
	 *
	 */
	public static class Attenuation {

		private float constant;

		private float linear;

		private float exponent;

		/**
		 * Constructor to set the coefficients of the point light
		 * @param constant float The constant term of the attenuation
		 * @param linear float The linear coefficient of the attenuation
		 * @param exponent float The exponential coefficient of the attenuation
		 */
		public Attenuation(float constant, float linear, float exponent) {
			this.constant = constant;
			this.linear = linear;
			this.exponent = exponent;
		}

		/**
		 * Method to get the constant term of the attenuation
		 * @return float The constant term
		 */
		public float getConstant() {
			return constant;
		}

		/**
		 * Method to set the constant term of the attenuation
		 * @param constant float The new constant term
		 */
		public void setConstant(float constant) {
			this.constant = constant;
		}

		/**
		 * Method to get the linear coefficient of the attenuation
		 * @return float The linear coefficient of the attenuation
		 */
		public float getLinear() {
			return linear;
		}

		/**
		 * Method to set the linear coefficient of the attenuation
		 * @param linear float The new value of the linear coefficient
		 */
		public void setLinear(float linear) {
			this.linear = linear;
		}

		/**
		 * Method to get the exponential coefficient of the attenuation
		 * @return float The exponential coefficient
		 */
		public float getExponent() {
			return exponent;
		}

		/**
		 * Method to set the exponential coefficient of the attenuation
		 * @param exponent float The new value of the exponential coefficient
		 */
		public void setExponent(float exponent) {
			this.exponent = exponent;
		}
	}
}

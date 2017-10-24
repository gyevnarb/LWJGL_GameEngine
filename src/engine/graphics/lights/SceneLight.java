package engine.graphics.lights;

import java.util.*;

import org.joml.Vector3f;

public class SceneLight {
	
		private Vector3f ambientLight;
	    
	    private List<PointLight> pointLights;
	    
	    private List<SpotLight> spotLights;
	    
	    private DirectionalLight directionalLight;

	    public SceneLight() {
	    	ambientLight = new Vector3f(0, 0, 0);
	    	pointLights = new ArrayList<>();
	    	spotLights = new ArrayList<>();
	    	directionalLight = new DirectionalLight(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 0.0f);
	    }
	    
	    public Vector3f getAmbientLight() {
	        return ambientLight;
	    }

	    public void setAmbientLight(Vector3f ambientLight) {
	        this.ambientLight = ambientLight;
	    }

	    public List<PointLight> getPointLights() {
	        return pointLights;
	    }

	    public void addPointLight(PointLight pointLight) {
	    	this.pointLights.add(pointLight);
	    }
	    
	    public void addPointLights(List<PointLight> pointLights) {
	        this.pointLights.addAll(pointLights);
	    }

	    public List<SpotLight> getSpotLights() {
	        return spotLights;
	    }

	    public void addSpotLight(SpotLight spotLight) {
	    	this.spotLights.add(spotLight);
	    }
	    
	    public void addSpotLights(List<SpotLight> spotLights) {
	        this.spotLights.addAll(spotLights);
	    }

	    public DirectionalLight getDirectionalLight() {
	        return directionalLight;
	    }

	    public void setDirectionalLight(DirectionalLight directionalLight) {
	        this.directionalLight = directionalLight;
	    }
}

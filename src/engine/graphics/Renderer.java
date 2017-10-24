package engine.graphics;

import engine.IHud;
import engine.Scene;
import engine.Window;
import engine.entities.Entity;
import engine.entities.SkyBox;
import engine.graphics.lights.DirectionalLight;
import engine.graphics.lights.PointLight;
import engine.graphics.lights.SceneLight;
import engine.graphics.lights.SpotLight;
import engine.utilities.Transformation;
import engine.utilities.Utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

public class Renderer {
	
    private static final float FOV = (float)Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static final int MAX_POINT_LIGHTS = 5;

    private static final int MAX_SPOT_LIGHTS = 5;
    
    private Transformation transformation;
    
	private ShaderProgram sceneShaderProgram;
	
	private ShaderProgram hudShaderProgram;
	
	private ShaderProgram skyBoxShaderProgram; 
	
	private float specularPower;
	
	public Renderer() {       
        transformation = new Transformation();
        specularPower = 10.0f;
	}
	
	public void init(Window window) throws Exception {
		setupSkyBoxShader();
        setupSceneShader();
        setupHudShader();
	}
	
    public void setupSceneShader() throws Exception {
    	//Create a new shader program
    	sceneShaderProgram = new ShaderProgram();
    	sceneShaderProgram.createVertexShader(Utils.loadResource("/resources/shaders/vertex.vs"));
    	sceneShaderProgram.createFragmentShader(Utils.loadResource("/resources/shaders/fragment.fs"));
    	sceneShaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
    	sceneShaderProgram.createUniform("projectionMatrix");
    	sceneShaderProgram.createUniform("modelViewMatrix");
    	sceneShaderProgram.createUniform("texture_sampler");
        
        // Create uniform for material
    	sceneShaderProgram.createMaterialUniform("material");
        
        // Create lighting related uniforms
    	sceneShaderProgram.createUniform("specularPower");
    	sceneShaderProgram.createUniform("ambientLight");
    	sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLight("directionalLight");

    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource("/resources/shaders/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/resources/shaders/hud_fragment.fs"));
        hudShaderProgram.link();

        // Create uniforms for Orthographic-model projection matrix and base color
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("color");
        hudShaderProgram.createUniform("hasTexture");

    }
    
    private void setupSkyBoxShader() throws Exception {
    	skyBoxShaderProgram = new ShaderProgram();
    	skyBoxShaderProgram.createVertexShader(Utils.loadResource("/resources/shaders/skybox_vertex.vs"));
    	skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/resources/shaders/skybox_frag.fs"));
    	skyBoxShaderProgram.link();
    	
    	skyBoxShaderProgram.createUniform("modelViewMatrix");
    	skyBoxShaderProgram.createUniform("projectionMatrix");
    	skyBoxShaderProgram.createUniform("texture_sampler");
    	skyBoxShaderProgram.createUniform("ambientLight");
    }
    
    public void render(Window window, Camera camera, Scene scene, IHud hud) {
        clear();

        if (window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // Update projection and view matrices once per render cycle
        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateViewMatrix(camera);
        
        renderScene(window, camera, scene);
        
        renderUniqueEntities(window, camera, scene);
        
        renderSkybox(window, camera, scene);
        
        renderHud(window, hud);
    }
    
    public void clear() {
    	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void renderUniqueEntities(Window window, Camera camera, Scene scene) {
    	sceneShaderProgram.bind();
    	
    	 Matrix4f projectionMatrix = transformation.getProjectionMatrix();
         sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

         Matrix4f viewMatrix = transformation.getViewMatrix();
    	
         sceneShaderProgram.setUniform("texture_sampler", 0);
         
        for (Entity e : scene.getUniqueEntites()) {
        	Mesh mesh = e.getMesh();
        	Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(e, viewMatrix);
        	sceneShaderProgram.setUniform("material", mesh.getMaterial());
        	sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        	mesh.render();
        }
         
    	sceneShaderProgram.unbind();
    }
    
    public void renderScene(Window window, Camera camera, Scene scene) {

        sceneShaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix();

        renderLights(viewMatrix, scene.getSceneLight());

        sceneShaderProgram.setUniform("texture_sampler", 0);
        
        // Render each gameItem
        Map<Mesh, List<Entity>> mapMeshes = scene.getEntityMeshes();
		for (Mesh mesh : mapMeshes.keySet()) {
			sceneShaderProgram.setUniform("material", mesh.getMaterial());
			mesh.renderList(mapMeshes.get(mesh), (Entity e) -> {
				Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(e, viewMatrix);
				sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			});
		}

        sceneShaderProgram.unbind();
    }

    private void renderHud(Window window, IHud hud) {
        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (Entity entity : hud.getEntities()) {
            Mesh mesh = entity.getMesh();
            
            // Set orthographic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.buildOrthoProjModelMatrix(entity, ortho);
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            hudShaderProgram.setUniform("color", entity.getMesh().getMaterial().getColor());
            hudShaderProgram.setUniform("hasTexture", mesh.getMaterial().isTextured() ? 1 : 0); 

            // Render the mesh for this HUD item
            mesh.render();
        }

        hudShaderProgram.unbind();
    }
    
    private void renderSkybox(Window window, Camera camera, Scene scene) {
    	skyBoxShaderProgram.bind();
    	
    	skyBoxShaderProgram.setUniform("texture_sampler", 0);
    	
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        SkyBox skyBox = scene.getSkyBox();
        Matrix4f viewMatrix = transformation.getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbientLight());
                
        scene.getSkyBox().getMesh().render();
    	
    	skyBoxShaderProgram.unbind();
    }
    
    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

    	sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
    	sceneShaderProgram.setUniform("specularPower", specularPower);

    	List<PointLight> pointLights = sceneLight.getPointLights();
    	List<SpotLight> spotLights = sceneLight.getSpotLights();
    	
        // Process point lights
        int numLights = pointLights != null ? pointLights.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLights.get(i));
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("pointLights", currPointLight, i);
        }

        // Process spot lights
        numLights = spotLights != null ? spotLights.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLights.get(i));
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);

    }
    
    public void cleanup() {
        if (sceneShaderProgram != null) {
        	sceneShaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
        	hudShaderProgram.cleanup();
        }
        if (skyBoxShaderProgram != null) {
        	skyBoxShaderProgram.cleanup();
        }
    }
}
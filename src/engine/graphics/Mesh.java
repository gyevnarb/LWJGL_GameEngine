package engine.graphics;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;

import engine.entities.Entity;

/**
 * Class to create and render a mesh of triangles specified in a *.obj file
 * @author bgyevnar
 *
 */
public class Mesh {
	
    private final int vaoId;

    private final List<Integer> vboIdList;
    
    private final int vertexCount;
    
    private Material material;
    

    /**
     * Constructs a new mesh object of triangle faces given an array of vertices, texture coordinates, normal vector coordinates and element indices
     * @param positions float[] The array of vertices of the mesh. The array stores the vertices as a flattened linear list, therefore the length of this array should be 3 * numOfVerticies.
     * @param textCoords float[] The array of flattened texture coordinates, specifying a mapping from the given texture file in 2D to the 3D mesh. The size of the array should be 2 * numOfTextureCoords.
     * @param normals float[] The array of normal vectors, corresponding to every face, flattened into one array.
     * @param indicies int[] The array of face indices specifying the order of vertices of the triangle faces
     */
    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indicies) {
        FloatBuffer verticesBuffer = null;
        FloatBuffer textBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indiciesBuffer = null;
        try {
        	vboIdList = new ArrayList<Integer>();
        	vertexCount = indicies.length;
        	
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            textBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textBuffer.put(textCoords).flip();
            
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();

            indiciesBuffer = MemoryUtil.memAllocInt(indicies.length);
            indiciesBuffer.put(indicies).flip();
            
            
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            
            //Position VBO
            int posVboId = glGenBuffers();
            vboIdList.add(posVboId);
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //Index VBO
            int idxVboId = glGenBuffers();
            vboIdList.add(idxVboId);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);
            
            //Texture coordinates VBO
            int textVboId = glGenBuffers();
            vboIdList.add(textVboId);
            glBindBuffer(GL_ARRAY_BUFFER, textVboId);
            glBufferData(GL_ARRAY_BUFFER, textBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            
            //Normal vector coordinates VBO
            int normalVboId = glGenBuffers();
            vboIdList.add(normalVboId);
            glBindBuffer(GL_ARRAY_BUFFER, normalVboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            
            
        } finally {
            if (verticesBuffer  != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indiciesBuffer != null) {
            	MemoryUtil.memFree(indiciesBuffer);
            }
            if (textBuffer != null) {
            	MemoryUtil.memFree(textBuffer);
            }
            if (vecNormalsBuffer != null) {
            	MemoryUtil.memFree(vecNormalsBuffer);
            }
        }
    }
    
    private void initRender() {
        Texture texture = material.getTexture();
        if (texture != null) {
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    private void endRender() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    /**
     * Method to enable, draw and finally reset the VBOs and texture created in the init() method
     */
    public void render() {
        initRender();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        endRender();
    }

    public void renderList(List<Entity> entities, Consumer<Entity> consumer) {
        initRender();

        for (Entity entity : entities) {
            // Set up data required by gameItem
            consumer.accept(entity);
            // Render this game item
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        endRender();
    }
    
    /**
     * Method to get the material associated with this mesh
     * @return Material The material of the mesh
     */
    public Material getMaterial() {
		return material;
	}

    /**
     * Method to set the material of the object
     * @param material
     */
	public void setMaterial(Material material) {
		this.material = material;
	}

	public boolean hasMaterial() {
    	return material != null;
    }

	public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the position VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        
        Texture texture = material.getTexture();
        if (texture != null)
        	texture.cleanup();
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    
    public void deleteBuffers() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}

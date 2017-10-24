package engine.graphics;

import org.joml.Vector3f;

/**
 * A camera object, that keeps track of its position and rotation. 
 * The camera itself doesn't actually move, instead the whole world is transformed, so that it seems like the camera is moving
 * @author bgyevnar
 *
 */
public class Camera {

    private final Vector3f position;

    private final Vector3f rotation;

    /**
     * Default constructor, that sets the rotation and position of the camera to Vector3f(0.0, 0.0, 0.0)
     */
    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    /**
     * Constructor to set the position and rotation of the camera
     * @param position A Vector3f specifying the position of the camera
     * @param rotation A Vector3f specifying the rotation of the camera. Each component of the Vector3f specifies a rotation - in degrees - around the axis (x,y,z) respectively
     */
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * 
     * @return Vector3f of the position of the camera
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets the position of the camera, componentwise
     * @param x
     * @param y
     * @param z
     */
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Moves the camera with a specific offset down a given axis.
     * This function takes into account the rotation of the camera, so that the movement happens in the direction, that the camera is facing
     * @param offsetX Displacement along the x-axis
     * @param offsetY Displacement along the y-axis
     * @param offsetZ Displacement along the z-axis
     */
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    /**
     * 
     * @return Vector3f containing the rotation of the camera around the axis (x,y,z) respectively
     */
    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Method to set the rotation of the camera around the axis (x,y,z) respectively
     * @param x Rotation around the x-axis in degrees
     * @param y Rotation around the y-axis in degrees
     * @param z Rotation around the z-axis in degrees
     */
    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    /**
     * Changes the rotation of the camera by given offsets
     * @param offsetX
     * @param offsetY
     * @param offsetZ
     */
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}
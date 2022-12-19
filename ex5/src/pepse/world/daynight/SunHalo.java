package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final String HALO_TAG = "sky";
    private static final float HALO_RADIO=1.25f;
    private static final Color HALO_COLOR = new Color(255,255,0,20);
    public static GameObject create(GameObjectCollection gameObjects, int Layer,GameObject sun,Color color) {
        GameObject halo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(HALO_RADIO), new OvalRenderable(color));
        halo.setCenter(sun.getCenter());
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo,Layer);
        halo.setTag(HALO_TAG);
        return halo;
    }
}

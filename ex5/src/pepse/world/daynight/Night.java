package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final Color DRAKNESS_COLOR = Color.BLACK;
    private static final String SKY_TAG = "Darkness";

    public static GameObject create(GameObjectCollection gameObjects, int Layer,
                                    Vector2 windowDimensions, float cycleLength) {
        GameObject Darkness = new GameObject(Vector2.ZERO, windowDimensions, new RectangleRenderable(DRAKNESS_COLOR));
        Darkness.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(Darkness, Layer);
        Darkness.setTag(SKY_TAG);
        return Darkness;
    }
}

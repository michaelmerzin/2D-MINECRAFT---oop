package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final Color DRAKNESS_COLOR = Color.BLACK;
    private static final String NIGHT_TAG = "Darkness";
    private static final Float MID_NIGHT_OPACITY = 0.5f;

    public static GameObject create(GameObjectCollection gameObjects, int Layer,
                                    Vector2 windowDimensions, float cycleLength) {
        GameObject Darkness = new GameObject(Vector2.ZERO, windowDimensions, new RectangleRenderable(DRAKNESS_COLOR));
        Darkness.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(Darkness, Layer);
        Darkness.setTag(NIGHT_TAG);
        Transition<Float> transitionForDarkness = new Transition<Float>(Darkness, Darkness.renderer()::setOpaqueness,
                0f, MID_NIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return Darkness;
    }
}

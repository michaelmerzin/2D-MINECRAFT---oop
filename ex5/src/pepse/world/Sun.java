package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final String SUN_TAG = "sun";
    private static final float SUN_RADIO = 0.12f;
    private static final float SUN_HEIGHT=150;
    private static final float SUN_ROTATION = (float) (2*Math.PI);

    public static GameObject create(GameObjectCollection gameObjects, int Layer,
                                    Vector2 windowDimensions, float cycleLength) {
        float radius = windowDimensions.x() * SUN_RADIO;

        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(radius, radius), new OvalRenderable(SUN_COLOR));
        sun.setCenter(new Vector2(windowDimensions.x()/2,SUN_HEIGHT));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, Layer);
        sun.setTag(SUN_TAG);
        Consumer<Float> sunVector=(angel)->
                sun.setCenter(sun.getCenter().add(calcSunPosition(windowDimensions,angel)));
        Transition<Float> transitionForDarkness = new Transition<Float>(sun,sunVector,
                0f, SUN_ROTATION/2,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return sun;

    }

    private static Vector2 calcSunPosition(Vector2 windowDimensions,float angel)
    {
        float x = (float) (Math.cos(angel)+Math.sin(angel));
        float y = (float) (Math.cos(angel)-Math.sin(angel));
        return new Vector2(x,y);
    }


}

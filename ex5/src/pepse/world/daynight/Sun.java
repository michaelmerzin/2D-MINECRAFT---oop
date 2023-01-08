package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final String SUN_TAG = "sun";
    private static final float SUN_RADIO = 0.12f;
    private static final float SUN_RADIUS=375;
    private static final float SUN_HEIGHT=150;
    private static final float SUN_ROTATION = (float) (2*Math.PI);

    /**
     * Creates a new sun object which rotates around the world
     * @param gameObjects The game objects collection
     * @param Layer The layer of the sun
     * @param windowDimensions The dimensions of the window
     * @param cycleLength The length of the cycle
     * @return game object of the sun
     */
    public static GameObject create(GameObjectCollection gameObjects, int Layer,
                                    Vector2 windowDimensions, float cycleLength) {
        float radius = windowDimensions.x() * SUN_RADIO;

        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(radius, radius), new OvalRenderable(SUN_COLOR));
        sun.setCenter(new Vector2(windowDimensions.x()/2+200,SUN_HEIGHT));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, Layer);
        sun.setTag(SUN_TAG);
        rotateSun(sun, windowDimensions, cycleLength);
        return sun;

    }
    /**
     * Rotates the sun in a ellipse motion around the world
     * @param sun The sun
     * @param windowDimensions The dimensions of the window
     * @param cycleLength The length of the cycle
     */
    private static void rotateSun(GameObject sun, Vector2 windowDimensions, float cycleLength){
        Consumer<Float> sunVector=(angel)->
                sun.setCenter(windowDimensions.mult(0.5f).add(calcSunPosition(angel)));

        Transition<Float> transitionForDarkness = new Transition<Float>(sun,sunVector,
                (float)(-Math.PI/2),
                (float)(3*Math.PI/2),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP, null);

    }
    /**
     * Calculates the position of the sun
     * @param angel The angel of the sun
     * @return The position of the sun
     */
    private static Vector2 calcSunPosition(float angel)
    {
        float x = (float) (SUN_RADIUS*1.5*Math.cos(angel));
        float y = (float) (SUN_RADIUS*Math.sin(angel));
        return new Vector2(x,y);
    }


}

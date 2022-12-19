package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;

import java.util.Vector;

public class PepseGameManager extends GameManager {
    private final float cycleLength = 30;
    private static final Float MID_NIGHT_OPACITY = 0.5f;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        int seed = 5; //TODO
        Terrain ground = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), seed);
        ground.createInRange(0, (int) windowController.getWindowDimensions().x());
        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), cycleLength);
        Transition<Float> transitionForDarkness = new Transition<Float>(night, night.renderer()::setOpaqueness,
                0f, MID_NIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

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
import pepse.world.Sun;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.Vector;

public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    private static final Color HALO_COLOR=Color.RED;


    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        int seed = 5; //TODO
        Terrain ground = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), seed);
        ground.createInRange(0, (int) windowController.getWindowDimensions().x());
        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sun= Sun.create(gameObjects(),Layer.BACKGROUND+1,windowController.getWindowDimensions(),CYCLE_LENGTH);
        GameObject halo= SunHalo.create(gameObjects(),Layer.BACKGROUND,sun,HALO_COLOR);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

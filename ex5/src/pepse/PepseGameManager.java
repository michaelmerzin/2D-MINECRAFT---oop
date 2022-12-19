package pepse;

import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;

import java.util.Vector;

public class PepseGameManager extends GameManager {
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        int seed= 5; //TODO
        Terrain ground=new Terrain(gameObjects(),Layer.STATIC_OBJECTS,windowController.getWindowDimensions(),seed);
        ground.createInRange(0, (int) windowController.getWindowDimensions().x());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

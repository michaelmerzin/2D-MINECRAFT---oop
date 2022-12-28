package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.daynight.Sun;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private float renderPoint;
    private int renderChunk;
    private int leftRenderEnding;
    private int rightRenderEnding;
    private Terrain ground;
    private Tree trees;
    private Avatar avatar;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Terrain.setImageReader(imageReader);
        GameObject sky = Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND + 1, windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject halo = createHalo(sun);


        int seed = 5; //TODO
        this.ground = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), seed);
        this.trees = new Tree(gameObjects(), Layer.BACKGROUND + 13, Layer.BACKGROUND + 14,
                windowController.getWindowDimensions(), ground::groundHeightAt, seed);
        this.avatar = createAvatar(windowController, inputListener, imageReader);

        this.renderPoint = windowController.getWindowDimensions().x() / 2;
        this.leftRenderEnding = 0;
        this.rightRenderEnding = (int) windowController.getWindowDimensions().x();
        this.renderChunk = (int) windowController.getWindowDimensions().x();
        createWord(leftRenderEnding, rightRenderEnding);
    }


    private void createWord(int startX, int endX) {
        this.trees.createInRange(startX, endX);
        this.ground.createInRange(startX, endX);
    }

    private Avatar createAvatar(WindowController windowController,
                                UserInputListener inputListener, ImageReader imageReader) {

        float yOfx=ground.groundHeightAt(windowController.getWindowDimensions().x());
        Vector2 initialAvatarPos = new Vector2(windowController.getWindowDimensions().x(),
                                                windowController.getWindowDimensions().y()-yOfx);
        Avatar avatar = Avatar.create(gameObjects(),
                Layer.DEFAULT, initialAvatarPos, inputListener, imageReader);
        Vector2 relativeVector = initialAvatarPos.add(initialAvatarPos.mult(-1));
        setCamera(new Camera(avatar, relativeVector, windowController.getWindowDimensions(),
                        windowController.getWindowDimensions()));
        return avatar;
    }

    private GameObject createHalo(GameObject sun) {
        GameObject halo = SunHalo.create(gameObjects(), Layer.BACKGROUND + 11, sun, HALO_COLOR);
        halo.addComponent((deltaTime -> halo.setCenter(sun.getCenter())));
        return halo;
    }


    @Override
    public void update(float deltaTime) {

        float x = avatar.getCenter().x();

        if (x - this.renderPoint < this.leftRenderEnding) {
            createWord(this.leftRenderEnding - this.renderChunk, this.leftRenderEnding);
            this.leftRenderEnding = this.leftRenderEnding - this.renderChunk;
        }
        if (x + this.renderPoint > this.rightRenderEnding) {
            createWord(this.rightRenderEnding, this.rightRenderEnding + this.renderChunk);
            this.rightRenderEnding = this.rightRenderEnding + this.renderChunk;
        }
        super.update(deltaTime);
        gameObjects().update(deltaTime);

    }


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

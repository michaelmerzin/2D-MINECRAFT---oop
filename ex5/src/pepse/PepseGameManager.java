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
import pepse.world.*;
import pepse.world.daynight.Sun;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH_NIGHT = 30;
    private static final float CYCLE_LENGTH_SUN = 60;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 11;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int TREE_LAYER = Layer.BACKGROUND + 13;
    private static final int LEAVE_LAYER = Layer.BACKGROUND + 14;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private float renderedDistance;
    private int renderChunk;
    private int leftRenderEnding;
    private int rightRenderEnding;
    private Terrain ground;
    private Tree trees;
    private Avatar avatar;
    private ImageReader imageReader;
    private int seed;
    private Function<Float, Float> heightAtX;
    private WindowController windowController;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(gameObjects(), windowController.getWindowDimensions(), SKY_LAYER);
        GameObject night = Night.create(gameObjects(), NIGHT_LAYER, windowController.getWindowDimensions(), CYCLE_LENGTH_NIGHT);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowController.getWindowDimensions(), CYCLE_LENGTH_SUN);
        GameObject halo = createHalo(sun);

        this.windowController = windowController;
        this.imageReader = imageReader;
        this.seed = 9; //TODO
        this.ground = new Terrain(gameObjects(), GROUND_LAYER, windowController.getWindowDimensions(),
                seed, imageReader);
        this.trees = new Tree(gameObjects(), TREE_LAYER, LEAVE_LAYER,
                windowController.getWindowDimensions(), ground::groundHeightAt, seed);
        this.avatar = createAvatar(windowController, inputListener, imageReader);

        this.heightAtX = ground::groundHeightAt;
        this.renderedDistance =  roundUp((int) windowController.getWindowDimensions().x()) ;
        this.leftRenderEnding = roundDown((int)(0 - this.renderedDistance / 2));
        this.rightRenderEnding = roundUp((int) (windowController.getWindowDimensions().x() / 2 + this.renderedDistance));

        createWord(leftRenderEnding, rightRenderEnding);
        this.gameObjects().layers().shouldLayersCollide(LEAVE_LAYER, AVATAR_LAYER, true);
        this.gameObjects().layers().shouldLayersCollide(TREE_LAYER, AVATAR_LAYER, true);
        this.gameObjects().layers().shouldLayersCollide(LEAVE_LAYER, GROUND_LAYER, true);
//        this.gameObjects().layers().shouldLayersCollide(GROUND_LAYER, AVATAR_LAYER, true);
    }

    /**
     * @param windowController the window controller
     * @param inputListener    the input listener
     * @param imageReader      the image reader
     * @return the avatar
     */
    private Avatar createAvatar(WindowController windowController,
                                UserInputListener inputListener, ImageReader imageReader) {

        float yOfx = ground.groundHeightAt(windowController.getWindowDimensions().x() / 2);
        Vector2 initialAvatarPos = new Vector2(windowController.getWindowDimensions().x() / 2,
                windowController.getWindowDimensions().y() - yOfx - Block.SIZE);
        Avatar avatar = Avatar.create(gameObjects(),
                AVATAR_LAYER, initialAvatarPos, inputListener, imageReader);
        Vector2 relativeVector = initialAvatarPos.add(initialAvatarPos.mult(-1));
        setCamera(new Camera(avatar, relativeVector, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));


        return avatar;
    }

    /**
     * @param sun the sun
     * @return the halo
     */
    private GameObject createHalo(GameObject sun) {
        GameObject halo = SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, HALO_COLOR);
        halo.addComponent((deltaTime -> halo.setCenter(sun.getCenter())));
        return halo;
    }


    /**
     * @param startX inclusive
     * @param endX   exclusive
     */
    private void createWord(int startX, int endX) {

        this.trees.createInRange(startX, endX);
        this.ground.createInRange(startX, endX);
//        Cow.createInRange(this.gameObjects(), AVATAR_LAYER, this.imageReader, startX, endX, this.seed,
//                this.heightAtX, this.windowController.getWindowDimensions().y());

    }

    /**
     * @param /x the x position of the avatar
     * @param /xOffset offset from x
     */
    private void removeWord(float leftX, float rightX) {
        ArrayList<GameObject> removeList = new ArrayList<>();
        for (GameObject object : this.gameObjects()) {
            if (object.getTopLeftCorner().x() < leftX ||
                    object.getTopLeftCorner().x() > rightX) {
                removeList.add(object);
            }
        }

        for (GameObject object : removeList)
        {
            if (object.getTag().equals("leaves") )
               this.gameObjects().removeGameObject(object, LEAVE_LAYER);

            else if (object.getTag().equals("ground")) {
                this.gameObjects().removeGameObject(object, GROUND_LAYER);
            }
            else if (object.getTag().equals("trunk")) {
                this.gameObjects().removeGameObject(object, TREE_LAYER);
            }
        }
    }




    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float x = avatar.getTopLeftCorner().x();

        if(x - this.renderedDistance + Block.SIZE < leftRenderEnding)
        {
            createWord(leftRenderEnding-Block.SIZE, leftRenderEnding);
            rightRenderEnding -= Block.SIZE;
            leftRenderEnding -= Block.SIZE;
            removeWord(leftRenderEnding, rightRenderEnding);
        }
        if(x + this.renderedDistance - Block.SIZE > rightRenderEnding)
        {
            createWord(rightRenderEnding, rightRenderEnding + Block.SIZE);
            rightRenderEnding += Block.SIZE;
            leftRenderEnding += Block.SIZE;
            removeWord(leftRenderEnding, rightRenderEnding);

        }
    }

    private int roundDown(int start) {
        if (start < 0)
            return start - (Block.SIZE + (start % Block.SIZE));
        return start - (start % Block.SIZE);//54 -54%30=30

    }

    private int roundUp(int end) {
        if (end < 0)
            return end - (end % Block.SIZE);
        return end + Block.SIZE - (end % Block.SIZE);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();


    }

}

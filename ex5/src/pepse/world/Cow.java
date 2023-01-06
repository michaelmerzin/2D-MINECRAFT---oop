package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Cow extends GameObject {
    private static final float SIZE_RATIO_TO_BASIC = 50;
    private static final float VELOCITY_Y = -300;
    private static final float VELOCITY_X = 300;
    private static final float GRAVITY = 600;
    private static final String cowLeftImagePath = "animation/cow_left.png";
    private static final String cowRightImagePath = "animation/cow_right.png";
    private final ImageRenderable leftImage;
    private final ImageRenderable rightImage;

    /**
     * Creates a new cow.
     * @param pos The position of the cow.
     *            The cow will be created with a random velocity.
     *            The cow will be created with a random size.
     *            The cow will be created with a random image.
     *            The cow will be created with a random acceleration.
     *            The cow will be created with a random rotation.
     *            The cow will be created with a random rotation speed.
     * @param imageReader The image reader to use.
     *
     */
    private Cow(Vector2 pos, ImageReader imageReader) {
        super(pos, Vector2.ONES.multX(SIZE_RATIO_TO_BASIC * 1.25f).multY(SIZE_RATIO_TO_BASIC * 1.5f),
                new ImageRenderable(imageReader.readImage(cowLeftImagePath, true).getImage()));
        this.leftImage = new ImageRenderable(imageReader.readImage(cowLeftImagePath, true).getImage());
        this.rightImage = new ImageRenderable(imageReader.readImage(cowRightImagePath, true).getImage());
        transform().setAccelerationY(GRAVITY);
    }

    private static Cow create(GameObjectCollection gameObjects, int avatarLayer,
                              Vector2 topLeftCorner, ImageReader imageReader, Random rand) {
        Cow cow = new Cow(topLeftCorner, imageReader);
        gameObjects.addGameObject(cow, avatarLayer);
        cow.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        Cow.movementCow(cow, rand);
        return cow;
    }

    /**
     *
     * @param gameObjects the game objects collection
     * @param avatarLayer the layer of the avatar
     * @param imageReader the image reader
     * @param startX    the start x position
     * @param endX     the end x position
     * @param seed    the seed for the random
     * @param heightAtX the function that returns the height at x
     * @param heightOffset the offset of the height
     */
    public static void createInRange(GameObjectCollection gameObjects, int avatarLayer, ImageReader imageReader,
                                     int startX, int endX, int seed, Function<Float, Float> heightAtX,
                                     float heightOffset) {


        Random rand = new Random(Objects.hash(startX, endX, seed));
        int randomX = rand.nextInt(endX - startX) + startX;
        float randomY = heightOffset - heightAtX.apply((float) randomX) - Block.SIZE;
        Vector2 topLeftCorner = new Vector2(randomX, randomY);
        Cow.create(gameObjects, avatarLayer, topLeftCorner, imageReader, rand);



    }

    /**
     *
     * @param cow the cow
     * @param rand the random
     */
    private static void movementCow(Cow cow, Random rand) {
        new Transition<Float>(cow, (Float x) ->
        {
            if (x < 0){
                cow.renderer().setRenderable(cow.leftImage);
            }
            else {
                cow.renderer().setRenderable(cow.rightImage);
            }
            if (rand.nextInt(10) == 5 && cow.getVelocity().y() == 0) {
                cow.transform().setVelocityY(VELOCITY_Y);
                cow.physics().preventIntersectionsFromDirection(Vector2.ZERO);
            }
            cow.transform().setVelocityX(x);

        },
                -30f, 30f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                10, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        if (transform().getVelocity().y() == 0) {
            this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals("ground")) {
            this.setVelocity(Vector2.ZERO);
        }
    }
}

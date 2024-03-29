package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class Tree {
    private static final int BOUND = 100;
    private static final int UPPER_BOUND = 10;
    private static final int TREE_HEIGHT = 4;
    private static final Color COLOR_OF_TREE = new Color(100, 50, 20);
    private static final Color COLOR_OF_LEAVES = new Color(0, 102, 0);
    private static final int LEAVES_START = -1;
    private static final int LEAVES_END = 2;
    private  static final int LEAVES_HEIGHT = 7;
    private static final float CYCLE_LENGTH = 2;
    private static final String TRUNK_TAG = "trunk";
    private static final String LEAVES_TAG = "leaves";
    private static final int FADE_OUT_TIME = 15;
    private static final int FADE_IN_TIME = 3;
    private static final int FADE_START_TIME = 500;
    private static final int LEAVE_BACK_TO_PLACE_TIME = 30;
    private static final int LeaveFallSpeed = 30;
    private final Function<Float, Float> heightAtX;
    private final GameObjectCollection gameObjects;
    private final int treeLayer;
    private final float height;
    private final int leaveLayer;
    private Random rand;
    private final int seed;
    private final Vector2 windowDimensionsForLeaves;
    public static final Set<Integer> treesXcords = new HashSet<>();
    private static final RectangleRenderable renderTrunk=new RectangleRenderable(COLOR_OF_TREE);
    private static final RectangleRenderable renderLeaves=new RectangleRenderable(COLOR_OF_LEAVES);



    public Tree(GameObjectCollection gameObjects, int treeLayer, int leaveLayer,
                Vector2 windowDimensions, Function<Float, Float> heightAtX, int seed) {

        this.treeLayer = treeLayer;
        this.leaveLayer = leaveLayer;
        this.gameObjects = gameObjects;
        this.heightAtX = heightAtX;
        this.height = windowDimensions.y();
        this.rand = new Random();
        this.seed = seed;
        this.windowDimensionsForLeaves=windowDimensions;
    }

    /**
     * create tress and random position in the given range
     * @param minX the minimum x value of the range
     * @param maxX the maximum x value of the range
     */
    public void createInRange(int minX, int maxX) {
        int start = calcStart(minX);
        int end = calcEnd(maxX)-Block.SIZE;
        int randomNum;


        for (int x = start; x < end; x += Block.SIZE) {
            rand = new Random(Objects.hash(x, this.seed));
            randomNum = rand.nextInt(BOUND);
            if (randomNum <= UPPER_BOUND && (x/Block.SIZE)%3==0 && !treesXcords.contains(x)) {
                buildTree(x);
                treesXcords.add(x);
            }
        }
    }


    /**
     * builds a tree at the given x position
     * @param x the x position of the tree
     */
    private void buildTree(float x)
    {
        float bottomY = this.heightAtX.apply(x);
        Block block;
        for (float y = bottomY; y < bottomY + Block.SIZE * TREE_HEIGHT; y += Block.SIZE) {
            block = createTree(new Vector2(x, height - y + Block.SIZE));
            gameObjects.addGameObject(block, treeLayer);
            block.setTag(TRUNK_TAG);
        }
        buildLeaves(x);

    }
    /**
     * builds the leaves of the tree at the given x position
     * @param x the x position of the tree
     */
    private void buildLeaves(float x) {
        Block leave;
        float bottomY = this.heightAtX.apply(x);
        for (int leaveX = LEAVES_START; leaveX < LEAVES_END; leaveX++) {
            for (float h = bottomY; h < bottomY + Block.SIZE * LEAVES_HEIGHT; h += Block.SIZE) {
                leave = createLeave(new Vector2(x - leaveX * Block.SIZE,
                        height - h - (TREE_HEIGHT - 1) * Block.SIZE));
                leave.setTag(LEAVES_TAG);
                gameObjects.addGameObject(leave, leaveLayer);
                rotateLeave(leave);
                droppingLeave(leave);

            }
        }
    }

    /**
     * assign the dropping animation to the leave
     * @param leave the leave to drop
     */
    private void droppingLeave(Block leave){
        int leavesLifeTime = rand.nextInt(FADE_START_TIME); // after that time the leave starting to move and fade.
        Vector2 startingPlace=leave.getCenter().getImmutableCopy();
        new ScheduledTask(leave, leavesLifeTime, false, new Runnable() {
            @Override
            public void run() {
                leave.transform().setVelocityY(LeaveFallSpeed);
                leave.renderer().fadeOut(FADE_OUT_TIME);

                int leaveBackToPlaceTime=rand.nextInt(LEAVE_BACK_TO_PLACE_TIME);

                new ScheduledTask(leave,leavesLifeTime+ leaveBackToPlaceTime, true, new Runnable() {
                    @Override
                    public void run() {
                        getLeaveToTheStartingPlace(leave,startingPlace);
                    }
                }
                );
            }
        }
        );
    }
    /**
     * return the leave back to the tree he was part of
     * @param leave the leave return to his starting place
     * @param startingPlace the starting place of the leave
     */
    private void getLeaveToTheStartingPlace(Block leave, Vector2 startingPlace)
    {
        leave.renderer().fadeIn(FADE_IN_TIME);
        leave.setCenter(startingPlace);
        leave.setVelocity(Vector2.ZERO);
    }

    /**
     * rotates the leave to a random direction
     * @param leave the leave to rotate
     */
    private void rotateLeave(Block leave){
        Random rand = new Random();
        float initialValue;
        float finalValue;
        if(rand.nextBoolean())
        {
            initialValue=0f;
            finalValue=10;
        }
        else
        {
            initialValue=10f;
            finalValue=0;
        }
        new Transition<Float>(leave, leave.renderer()::setRenderableAngle,
                initialValue, finalValue,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                CYCLE_LENGTH, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    private int calcEnd(int end) {
        return end + Block.SIZE - (end % Block.SIZE);
    }

    private Block createTree(Vector2 cords) {
        return new Block(cords, renderTrunk);
    }
    private Block createLeave(Vector2 cords) {return new Block(cords, renderLeaves);}
}


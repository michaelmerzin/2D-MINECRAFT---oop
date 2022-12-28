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
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final int BOUND = 100;
    private static final int UPPER_BOUND = 10;
    private static final int TREE_HEIGHT = 4;
    private static final Color COLOR_OF_TREE = new Color(100, 50, 20);
    private static final Color COLOR_OF_LEAVES = new Color(0, 102, 0);
    private final Function<Float, Float> heightAtX;
    private final int LEAVES_START = -2;
    private final int LEAVES_END = 3;
    private final int LEAVES_HEIGHT = 7;
    private static final float CYCLE_LENGTH = 2;
    private static final String TRUNK_TAG = "trunk";
    private static final String LEAVES_TAG = "leaves";
    private static final int FADE_OUT_TIME = 15;
    private static final int FADE_START_TIME = 500;
    private static final int LEAVE_BACK_TO_PLACE_TIME = 30;
    private static final int LeaveFallSpeed = 30;
    private final GameObjectCollection gameObjects;
    private final int treeLayer;
    private final float height;
    private final int leaveLayer;
    private Random rand;
    private final int seed;
    private final Vector2 windowDimensionsForLeaves;


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


    public void createInRange(int minX, int maxX) {
        int start = calcStart(minX);
        int end = calcEnd(maxX);
        int randomNum;
        for (int x = start; x < end; x += Block.SIZE) {
            rand = new Random(Objects.hash(x, this.seed));
            randomNum = rand.nextInt(BOUND);
            if (randomNum <= UPPER_BOUND && (x/Block.SIZE)%2==0) {
                buildTree(x);
            }
        }
    }

    private void buildTree(float x)
    {
        float bottomY = this.heightAtX.apply(x);
//        bottomY=(int)(bottomY);
        Block block;
        RectangleRenderable render = new RectangleRenderable(COLOR_OF_TREE);
        for (float y = bottomY; y < bottomY + Block.SIZE * TREE_HEIGHT; y += Block.SIZE) {
            block = createBlock(new Vector2(x, height - y + Block.SIZE), render);
            gameObjects.addGameObject(block, treeLayer);
            block.setTag(TRUNK_TAG);
        }
        buildLeaves(x);

    }

    private void buildLeaves(float x) {
        Block leave;
        float bottomY = this.heightAtX.apply(x);
        RectangleRenderable render = new RectangleRenderable(COLOR_OF_LEAVES);
        for (int leaveX = LEAVES_START; leaveX < LEAVES_END; leaveX++) {
            for (float h = bottomY; h < bottomY + Block.SIZE * LEAVES_HEIGHT; h += Block.SIZE) {
                leave = createLeave(new Vector2(x - leaveX * Block.SIZE,
                                                height - h - (TREE_HEIGHT - 1) * Block.SIZE), render,this.heightAtX);
                leave.setTag(LEAVES_TAG);
                gameObjects.addGameObject(leave, leaveLayer);
                rotateLeave(leave);
                droppingLeave(leave);

            }
        }
    }

    private void droppingLeave(Block leave){


        int leavesLifeTime = rand.nextInt(FADE_START_TIME);// after that time the leave starting to move and fade.
        Vector2 startingPlace=leave.getCenter().getImmutableCopy();
        new ScheduledTask(leave, leavesLifeTime, false, new Runnable() {//TODO ADD IF FALL AGAIN OR NO
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
    private void getLeaveToTheStartingPlace(Block leave, Vector2 startingPlace)
    {
        leave.renderer().fadeIn(3);
        leave.setCenter(startingPlace);
        leave.setVelocity(Vector2.ZERO);
    }

    private void rotateLeave(Block leave){
        new Transition<Float>(leave, leave.renderer()::setRenderableAngle,
                0f, 10f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                CYCLE_LENGTH, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    private int calcEnd(int end) {
        return end + (end % Block.SIZE);
    }

    private Block createBlock(Vector2 cords, Renderable renderable) {
        return new Block(cords, renderable);
    }
    private Block createLeave(Vector2 cords, Renderable renderable,Function<Float, Float> heightAtX) {
        return new Block(cords, renderable,heightAtX, this.windowDimensionsForLeaves);
    }
}


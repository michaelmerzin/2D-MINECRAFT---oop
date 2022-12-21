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
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final Double UPPER_BOUND = 0.1;
    private static final int TREE_HEIGHT = 4;
    private static final Color COLOR_OF_TREE = new Color(100, 50, 20);
    private static final Color COLOR_OF_LEAVES = new Color(50, 200, 30);
    private final Function<Float, Float> heightAtX;
    private final int LEAVES_START = -2;
    private final int LEAVES_END = 3;
    private final int LEAVES_HEIGHT = 4;
    private static final float CYCLE_LENGTH = 3;

    private static final String TRUNK_TAG = "trunk";
    private static final String LEAVES_TAG = "leaves";
    private static final int FADE_OUT_TIME = 4;
    private final GameObjectCollection gameObjects;
    private final int treeLayer;
    private final float height;
    private final Random rand = new Random();

    public Tree(GameObjectCollection gameObjects, int treeLayer, Vector2 windowDimensions, Function<Float, Float> heightAtX) {
        this.treeLayer = treeLayer;
        this.gameObjects = gameObjects;
        this.heightAtX = heightAtX;
        this.height = windowDimensions.y();
    }


    public void createInRange(int minX, int maxX) {
        int start = calcStart(minX);
        int end = calcEnd(maxX);
        double rand;
        for (int x = start; x < end; x += Block.SIZE) {
            rand = Math.random();
            if (rand <= UPPER_BOUND) {
                buildTree(x);
            }
        }
    }

    private void buildTree(float x)//TODO should get height of the ground in that point and build the tree.
    {
        float bottomY = this.heightAtX.apply(x);
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
        Block block;
        float bottomY = this.heightAtX.apply(x);
        RectangleRenderable render = new RectangleRenderable(COLOR_OF_LEAVES);
        for (int w = LEAVES_START; w < LEAVES_END; w++) {
            for (float h = bottomY; h < bottomY + Block.SIZE * LEAVES_HEIGHT; h += Block.SIZE) {
                block = createBlock(new Vector2(x - w * Block.SIZE, height - h - (TREE_HEIGHT - 1) * Block.SIZE), render);
                gameObjects.addGameObject(block, treeLayer);

                Transition<Float> transitionForMoveLeaves = new Transition<Float>((GameObject) block, block.renderer()::setRenderableAngle,
                        0f, 10f,
                        Transition.CUBIC_INTERPOLATOR_FLOAT,
                        CYCLE_LENGTH, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

                Block finalBlock = block;
                int leavesLifeTime = rand.nextInt(10);// after that time the leave starting to move and fade.
                new ScheduledTask(block, leavesLifeTime, true, new Runnable() {
                    @Override
                    public void run() {
                        int leaves_Movement = rand.nextInt(20);
                        finalBlock.renderer().fadeOut(FADE_OUT_TIME);
                        finalBlock.transform().setVelocityY(30);
                    }
                }
                );
                block.setTag(LEAVES_TAG);

            }
        }
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
}

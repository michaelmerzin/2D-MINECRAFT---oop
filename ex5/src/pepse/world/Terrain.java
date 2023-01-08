package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.SimplexNoise;


import java.awt.*;

public class Terrain {


    private static final int TERRAIN_DEPTH = 20;
    private static final String BLOCK_TAG = "ground";
    private static final double NOISE_PERSISTENCE = 2.1 ;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private static final Color COLOR_OF_LEAVES = new Color(0, 102, 0);
    private static final Color COLOR_OF_TREE = new Color(100, 50, 20);
    private static final RectangleRenderable renderDirt = new RectangleRenderable(COLOR_OF_TREE);
    private static final RectangleRenderable renderGrass = new RectangleRenderable(COLOR_OF_LEAVES);
    private final float height;
    private final int seed;
    private final SimplexNoise noise;

    /**
     * Creates a new terrain
     * @param gameObjects The game objects collection
     * @param groundLayer The layer of the ground
     * @param windowDimensions The dimensions of the window
     * @param seed The seed for the terrain
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.groundLayer = groundLayer;
        this.gameObjects = gameObjects;
        this.height = windowDimensions.y();
        this.seed = seed;
        this.noise = new SimplexNoise(TERRAIN_DEPTH, NOISE_PERSISTENCE, seed);


    }

    public float groundHeightAt(float x) {
        return (float) noise.getNoise((int) x);
    }

    /**
     * Creates a new terrain with the given seed using noise method to generate the terrain
     * @param minX The minimum x coordinate
     * @param maxX The maximum x coordinate
     */
    public void createInRange(int minX, int maxX) {
        int startX = calcStart(minX);
        int endX = calcEnd(maxX);
        Block block;
        int y;
        boolean onSurface = true;

        for (int x = startX; x < endX; x += Block.SIZE) {
            y = calcTopY(x);
            onSurface = true;
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                y -= Block.SIZE;
                if (onSurface) {
                    block = createBlock(new Vector2(x, height - y), renderGrass);
                } else {
                    block = createBlock(new Vector2(x, height - y), renderDirt);
                }
                gameObjects.addGameObject(block, groundLayer);
                block.setTag(BLOCK_TAG);
                onSurface = false;
            }

        }
    }

    /**
     * the highest y coordinate of the terrain
     * @param x the x coordinate
     * @return y coordinate
     */
    private int calcTopY(int x) {
        return (int) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
    }


    /**
     * calculates the start x coordinate
     * @param start number to calculate by
     * @return round down to the nearest multiple of block size
     */
    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    /**
     * calculates the start x coordinate
     * @param end number to calculate by
     * @return round up to the nearest multiple of block size
     */
    private int calcEnd(int end) {
        return end + Block.SIZE - (end % Block.SIZE);
    }

    private Block createBlock(Vector2 cords, Renderable renderable) {
        return new Block(cords, renderable);
    }
}

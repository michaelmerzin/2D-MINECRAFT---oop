package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.SimplexNoise;
import pepse.SimplexNoiseOctave;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 73);
    private static final String GRASS_IMAGE = "animation/grass.png";
    private static final String DIRT_IMAGE = "animation/dirt.jpg";
    private static final double GROUND_RADIO = 0.3;
    private static final int TERRAIN_DEPTH = 20;
    private static final String BLOCK_TAG = "ground";
    private static final double NOISE_PERSISTENCE = 2.1 ;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
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
                   int seed, ImageReader imageReader) {
        this.groundLayer = groundLayer;
        this.gameObjects = gameObjects;
        this.groundHeightAtX0 = (float) (windowDimensions.y() * GROUND_RADIO);
        this.height = windowDimensions.y();
        this.seed = seed;
        this.noise = new SimplexNoise(TERRAIN_DEPTH, NOISE_PERSISTENCE, seed);


    }

    public float groundHeightAt(float x) {
        return (float) noise.getNoise((int) x);
    }

    /**
     * Creates a new terrain
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

    private int calcTopY(int x) {
        return (int) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
    }

    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    private int calcEnd(int end) {
        return end + Block.SIZE - (end % Block.SIZE);
    }

    private Block createBlock(Vector2 cords, Renderable renderable) {
        return new Block(cords, renderable);
    }
}

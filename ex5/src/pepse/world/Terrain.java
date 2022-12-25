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
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private final float height;
    private final int seed;
    private final SimplexNoise noise;
    private static ImageReader imageReader = null;


    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.groundLayer = groundLayer;
        this.gameObjects = gameObjects;
        this.groundHeightAtX0 = (float) (windowDimensions.y() * GROUND_RADIO);
        this.height = windowDimensions.y();
        this.seed = seed;
        this.noise = new SimplexNoise(TERRAIN_DEPTH, 2.45, seed);


    }

    public static void setImageReader(ImageReader imageReader) {
        Terrain.imageReader = imageReader;
    }

    public float groundHeightAt(float x) {
        return (float) noise.getNoise((int) x);
//        return groundHeightAtX0; //TODO

    }

    public void createInRange(int minX, int maxX) {
        int startX = calcStart(minX);
        int endX = calcEnd(maxX);
        Block block;
        int y;
        boolean areWeOnTheSurface = true;
        ImageRenderable render_dert =
                new ImageRenderable(imageReader.readImage(DIRT_IMAGE, true).getImage());;
        ImageRenderable render_grass =
                new ImageRenderable(imageReader.readImage(GRASS_IMAGE, true).getImage());
        for (int x = startX; x < endX; x += Block.SIZE) {
            y = calcTopY(x);
            areWeOnTheSurface = true;
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                y -= Block.SIZE;
                if (areWeOnTheSurface) {
                    block = createBlock(new Vector2(x, height - y), render_grass);
                } else {
                    block = createBlock(new Vector2(x, height - y), render_dert);
                }
                gameObjects.addGameObject(block, groundLayer);
                block.setTag(BLOCK_TAG);
                areWeOnTheSurface = false;
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
        return end + (end % Block.SIZE);
    }

    private Block createBlock(Vector2 cords, Renderable renderable) {
        return new Block(cords, renderable);
    }
}

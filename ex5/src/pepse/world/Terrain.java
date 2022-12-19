package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR=new Color(212,123,73);
    private static final float GROUND_RADIO=1/3;
    private static final String BLOCK_TAG="ground";
    private  final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;



    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions,int seed)
    {
        this.groundLayer=groundLayer;
        this.gameObjects=gameObjects;
        this.groundHeightAtX0=windowDimensions.y()*GROUND_RADIO;
    }
    public float GroundHeightAt(float x)
    {return groundHeightAtX0;}
    public void createInRange(int minX,int maxX)
    {
        RectangleRenderable render=new RectangleRenderable(BASE_GROUND_COLOR);
        Block block = new Block(new Vector2(minX,maxX),render);
        gameObjects.addGameObject(block, Layer.STATIC_OBJECTS);
        block.setTag(BLOCK_TAG);
    }
}

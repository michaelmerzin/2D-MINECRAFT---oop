package pepse.world;


import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;
import java.util.function.Function;

public class Block extends GameObject {

    public static final int SIZE=30;
    private static final int LEAVE_BACK_TO_PLACE_TIME = 300;
    private Random rand;
    private Vector2 blockCenter;
    private  Function<Float, Float> heightAtX;
    private  Vector2 windowDimensions;
    public Block(Vector2 topLeftCorner, Renderable renderable, Function<Float, Float> heightAtX, Vector2 windowDimensions)
    {
        super(topLeftCorner,Vector2.ONES.mult(SIZE),renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.blockCenter=super.getCenter();
        this.heightAtX=heightAtX;
        this.windowDimensions=windowDimensions;
    }

    public Block(Vector2 topLeftCorner, Renderable renderable)
    {
        super(topLeftCorner,Vector2.ONES.mult(SIZE),renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.blockCenter=super.getCenter();
    }
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        if(this.getTag().equals("leaves"))
        {
            if(this.getVelocity().y()!=0) {
                if (this.getCenter().y() >= windowDimensions.y()-heightAtX.apply(this.getCenter().x())+Block.SIZE) {
                    this.setVelocity(Vector2.ZERO);
                }
            }
        }

    }
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(this.getTag().equals("leaves")&&!(other.getTag().equals("ground")))
        {
            return false;
        }
        return true;
    }
    @Override
    public void onCollisionEnter(GameObject other, Collision collision)
    {
        if(this.getTag().equals("leaves")&&other.getTag().equals("ground"))
        {
            this.setVelocity(Vector2.ZERO);
        }
        super.onCollisionEnter(other,collision);
    }



}

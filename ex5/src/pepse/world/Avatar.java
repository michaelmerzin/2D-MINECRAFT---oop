package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject
{
    private static final String avatarImagePath = "animation/avatarImage.jpg";
    private static final float SIZE_RATIO_TO_BASIC = 50;
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 600;
    private static final String AVATAR_TAG= "Avatar";
    private final UserInputListener inputListener;
    private final ImageReader imageReader;

    private Avatar(Vector2 pos, UserInputListener inputListener,ImageReader imageReader)
    {
        super(pos, Vector2.ONES.mult(SIZE_RATIO_TO_BASIC),
                new ImageRenderable(imageReader.readImage(avatarImagePath,true).getImage()));
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;

    }

    public static Avatar create(GameObjectCollection gameObjects, int avatarLayer,
                                Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader)
    {
        Avatar avatar=new Avatar(topLeftCorner,inputListener,imageReader);
        gameObjects.addGameObject(avatar,avatarLayer);
        avatar.setTag(AVATAR_TAG);
        //avatar cant enter the inside terrain
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        return avatar;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleMovement();
    }

    private void handleMovement(){
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    ()->physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            transform().setVelocityY(VELOCITY_Y);
    }
}

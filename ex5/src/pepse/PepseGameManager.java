package pepse;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

import java.util.Vector;

public class PepseGameManager extends GameManager {
        public static void main(String[] args) {
            new PepseGameManager().run();
        }
        @Override
        public void initializeGame(ImageReader imageReader,
                                   SoundReader soundReader, UserInputListener inputListener,
                                   WindowController windowController)
        {}

}

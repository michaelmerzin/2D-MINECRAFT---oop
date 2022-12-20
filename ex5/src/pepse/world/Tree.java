package pepse.world;

import java.awt.*;

public class Tree {
    private static final Double UPPER_BOUND = 0.1;
    private static final int TREE_HEIGHT = 4;
    private static final Color COLOR_OF_TREE = new Color(100, 50, 20);
    private static final Color COLOR_OF_LEAVES = new Color(50, 200, 30);

    public void createInRange(int minX, int maxX) {
        int start = calcStart(minX);
        int end = calcEnd(maxX);
        double rand;
        for (int i = start; i < end; i += Block.SIZE) {
            rand = Math.random();
            if (rand <= UPPER_BOUND) {
                buildTree();
            }
        }


    }

    private void buildTree()//TODO should get height of the ground in that point and build the tree.
    {

    }

    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    private int calcEnd(int end) {
        return end + (end % Block.SIZE);
    }
}

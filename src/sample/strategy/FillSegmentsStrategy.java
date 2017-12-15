package sample.strategy;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Anastasia on 21.11.2017.
 */
public class FillSegmentsStrategy extends FillStrategyAbstract {

    public FillSegmentsStrategy(double xSize, double ySize) {
        super(xSize, ySize);
    }

    @Override
    protected void generatePoints(int xStart, int yStart) {
        Queue<Point> queue = new ArrayDeque<>();
        ArrayList<Point> dots = new ArrayList<>();
        queue.add(new Point(xStart, yStart));

        while (queue.size() > 0) {
            Point from = queue.poll();

            int fromX = from.x, fromY = from.y;
            while (fromX > 0 && canFill(fromX - 1, fromY)) fromX--;

            boolean topSegmentOpen = false, bottomSegmentOpen = false;
            for (int x = fromX; canFill(x, fromY); x++) {
                if (canFill(x, fromY - 1) && !bottomSegmentOpen) {
                    queue.add(new Point(x, fromY - 1));
                }

                bottomSegmentOpen = canFill(x, fromY - 1);

                if (canFill(x, fromY + 1) && !topSegmentOpen) {
                    queue.add(new Point(x, fromY + 1));
                }

                topSegmentOpen = canFill(x, fromY + 1);

                colors[x][fromY] = color;
                addPoint(x, fromY);
                dots.add(new Point(x, fromY));
            }

        }
    }
}

package sample.strategy;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Anastasia on 21.11.2017.
 */
public class FillBfsStrategy extends FillStrategyAbstract {

    public FillBfsStrategy(double xSize, double ySize) {
        super(xSize, ySize);
    }

    protected final static int[][] STEPS = {
            { -1, 0 }, { 1, 0 },
            { 0, -1 }, { 0, 1 }
    };

    @Override
    protected void generatePoints(int xStart, int yStart) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.add(new Point(xStart, yStart));
        colors[xStart][yStart] = color;

        while (queue.size() > 0) {
            Point from = queue.poll();
            addPoint(from);

            int fromX = from.x, fromY = from.y;

            for (int[] step : STEPS) {
                int toX = fromX + step[0];
                int toY = fromY + step[1];

                if (canFill(toX, toY)) {
                    queue.add(new Point(toX, toY));
                    colors[toX][toY] = color;
                }
            }
        }
    }
}

package sample.strategy;

import java.awt.*;
import java.util.List;

/**
 * Created by Anastasia on 21.11.2017.
 */
public abstract class FillStrategyAbstract extends DrawStrategyAbstract {

    protected int xSize, ySize;

    protected int color;
    protected int[][] colors;

    protected FillStrategyAbstract(double xSize, double ySize) {
        this.xSize = (int)xSize;
        this.ySize = (int)ySize;

        this.color = 0;
        this.colors = new int[this.xSize][this.ySize];
    }

    private boolean checkIndex(int index, int size) {
        return (0 <= index && index < size);
    }

    protected boolean canFill(int x, int y) {
        return checkIndex(x, xSize) && checkIndex(y, ySize) && colors[x][y] != color;
    }

    @Override
    protected void generatePoints(List<Point> basePoints, double... coeffs) throws DrawStrategyException {
        ++color;
        for (Point sidePoint : basePoints) {
            if (canFill(sidePoint.x, sidePoint.y)) {
                colors[sidePoint.x][sidePoint.y] = color;
            }
        }

        int xStart = (int)coeffs[0], yStart = (int)coeffs[1];
        generatePoints(xStart, yStart);
    }

    protected abstract void generatePoints(int xStart, int yStart);
}

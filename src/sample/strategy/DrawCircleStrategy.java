package sample.strategy;

import java.awt.*;
import java.util.List;

/**
 * Created by Anastasia on 20.11.2017.
 */
public class DrawCircleStrategy extends DrawStrategyAbstract {

    public static final DrawCircleStrategy INSTANCE = new DrawCircleStrategy();

    private DrawCircleStrategy(){
        super();
    }

    @Override
    protected void generatePoints(List<Point> basePoints, double... coeffs) throws DrawStrategyException {
        if (coeffs.length == 0) {
            throw new DrawStrategyException("Не введен радиус");
        }

        double doubleRadius = coeffs[0];
        if (Double.isNaN(doubleRadius) || Double.isInfinite(doubleRadius) || doubleRadius <= 1) {
            throw new DrawStrategyException("Радиус должен быть положительным числом не менее 1");
        }

        int radius = (int)doubleRadius;

        for (Point center : basePoints) {
            generateCircle(center, radius);
        }
    }

    private void generateCircle(Point center, int radius) {
        int delta = 3 - 2 * radius;
        for (int x = 0, y = radius; x <= y; x++) {
            generatePoint8(center.x, center.y, x, y);

            if (delta <= 0) {
                delta += 4 * x + 6;
            } else {
                delta += 4 * (x - y) + 10;
                y--;
            }
        }
    }

    private void generatePoint8(int centerX, int centerY, int x, int y) {
        int[] signs = { -1, 1 };
        for (int mask = 0; mask < 4; ++mask) {
            int firstSign = signs[mask % 2];
            int secondSign = signs[mask / 2];

            addPoint(centerX + x * firstSign, centerY + y * secondSign);
            addPoint(centerX + y * firstSign, centerY + x * secondSign);
        }
    }
}

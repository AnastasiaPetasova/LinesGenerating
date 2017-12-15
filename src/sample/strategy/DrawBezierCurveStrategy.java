package sample.strategy;

import java.awt.*;
import java.util.List;

/**
 * Created by Anastasia on 21.11.2017.
 */
public class DrawBezierCurveStrategy extends DrawStrategyAbstract {

    public static final DrawBezierCurveStrategy INSTANCE = new DrawBezierCurveStrategy();

    private DrawBezierCurveStrategy() {
        super();
    }

    @Override
    protected void generatePoints(java.util.List<Point> basePoints, double... coeffs) throws DrawStrategyException {
        if (coeffs.length == 0) {
            throw new DrawStrategyException("Количество точек кривой Безье не задано");
        }

        double doubleSize = coeffs[0];
        if (Double.isNaN(doubleSize) || Double.isInfinite(doubleSize) || doubleSize <= 2) {
            throw new DrawStrategyException("Количество точек кривой Безье должно быть положительным числом не менее 2");
        }

        int basePointsCount = basePoints.size();

        double[][] c = new double[basePointsCount][basePointsCount];
        for (int i = 0; i < basePointsCount; ++i) {
            c[i][0] = c[i][i] = 1;
            for (int j = 1; j < i; ++j) {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
            }
        }

        int size = (int) doubleSize;

        double dt = 1.0 / (size - 1);
        for (double t = 0; t <= 1; t += dt) {
            double x = 0;
            double y = 0;

            for (int i = 0; i < basePointsCount; ++i) {
                double multiplier = c[basePointsCount - 1][i];
                multiplier *= Math.pow(t, i);
                multiplier *= Math.pow(1 - t, basePointsCount - 1 - i);

                Point basePoint = basePoints.get(i);

                x += multiplier * basePoint.x;
                y += multiplier * basePoint.y;
            }

            addPoint(x, y);
        }

        connectPoints(outPoints);
        connectPoints(basePoints);
    }

    private void connectPoints(List<Point> basePoints) throws DrawStrategyException {
        DrawStrategy connector = DrawPolylineStrategy.POLYLINE_INSTANCE;
        List<Point> connectedPoints = connector.generate(basePoints);

        addPoints(connectedPoints);
    }
}

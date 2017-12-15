package sample.strategy;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Anastasia on 20.11.2017.
 */
public abstract class DrawStrategyAbstract implements DrawStrategy {

    protected List<Point> outPoints;

    protected void addPoints(Collection<Point> points) {
        outPoints.addAll(points);
    }

    protected void addPoint(Point point) {
        addPoint(point.x, point.y);
    }

    protected void addPoint(double x, double y) {
        addPoint((int)x, (int)y);
    }

    protected void addPoint(int x, int y) {
        outPoints.add(new Point(x, y));
    }

    protected abstract void generatePoints(List<Point> basePoints, double... coeffs) throws DrawStrategyException;

    @Override
    public List<Point> generate(List<Point> basePoints, double... coeffs) throws DrawStrategyException {
        this.outPoints = new ArrayList<>();

        generatePoints(basePoints, coeffs);

        return outPoints;
    }
}

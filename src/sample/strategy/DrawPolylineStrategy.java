package sample.strategy;

import java.awt.Point;
import java.util.List;

/**
 * Created by Anastasia on 06.11.2017.
 */
public class DrawPolylineStrategy extends DrawStrategyAbstract {

    public static final DrawPolylineStrategy
        POLYLINE_INSTANCE = new DrawPolylineStrategy(false),
        POLYGON_INSTANCE = new DrawPolylineStrategy(true);

    private boolean isPolygon;

    private DrawPolylineStrategy(boolean isPolygon) {
        super();
        this.isPolygon = isPolygon;
    }

    @Override
    protected void generatePoints(List<Point> basePoints, double... coeffs) {
        for (int i = 0; i < basePoints.size() - 1; i++) {
            Point from = basePoints.get(i);
            Point to = basePoints.get(i + 1);

            generateSegment(from, to);
        }

        if (isPolygon) {
            Point from = basePoints.get(basePoints.size() - 1);
            Point to = basePoints.get(0);

            generateSegment(from, to);
        }
    }

    private void generateSegment(Point from, Point to) {
        if (from.x > to.x || from.x == to.x && from.y > to.y){
            Point tmp = to;
            to = from;
            from = tmp;
        }

        if (from.x == to.x){
            for (int y = from.y; y <= to.y; y++){
                addPoint(from.x, y);
            }
        } else {
            double dx = to.x - from.x;
            double dy = to.y - from.y;

            int sign = (int) Math.signum(dy);

            double tg = Math.abs(dy / dx);
            int yDelta = (int) Math.floor(tg);

            double m = tg - yDelta;
            double error = tg - 0.5;

            addPoint(from);

            for (int x = from.x, y = from.y; x <= to.x; x++) {
                for (int i = 0; i < yDelta; ++i) {
                    int cmpResult = Integer.compare(to.y, y);
                    if (cmpResult != sign) break;

                    addPoint(x, y);
                    y += sign;
                }

                if (error >= 0) {
                    y += sign;
                    error += m - 1;
                } else {
                    error += m;
                }

                addPoint(x, y);
            }

            addPoint(to);
        }
    }
}

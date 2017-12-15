package sample.strategy;

import java.awt.*;
import java.util.List;

public interface DrawStrategy {
        
      List<Point> generate(List<Point> basePoints, double... coeffs) throws DrawStrategyException;
}
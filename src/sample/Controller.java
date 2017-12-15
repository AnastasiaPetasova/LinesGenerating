package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.strategy.*;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Canvas canvas;

    @FXML
    TextField input_textField;

    @FXML
    Button clear_button;

    @FXML
    Button polygon_button;

    @FXML
    Button circle_button;

    @FXML
    Button bezier_button;

    @FXML
    RadioButton point_click_button;

    @FXML
    RadioButton dfs_coloring_button;

    @FXML
    RadioButton segment_coloring_button;

    List<Point> clickedPoints = new ArrayList<>();
    List<Point> drawnPoints = new ArrayList<>();

    DrawStrategy selectedFillStrategy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFields();
        initCanvas();
        initButtons();
    }

    private void initFields() {
        this.clickedPoints = new ArrayList<>();
    }

    private void initCanvas() {
        initCanvasSize();

        initCanvasListeners();

        clearCanvas();
    }

    private void initCanvasSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        canvas.setWidth(screenSize.width - 200);
        canvas.setHeight(screenSize.height - 200);
    }

    private void initCanvasListeners() {
        canvas.setOnMouseClicked(event -> {
            int clickedX = (int)event.getX(), clickedY = (int)event.getY();

            clearCanvas();

            if (selectedFillStrategy == null) {
                Point point = new Point(clickedX, clickedY);
                clickedPoints.add(point);

                drawPoints(clickedPoints, Color.BLACK);
            } else {
                drawPoints(drawnPoints, Color.BLACK);
                drawPoints(clickedPoints, Color.RED);

                List<Point> blockedPoints = new ArrayList<>();
                for (Point drawnPoint : drawnPoints) {
                    for (int x = drawnPoint.x - POINT_SIZE; x <= drawnPoint.x + POINT_SIZE; ++x) {
                        for (int y = drawnPoint.y - POINT_SIZE; y <= drawnPoint.y + POINT_SIZE; ++y) {
                            int dx = (x - drawnPoint.x);
                            int dy = (y - drawnPoint.y);

                            if (dx * dx + dy * dy <= POINT_SIZE * POINT_SIZE) {
                                blockedPoints.add(new Point(x, y));
                            }
                        }
                    }
                }

                try {
                    List<Point> fillPoints = selectedFillStrategy.generate(blockedPoints, clickedX, clickedY);
                    drawPoints(fillPoints, Color.PINK);
                } catch (DrawStrategyException e) {
                    showExceptionAlert(e);
                }
            }
        });
    }

    private GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }

    private static final int POINT_SIZE = 1;

    private void drawPoints(List<Point> points, Color color) {
        for (Point point : points) {
            drawPoint(point, color);
        }
    }

    private void drawPoint(Point point, Color color) {
        getGraphicsContext().setFill(color);
        getGraphicsContext().fillOval(point.getX() - POINT_SIZE, point.getY() - POINT_SIZE, 2 * POINT_SIZE, 2 * POINT_SIZE);
    }

    private void clearCanvas() {
        getGraphicsContext().setFill(Color.WHITE);
        getGraphicsContext().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private double[] getCoeffs() {
        String[] parts = input_textField.getText().split(" ");

        double[] coeffs = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            try {
                coeffs[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException e) {
                coeffs[i] = Double.NaN;
            }
        }

        return coeffs;
    }

    private void initDrawButton(Button drawButton, DrawStrategy drawStrategy1) {
        drawButton.setOnAction(event -> {
            double[] coeffs = getCoeffs();

            try {
                this.drawnPoints = new ArrayList<>();
                for (DrawStrategy drawStrategy : new DrawStrategy[]{drawStrategy1}) {
                    drawnPoints.addAll(drawStrategy.generate(clickedPoints, coeffs));
                }

                clearCanvas();
                drawPoints(drawnPoints, Color.BLACK);
                drawPoints(clickedPoints, Color.RED);
            } catch (DrawStrategyException e) {
                showExceptionAlert(e);
            }
        });
    }

    private void initFillButton(RadioButton fillButton, ToggleGroup group, DrawStrategy fillStrategy) {
        fillButton.setToggleGroup(group);
        fillButton.setOnAction(event -> {
            selectedFillStrategy = fillStrategy;
        });
    }

    private void showExceptionAlert(DrawStrategyException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка отрисовки");
        alert.setHeaderText("Невозможно выполнить действие:");
        alert.setContentText("Причина: " + e.getMessage());

        alert.show();
    }

    private void initButtons() {
        clear_button.setOnAction(event -> {
            clearCanvas();
            clickedPoints.clear();
            drawnPoints.clear();
        });

        initDrawButton(polygon_button, DrawPolylineStrategy.POLYGON_INSTANCE);
        initDrawButton(circle_button, DrawCircleStrategy.INSTANCE);
        initDrawButton(bezier_button, DrawBezierCurveStrategy.INSTANCE);

        ToggleGroup group = new ToggleGroup();
        initFillButton(point_click_button, group, null);
        initFillButton(dfs_coloring_button, group, new FillBfsStrategy(canvas.getWidth(), canvas.getHeight()));
        initFillButton(segment_coloring_button, group, new FillSegmentsStrategy(canvas.getWidth(), canvas.getHeight()));

        point_click_button.fire();
        selectedFillStrategy = null;
    }
}

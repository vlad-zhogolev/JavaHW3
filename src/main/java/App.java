import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.time.LocalTime;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App extends Application {

    private int width = 1000;
    private int height = 500;
    private int clockRadius = 150;
    private int clockSide = height;
    private Point2D clockCenter = new Point2D(clockSide / 2.0, clockSide / 2.0);
    private int hourStrokeWidth = 6;
    private int hourStrokeHeight = 12;
    private int minuteStrokeWidth = hourStrokeWidth / 2 + hourStrokeWidth % 2;
    private int minuteStrokeHeight = hourStrokeHeight / 2 + hourStrokeHeight % 2;
    private Color clockColor = Color.LAWNGREEN;
    private Image image = new Image(getClass().getResource("summer.jpg").toString());

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        root.setMinWidth(width);
        root.setMinHeight(height);

        Pane clockRoot = new Pane();
        clockRoot.setPrefSize(clockSide, clockSide);
        root.getChildren().add(clockRoot);

        VBox controls = new VBox();
        controls.setPrefSize(width - clockSide, height);
        controls.setSpacing(5.0);
        root.getChildren().add(controls);

        Circle circle = new Circle(
                clockCenter.getX(),
                clockCenter.getY(),
                clockRadius);
        circle.setFill(new ImagePattern(image));
        clockRoot.getChildren().add(circle);

        LocalTime time = LocalTime.now();
        Arc arc = new Arc(
                clockCenter.getX(),
                clockCenter.getY(),
                clockRadius,
                clockRadius,
                90,
                360);
        arc.setOpacity(1.0);
        arc.setType(ArcType.ROUND);
        arc.setFill(clockColor);
        clockRoot.getChildren().add(arc);

        //Draw hour strokes
        Translate moveHourStrokeToClockCenter = new Translate(
                clockCenter.getX() - hourStrokeWidth / 2,
                clockCenter.getY() - hourStrokeHeight / 2);
        Translate moveHourStrokeByRadius = new Translate(0, clockRadius - hourStrokeHeight / 2);

        for (int i = 0; i < 12; i++) {
            Rectangle rect = new Rectangle(hourStrokeWidth, hourStrokeHeight);

            rect.getTransforms().add(moveHourStrokeToClockCenter);
            rect.getTransforms().add(moveHourStrokeByRadius);
            rect.getTransforms().add(new Rotate(
                    30 * i,
                    hourStrokeWidth / 2,
                    hourStrokeHeight - clockRadius));

            clockRoot.getChildren().add(rect);
        }

        //Draw minute stroke
        Translate moveMinuteStrokeToClockCenter = new Translate(
                clockCenter.getX() - minuteStrokeWidth / 2,
                clockCenter.getY() - minuteStrokeHeight / 2);
        Translate moveMinuteStrokeByRadius = new Translate(0, clockRadius - hourStrokeHeight / 2);

        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0)
                continue;

            Rectangle rect = new Rectangle(minuteStrokeWidth, minuteStrokeHeight);

            rect.getTransforms().add(moveMinuteStrokeToClockCenter);
            rect.getTransforms().add(moveMinuteStrokeByRadius);
            rect.getTransforms().add(new Rotate(
                    6 * i,
                    minuteStrokeWidth / 2,
                    hourStrokeHeight / 2 + minuteStrokeHeight / 2 - clockRadius));
            clockRoot.getChildren().add(rect);
        }

        //Draw numbers
        for (int i = 0; i < 12; i++) {
            Label text = new Label((i == 0 ? 12 : i) + "");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            text.setTextFill(Color.BLACK);
            Rectangle rect = new Rectangle(20, 20);
            rect.setOpacity(0.0);
            StackPane sp = new StackPane();
            sp.getChildren().addAll(rect, text);

            sp.getTransforms().add(new Translate(
                    clockCenter.getX() - rect.getWidth() / 2,
                    clockCenter.getY() - rect.getHeight() / 2 - 0.8 * clockRadius));
            sp.getTransforms().add(new Rotate(30 * i, 0, 0.8 * clockRadius));
            sp.getTransforms().add(new Rotate(-30 * i, 0, 0));

            clockRoot.getChildren().add(sp);
        }

        //Position, animate and draw watch hands
        Shape hourHand = Shape.union(
                new Polygon(-3, -3, -8, 0.2 * clockRadius, 0, 0.5 * clockRadius, 8, 0.2 * clockRadius, 3, -3),
                new Circle(0, 0, 8));
        Shape minuteHand = Shape.union(
                new Polygon(-3, -3, -5, 0.3 * clockRadius, 0, 0.75 * clockRadius, 5, 0.3 * clockRadius, 3, -3),
                new Circle(0, 0, 6));
        Shape secondHand = Shape.union(
                new Polygon(-3, -40, -3, -10, -1, -10, -1, 0.85 * clockRadius, 1, 0.85 * clockRadius, 1, -10, 3, -10, 3, -40),
                new Circle(0, 0, 5));
        secondHand.setFill(Color.DARKRED);

        clockRoot.getChildren().add(hourHand);
        clockRoot.getChildren().add(minuteHand);
        clockRoot.getChildren().add(secondHand);

        hourHand.getTransforms().add(new Translate(clockCenter.getX(), clockCenter.getY()));
        hourHand.getTransforms().add(new Rotate(180, 0, 0));
        minuteHand.getTransforms().add(new Translate(clockCenter.getX(), clockCenter.getY()));
        minuteHand.getTransforms().add(new Rotate(180, 0, 0));
        secondHand.getTransforms().add(new Translate(clockCenter.getX(), clockCenter.getY()));
        secondHand.getTransforms().add(new Rotate(180, 0, 0));

        Rotate secHandRotation = new Rotate();
        secondHand.getTransforms().add(secHandRotation);

        Rotate minHandRotation = new Rotate();
        minuteHand.getTransforms().add(minHandRotation);

        Rotate hourHandRotation = new Rotate();
        hourHand.getTransforms().add(hourHandRotation);

        int secSeed = time.getSecond();
        int minSeed = time.getMinute();
        int hourSeed = time.getHour();

        Timeline secondHandBegin = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(secHandRotation.angleProperty(), 6 * secSeed)),
                new KeyFrame(Duration.seconds(60 - secSeed), new KeyValue(secHandRotation.angleProperty(), 360)));

        Timeline secondHandAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(secHandRotation.angleProperty(), 0), new KeyValue(arc.lengthProperty(), 360)),
                new KeyFrame(Duration.seconds(60), new KeyValue(secHandRotation.angleProperty(), 360), new KeyValue(arc.lengthProperty(), 0)),
                new KeyFrame(Duration.seconds(120), new KeyValue(secHandRotation.angleProperty(), 720), new KeyValue(arc.lengthProperty(), -360)));

        Timeline minuteHandAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(minHandRotation.angleProperty(), 6 * minSeed + secSeed / 60.0)),
                new KeyFrame(Duration.minutes(60), new KeyValue(minHandRotation.angleProperty(), 360 + 6 * minSeed)));

        Timeline hourHandAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(hourHandRotation.angleProperty(), 30 * (hourSeed % 12) + minSeed / 10.0 + secSeed / 600.0)),
                new KeyFrame(Duration.hours(24), new KeyValue(hourHandRotation.angleProperty(), 360 + 30 * (hourSeed % 12) + minSeed / 10.0 + secSeed / 600.0)));


        secondHandAnimation.setCycleCount(Animation.INDEFINITE);
        minuteHandAnimation.setCycleCount(Animation.INDEFINITE);
        hourHandAnimation.setCycleCount(Animation.INDEFINITE);
        secondHandBegin.setOnFinished(event -> secondHandAnimation.play());

        hourHandAnimation.play();
        minuteHandAnimation.play();
        secondHandBegin.play();

        Button stopClocksButton = new Button("Stop");
        stopClocksButton.setPrefWidth(80);

        Button continueClockButton = new Button("Continue");
        continueClockButton.setDisable(true);

        TilePane buttonTiles = new TilePane(Orientation.HORIZONTAL, 10, 0, stopClocksButton, continueClockButton);
        controls.getChildren().add(buttonTiles);

        Slider watchAnimationRateSlider = new Slider(0.0, 10, 1);
        watchAnimationRateSlider.setPrefSize(controls.getWidth(), 50);
        watchAnimationRateSlider.setShowTickLabels(true);
        watchAnimationRateSlider.setShowTickMarks(true);
        watchAnimationRateSlider.setSnapToTicks(true);
        watchAnimationRateSlider.setMajorTickUnit(1.0);
        watchAnimationRateSlider.setMinorTickCount(10);

        controls.getChildren().add(watchAnimationRateSlider);

        stopClocksButton.setOnAction(event -> {
            hourHandAnimation.pause();
            minuteHandAnimation.pause();
            if (secondHandAnimation.getStatus() == Animation.Status.RUNNING)
                secondHandAnimation.pause();
            if (secondHandBegin.getStatus() == Animation.Status.RUNNING)
                secondHandBegin.pause();
            watchAnimationRateSlider.setDisable(true);
            continueClockButton.setDisable(false);
        });

        continueClockButton.setOnAction(event -> {
            if (secondHandBegin.getStatus() == Animation.Status.PAUSED)
                secondHandBegin.play();
            if (secondHandAnimation.getStatus() == Animation.Status.PAUSED)
                secondHandAnimation.play();
            minuteHandAnimation.play();
            hourHandAnimation.play();
            continueClockButton.setDisable(true);
            watchAnimationRateSlider.setDisable(false);
        });

        watchAnimationRateSlider.valueProperty().addListener(observable -> {
            DoubleProperty value = (DoubleProperty) observable;
            if (value.get() == 0) {
                hourHandAnimation.pause();
                minuteHandAnimation.pause();
                if (secondHandBegin.getStatus() == Animation.Status.PAUSED)
                    secondHandBegin.pause();
                if (secondHandAnimation.getStatus() == Animation.Status.PAUSED)
                    secondHandAnimation.pause();
            } else {
                hourHandAnimation.play();
                minuteHandAnimation.play();
                if (secondHandBegin.getStatus() == Animation.Status.PAUSED)
                    secondHandBegin.play();
                if (secondHandAnimation.getStatus() == Animation.Status.PAUSED)
                    secondHandAnimation.play();

                hourHandAnimation.setRate(value.get());
                minuteHandAnimation.setRate(value.get());
                secondHandAnimation.setRate(value.get());
                secondHandBegin.setRate(value.get());
            }

        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

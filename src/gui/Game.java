package gui;

import dev.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Edoardo De Matteis
 */
public class Game extends Application {
    private final static Double gap = Font.getDefault().getSize();
    private Color prophet = null;
    private boolean stop = true;
    public static Map<List<Integer>, Player> select = new HashMap<>();
    public static Pane root = new Pane();
    private static WritableImage wi = new WritableImage(Util.width, Util.heigth);
    private static Button startButton = new Button("In principium");
    private static Button clearButton = new Button("Ite, missa est");
    private static Button pauseButton = new Button("Dies septimus");
    private static Button godButton = new Button("Dixitque Deus");
    private static Button saveButton = new Button("Salvator mundi");
    private static Label rulesLabel = new Label(Util.label);
    private static Label goLabel = new Label("Non expedit!");
    private static Label numberLabel = new Label(Integer.toString(select.size()));
    private static StringProperty numberString = new SimpleStringProperty();
    private static Button atheistButton = new Button("Athei");
    private static Button muslimButton = new Button("Mahometani");
    private static Button christButton = new Button("Christiani");
    private static Button jewButton = new Button("Iudaei");
    private static Button greenButton = new Button();
    private static Button purpleButton = new Button();
    private static Button blueButton = new Button();

    /**
     * Organizza il layout della gui
     * @param eastBox       schermata principale
     * @param northBox      comandi principali
     * @param southBox      pannello su cui disegnare
     * @param playersBox    giocatori da selezionare
     */
    private void setup(VBox eastBox, HBox northBox, HBox southBox, HBox playersBox) {
        root.setPrefSize(Util.width, Util.heigth);
        root.setClip(new Rectangle(0, 0, Util.width, Util.heigth));
        root.setStyle("-fx-background-color: rgb(255,224,0);");
        //root.setBackground(new Background(new BackgroundFill(Util.VATICAN, CornerRadii.EMPTY, Insets.EMPTY)));
        goLabel.setVisible(false);
        goLabel.setStyle("-fx-text-fill: red;");
        numberLabel.textProperty().bind(numberString);

        northBox.setAlignment(Pos.CENTER);
        southBox.setAlignment(Pos.CENTER);
        playersBox.setAlignment(Pos.CENTER);

        northBox.getChildren().addAll(goLabel, startButton, pauseButton, godButton, clearButton, saveButton);
        southBox.getChildren().addAll(rulesLabel);
        playersBox.getChildren().addAll(numberLabel, atheistButton, christButton, purpleButton, muslimButton, greenButton, jewButton, blueButton);
        eastBox.getChildren().addAll(northBox, southBox, playersBox, root);
    }

    /**
     * Disegna un nodo sul pannello
     * @param p     è il nodo da disegnare. Posizione, grandezza e colore sono definiti in precedenza
     */
    public static void draw (Node p) {
        root.getChildren().add(p);
    }

    /**
     * In base al colore scelto crea un oggetto di tipo specifico
     * @param x     posizione nelle ascisse
     * @param y     posizione nelle ordinate
     */
    private void control(int x, int y) {
        try {
            if (prophet.equals(Color.RED)) {
                Muslim r = new Muslim(x, y);
                root.getChildren().add(r.rec);
            } else if (prophet.equals(Color.PURPLE)) {
                Christian c = new Christian(x, y);
                root.getChildren().add(c.rec);
            } else if (prophet.equals(Color.ROYALBLUE)) {
                Jew j = new Jew(x, y);
                root.getChildren().add(j.rec);
            }
        } catch (IllegalArgumentException ignored) {}
    }

    /**
     * Restituisce il colore di un oggetto con
     * @param l posizione in coordinate
     * @return  colore del nodo
     */
    public static Color getColor(List<Integer> l){
        try {
            return select.get(l).flag;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Trasla un oggetto a una posizione definita da {@link Player#move()}
     */
    public static void update() {
        for(Player p: select.values()) {
            Translate trans = new Translate();
            List<Integer> pos = p.move();
            trans.setX(pos.get(0));
            trans.setY(pos.get(1));
            p.rec.getTransforms().addAll(trans);
        }
    }

    /**
     * Salva uno snapshot in un file .png
     * @param file  file in cui si salverà lo snapshot
     */
    private static void photo(File file) {
        if(file != null){
            try {
                root.snapshot(null, wi);
                ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", file);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @see AnimationTimer#start()
     * @param primaryStage  finestra principale su cui si inseriscono gli elementi grafici
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tu sei Pietro e su questa pietra edificherò la mia chiesa");
        VBox eastBox = new VBox(gap);
        HBox northBox = new HBox(gap);
        HBox southBox = new HBox(gap);
        HBox playersBox = new HBox(gap);
        setup(eastBox, northBox, southBox, playersBox);

        startButton.setDisable(true);
        godButton.setDisable(true);
        rulesLabel.setWrapText(true);
        rulesLabel.setTextAlignment(TextAlignment.JUSTIFY);
        greenButton.setStyle("-fx-background-color: darkgreen;");
        purpleButton.setStyle("-fx-background-color: purple;");
        blueButton.setStyle("-fx-background-color: royalblue;");
        eastBox.setStyle("-fx-background-color: white");

        northBox.setPadding(new Insets(gap));
        southBox.setPadding((new Insets(gap)));
        playersBox.setPadding((new Insets(gap)));
        primaryStage.setScene(new Scene(eastBox));
        primaryStage.show();

        startButton.setOnMouseClicked(event -> new AnimationTimer() {
            /**
             * @see AnimationTimer#handle(long)
             */
            @Override
            public void handle(long now) {
                if (!stop) {
                    this.stop();
                } else {
                    Service<Void> service = new Service<Void>() {
                        /**
                         * @see Service#createTask()
                         */
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                /**
                                 * @see Task#call()
                                 * @throws Exception    eccezione
                                 */
                                @Override
                                protected Void call() throws Exception {
                                    final CountDownLatch latch = new CountDownLatch(1);
                                    Platform.runLater(() -> {
                                        try {

                                            Player.walk();
                                        } finally {
                                            latch.countDown();
                                        }
                                    });
                                    latch.await();
                                    return null;
                                }
                            };
                        }
                    };
                    service.start();
                }
            }
        }.start());

        clearButton.setOnMouseClicked(event -> {
            select.clear();
            root.getChildren().clear();
            numberString.setValue(Integer.toString(select.size()));

        });

        pauseButton.setOnMouseClicked(event -> {
            goLabel.setVisible(stop);
            stop = !stop;
        });

        godButton.setOnMouseClicked(event -> {
            Integer x = ThreadLocalRandom.current().nextInt(0, Util.width);
            Integer y = ThreadLocalRandom.current().nextInt(0, Util.heigth);
            control(x, y);
        });

        root.setOnMouseClicked(event -> {
            try {
                if(prophet == null) {
                    Integer x = (int) event.getX();
                    Integer y = (int) event.getY();
                    x = x-(x % Util.l);
                    x = x-(x % Util.l);
                    Player a = select.get(Arrays.asList(x,y));
                    System.out.println(a);
                }
                Integer x = (int) event.getX();
                Integer y = (int) event.getY();
                control(x, y);
            } catch (NullPointerException ignored) {}
            numberString.setValue(Integer.toString(select.size()));
        });

        atheistButton.setOnMouseClicked(event -> {
            prophet = null;
            startButton.setDisable(false);
            Player.walk();
        });

        muslimButton.setOnMouseClicked(event -> {
            prophet = Color.RED;
            startButton.setDisable(false);
            godButton.setDisable(false);
            Muslim.folk();
            numberString.setValue(Integer.toString(select.size()));
        });

        greenButton.setOnMouseClicked(event -> {
            prophet = Color.RED;
            startButton.setDisable(false);
            godButton.setDisable(false);
        });

        christButton.setOnMouseClicked(event -> {
            prophet = Color.PURPLE;
            startButton.setDisable(false);
            godButton.setDisable(false);
            Christian.folk();
            numberString.setValue(Integer.toString(select.size()));
        });

        purpleButton.setOnMouseClicked(event -> {
            prophet = Color.PURPLE;
            startButton.setDisable(false);
            godButton.setDisable(false);
        });

        jewButton.setOnMouseClicked(event -> {
            prophet = Color.ROYALBLUE;
            startButton.setDisable(false);
            godButton.setDisable(false);
            Jew.folk();
            numberString.setValue(Integer.toString(select.size()));
        });

        blueButton.setOnMouseClicked(event -> {
            prophet = Color.ROYALBLUE;
            startButton.setDisable(false);
            godButton.setDisable(false);
        });

        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            photo(file);
        });

    }

    /**
     * Avvia l'esecusione
     * @param args argomenti inseriti (possono esserci più schede
     */
    public static void main(String[] args) {
        launch(args);
    }
}
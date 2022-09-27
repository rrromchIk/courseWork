package com.main;

import com.figures.*;
import com.util.Painter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/*
 ⁃ default constructor          | done
 ⁃ static field counter         | done
 ⁃ label scene status           | done
 ⁃ default color of each figure | done
 ⁃ adding on scene algorithm    | in process
 ⁃ tooltips on buttons          | done
 ⁃ new tests                    | done
 */

public class MyController implements Initializable {
    private static final String THERE_ARE_NO_FIGURES = "There are no figures added!";
    private static final String TRY_TO_ADD_ANY_FIGURE = "Try to add any figure :)";
    private final List<Figure> figures = new ArrayList<>();

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField sizeTextField;
    @FXML
    private TextField minPerimeterTextField;
    @FXML
    private TextField maxAreaTextField;
    @FXML
    private TextField givenArea;
    @FXML
    private ComboBox<String> figuresComboBox;
    @FXML
    private ComboBox<String> namesComboBox;
    @FXML
    private Pane pane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Label sceneStatus;
    @FXML
    private Label writeToFileStatus;

    public void onAddFigureButtonClicked() {
        String name = nameTextField.getText();
        String size = sizeTextField.getText();
        try {
            if (figuresComboBox.getValue() == null)
                throw new IllegalArgumentException("Choose figure!!!");
            if(size.isEmpty() || name.isEmpty()) {
                throw new IllegalArgumentException("Fill all text fields!!!");
            }

            switch (figuresComboBox.getValue()) {
                case "Circle" ->  figures.add(new MyCircle(name, Double.parseDouble(size)));
                case "Square" -> figures.add(new MySquare(name, Double.parseDouble(size)));
                case "Triangle" -> {
                    String[] sides = size.split(" ");
                    figures.add(new MyTriangle(name, Double.parseDouble(sides[0]), Double.parseDouble(sides[1]),
                            Double.parseDouble(sides[2])));
                }
            }
            displayFigures(figures);
            namesComboBox.getItems().add(name);
            updateSceneStatus("↑ Scene: All added figures displayed! :)");
            updateWriteToFileStatus("Write info about figures to the file!");
        } catch (IllegalArgumentException e) {
            showAlert("It looks like something went wrong!", e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            showAlert("Bad input", "Write tree side sizes for triangle!");
        }

        clearTextFields();
    }

    public void onWriteToFileButtonCLicked() throws IOException {
        if(!figures.isEmpty()) {
            Figure.writeToFile(figures, "src\\main\\resources\\info.txt");
            updateWriteToFileStatus("Info wrote to the file: info.txt!");
        } else {
            showAlert(THERE_ARE_NO_FIGURES, TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onSortFiguresButtonCLicked() {
        if(!figures.isEmpty()) {
            displayFigures(Figure.sortFiguresByInscribedCircleRadius(figures));
            updateSceneStatus("↑ Scene: Figures sorted by inscribed circle radius! :)");
        } else {
            showAlert(THERE_ARE_NO_FIGURES, TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onMaxAreaFigureButtonClicked() {
        if(!figures.isEmpty()) {
            Figure figure = Figure.getMaxAreaFigure(figures);
            maxAreaTextField.setText(String.format("%s: %.2f", figure.getName(), figure.getArea()));
        } else {
            showAlert(THERE_ARE_NO_FIGURES, TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onMinPerimeterFigureButtonClicked() {
        if(!figures.isEmpty()) {
            Figure figure = Figure.getMinPerimeterFigure(figures);
            minPerimeterTextField.setText(String.format("%s: %.2f", figure.getName(), figure.getPerimeter()));
        } else {
            showAlert(THERE_ARE_NO_FIGURES, TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onFillButtonClicked() {
        if(namesComboBox.getValue() != null) {
            figures.stream()
                            .filter(figure -> figure.getName().equals(namesComboBox.getValue()))
                                    .forEach(figure -> figure.setColor(colorPicker.getValue()));
            displayFigures(figures);
        } else {
            showAlert("Choose figure name!", "If there are no names, " + TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onDisplayAllButtonClicked() {
        if(!figures.isEmpty()) {
            displayFigures(figures);
            updateSceneStatus("↑ Scene: All added figures displayed! :)");
        } else {
            showAlert(THERE_ARE_NO_FIGURES, TRY_TO_ADD_ANY_FIGURE);
        }
    }

    public void onClearSceneButtonClicked() {
        figures.clear();
        Painter.clearScene(pane);
        clearTextFields();
        namesComboBox.getItems().clear();
        namesComboBox.setPromptText("Name of figure");
        updateSceneStatus("↑ Scene: Cleaned)");
        updateWriteToFileStatus("");
    }

    public void onGreaterThanButtonClicked() {
        String text = givenArea.getText();
        if(!text.isEmpty()) {
            List<Figure> list = Figure.getFiguresWithAreaGreaterThenGiven(figures, Double.parseDouble(text));
            if(list.isEmpty())
                showAlert("There are no figures with are greater than " + text, "Try again!");
            else {
                displayFigures(list);
                updateSceneStatus("↑ Scene: Figures with are greater than: " +  text + "! :)");
            }
            givenArea.setText("");
        } else {
            showAlert("It looks like something went wrong!", "Enter any area value!");
        }
    }

    public void onReadFromFileButtonClicked() {
        try {
            figures.addAll(Figure.readFiguresFromFile("src\\main\\resources\\inputData.txt"));
            displayFigures(figures);
            figures.forEach(figure -> namesComboBox.getItems().add(figure.getName()));
            updateSceneStatus("↑ Scene: All added figures displayed! :)");
            updateWriteToFileStatus("Write info about figures to the file!");
        } catch (RuntimeException e) {
            showAlert("Bad input format!", "Change input data format and try again!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        figuresComboBox.getItems().addAll("Circle", "Square", "Triangle");
    }

    private void clearTextFields() {
        maxAreaTextField.setText("");
        minPerimeterTextField.setText("");
        nameTextField.setText("");
        sizeTextField.setText("");
    }

    private void showAlert(String headerText, String bodyText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ups..");
        alert.setHeaderText(headerText);
        alert.setContentText(bodyText);
        alert.showAndWait();
    }

    private void displayFigures(List<Figure> figures) {
        try {
            Painter painter = new Painter(pane, figures);
            painter.draw();
        } catch (Exception e) {
            showAlert("Ups...", "No more space!\n" +
                    "Try to increase your window or delete current figures!");
        }
    }

    private void updateSceneStatus(String text) {
        sceneStatus.setText(text);
    }

    private void updateWriteToFileStatus(String text) {
        writeToFileStatus.setText(text);
    }
}
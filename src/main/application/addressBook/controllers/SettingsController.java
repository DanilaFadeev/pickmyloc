package addressBook.controllers;

import addressBook.helpers.SwitchScene;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;

public class SettingsController {
    @FXML
    private void initialize() {
        ObservableList<String> options =
            FXCollections.observableArrayList(
                "Russian",
                "English",
                "French"
            );

        cbLanguage.itemsProperty().set(options);
        cbLanguage.setValue("Russian");

        Tooltip ttLang = new Tooltip("Help!!!");
        ttLanguage.setTooltip(ttLang);
    }

    @FXML
    private JFXComboBox<String> cbLanguage;

    @FXML
    private JFXButton ttLanguage;

    @FXML
    private JFXTextField locationField;

    @FXML
    private JFXSlider sliderZoom;

    @FXML
    private FontAwesomeIconView ttTakeFromMap;

    @FXML
    void onTakeFromMap(ActionEvent event) {

    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml");
        switchScene.loadScene(event);
    }
}

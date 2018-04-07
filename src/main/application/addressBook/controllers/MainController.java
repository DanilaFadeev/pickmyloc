package addressBook.controllers;

import addressBook.Main;
import addressBook.helpers.GoogleMapManager;
import addressBook.helpers.SwitchScene;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;


public class MainController {

    private ContactsPanel contactsPanel;

    @FXML
    public void initialize() {
        SwitchScene<ContactsPanel> switchScene = new SwitchScene<>("../views/ContactsPanel.fxml");
        contactsPanel = switchScene.loadToPane(rootPane);

        contactsPanel.setMainController(this);

        // hide buttons
        manageAdditionalButtons(false);

        // Map initialization
        googleMapView.addMapInializedListener(() -> GoogleMapManager.configureMap(googleMapView, mapLoadingSpinner));
    }

    @FXML
    protected AnchorPane rootPane;

    @FXML
    protected GoogleMapView googleMapView;

    @FXML
    protected JFXSpinner mapLoadingSpinner;

    @FXML
    protected JFXButton contactInfoBtn;

    @FXML
    protected JFXButton allContactsBtn;

    @FXML
    protected JFXButton editContactBtn;

    @FXML
    protected JFXButton removeContactBtn;

    @FXML
    protected void onAddContact() {
        SwitchScene<AddPanelController> switchScene = new SwitchScene<>("../views/AddPanel.fxml");
        switchScene.loadToPane(rootPane);
    }

    @FXML
    protected void onEditContact() {
        SwitchScene<AddPanelController> switchScene = new SwitchScene<>("../views/AddPanel.fxml");

        AddPanelController addPanelController = switchScene.loadToPane(rootPane);
        addPanelController.setEditingContact(contactsPanel.getSelectedContact());
    }

    @FXML void onShowContactInfo() {
        SwitchScene<ContactDetailsController> switchScene = new SwitchScene<>("../views/DetailsPanel.fxml");

        ContactDetailsController detailsPanel = switchScene.loadToPane(rootPane);
        detailsPanel.setContactInfo(contactsPanel.getSelectedContact());
    }

    @FXML
    protected void onSignOut(ActionEvent event) {
        SwitchScene<LoginFormController> switchScene = new SwitchScene<>("../views/Login.fxml", true, false);
        switchScene.loadScene(event);
    }

    @FXML
    protected void onDeselectContacts() {
        contactsPanel.contactsTable.getSelectionModel().clearSelection();
        manageAdditionalButtons(false);

        GoogleMapManager.setAllMarkers(Main.data);
    }

    @FXML
    protected void onRemoveContact() {
        Alert removeConfirmAlert = new Alert( Alert.AlertType.CONFIRMATION );

        String contactName = contactsPanel.getSelectedContact().name.getValue();
        removeConfirmAlert.setContentText("Do you really want to delete `" + contactName + "` from \nyour contacts?");

        removeConfirmAlert.setGraphic(null);
        removeConfirmAlert.setHeaderText(null);

        Optional<ButtonType> result = removeConfirmAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Main.data.remove(contactsPanel.getSelectedContact());
            onDeselectContacts();
        }
    }

    public void manageAdditionalButtons(boolean isShow) {
        contactInfoBtn.setVisible(isShow);
        allContactsBtn.setVisible(isShow);
        editContactBtn.setVisible(isShow);
        removeContactBtn.setVisible(isShow);
    }
}


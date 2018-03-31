package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import addressBook.Main;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;


public class MainController {

    private ContactsPanel contactsPanel;

    @FXML
    public void initialize() {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("../views/ContactsPanel.fxml"));

        try {
            Node pane = Loader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        contactsPanel = Loader.getController();
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
        try {
            Node pane = FXMLLoader.load(getClass().getResource("../views/AddPanel.fxml"));
            rootPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void onShowContactInfo() {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("../views/DetailInformation.fxml"));

        try {
            Node pane = Loader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        ContactDetails detailsPanel = Loader.getController();
        detailsPanel.setContactInfo(contactsPanel.getSelectedContact());
    }

    @FXML
    protected void onDeselectContacts() {
        contactsPanel.contactsTable.getSelectionModel().clearSelection();
        manageAdditionalButtons(false);

        GoogleMapManager.setAllMarkers(Main.data);
    }

    @FXML
    protected void onRemoveContact() {
        Alert removeConfirmAlert = new Alert(Alert.AlertType.CONFIRMATION);

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


    public void addContact(String name) {
        Main.data.add(new Contact(name, name, name, name));
    }

    public void manageAdditionalButtons(boolean isShow) {
        contactInfoBtn.setVisible(isShow);
        allContactsBtn.setVisible(isShow);
        editContactBtn.setVisible(isShow);
        removeContactBtn.setVisible(isShow);
    }


}


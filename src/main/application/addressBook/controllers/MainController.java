package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import addressBook.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;


public class MainController {

    private ContactsPanel contactsPanel;

    @FXML
    public void initialize() {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("../views/ContactsPanel.fxml"));

        try {
            AnchorPane pane = Loader.load();
            rootPane.getChildren().setAll(pane.getChildren());
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

    @FXML
    protected void onEditContact() throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("../views/AddPanel.fxml"));

        Node pane = Loader.load();
        rootPane.getChildren().setAll(pane);

        AddPanelController addPanelController = Loader.getController();
        addPanelController.setEditingContact(contactsPanel.getSelectedContact());
    }

    @FXML void onShowContactInfo() throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("../views/DetailInformation.fxml"));

        AnchorPane pane = Loader.load();
        rootPane.getChildren().setAll(pane.getChildren());

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

    @FXML
    protected void onSignOut(ActionEvent event) throws IOException {
        URL url = new File("src/main/application/addressBook/views/Login.fxml").toURL();
        FXMLLoader Loader = new FXMLLoader(url);

        Loader.load();
        Parent mainForm = Loader.getRoot();

        Scene scene = new Scene(mainForm);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }

    public void manageAdditionalButtons(boolean isShow) {
        contactInfoBtn.setVisible(isShow);
        allContactsBtn.setVisible(isShow);
        editContactBtn.setVisible(isShow);
        removeContactBtn.setVisible(isShow);
    }


}


package addressBook.controllers;

import addressBook.Main;
import addressBook.helpers.DBConnection;
import addressBook.helpers.GoogleMapManager;
import addressBook.helpers.HibernateUtil;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import addressBook.models.Settings;
import addressBook.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;


public class MainController {

    public static ObservableList<Contact> contacts;
    public static Settings appSettings;
    private ContactsController contactsPanel;

    public static User currentUser;

    @FXML
    public void initialize() {
        // load user settings
        appSettings = currentUser.getSettings();

        // load user contacts list
        contacts = FXCollections.observableArrayList(
                HibernateUtil.getInstance().getUserContacts( currentUser.getId() )
        );

        // hide buttons
        manageAdditionalButtons(false);

        SwitchScene<ContactsController> switchScene = new SwitchScene<>("panels/Contacts.fxml");
        contactsPanel = switchScene.loadToPane(rootPane);
        contactsPanel.setMainController(this);

        // Map initialization
        googleMapView.addMapInializedListener(()
                -> GoogleMapManager.configureMap(googleMapView, mapLoadingSpinner, contactsPanel));
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
        SwitchScene<ContactFormController> switchScene = new SwitchScene<>("panels/ContactForm.fxml");

        ContactFormController addPanelController = switchScene.loadToPane(rootPane);
        addPanelController.setGoogleMapView(googleMapView);
    }

    @FXML
    protected void onEditContact() {
        SwitchScene<ContactFormController> switchScene = new SwitchScene<>("panels/ContactForm.fxml");

        ContactFormController addPanelController = switchScene.loadToPane(rootPane);
        addPanelController.setEditingContact(contactsPanel.getSelectedContact());
        addPanelController.setGoogleMapView(googleMapView);
    }

    @FXML void onShowContactInfo() {
        SwitchScene<DetailsController> switchScene = new SwitchScene<>("panels/Details.fxml");

        DetailsController detailsPanel = switchScene.loadToPane(rootPane);
        detailsPanel.setContactInfo(contactsPanel.getSelectedContact());
    }

    @FXML
    protected void onSignOut(ActionEvent event) {
        currentUser = null;

        SwitchScene<LoginController> switchScene = new SwitchScene<>("forms/Login.fxml", true, false);
        switchScene.loadScene(event);
    }

    @FXML
    protected void onDeselectContacts() {
        contactsPanel.contactsTable.getSelectionModel().clearSelection();
        manageAdditionalButtons(false);

        GoogleMapManager.displayAllMarkers(true);
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
            HibernateUtil.getInstance().deleteContact( contactsPanel.getSelectedContact() );

            MainController.contacts.remove( contactsPanel.getSelectedContact() );
            GoogleMapManager.removeMarker( contactsPanel.getSelectedContact() );

            onDeselectContacts();
        }
    }

    @FXML
    protected void onSettings() {
        SwitchScene<SettingsController> switchScene = new SwitchScene<>("panels/Settings.fxml");
        switchScene.loadToPane(rootPane);
    }

    @FXML
    protected void onCloseApp(ActionEvent event) {
        HibernateUtil.getInstance().closeConnection();
        SwitchScene.closeStage(event);
    }

    public void manageAdditionalButtons(boolean isShow) {
        contactInfoBtn.setVisible(isShow);
        allContactsBtn.setVisible(isShow);
        editContactBtn.setVisible(isShow);
        removeContactBtn.setVisible(isShow);
    }
}


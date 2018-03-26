package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import addressBook.Main;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

import java.util.function.Predicate;


public class Controller {

    @FXML
    public void initialize() {

        ObservableList<Contact> filteredContacts = Main.data;

        JFXTreeTableColumn<Contact, String> nameColumn = new JFXTreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<Contact, String> surnameColumn = new JFXTreeTableColumn<>("Surname");
        surnameColumn.setCellValueFactory(param -> param.getValue().getValue().surname);

        JFXTreeTableColumn<Contact, String> phoneColumn = new JFXTreeTableColumn<>("Phone");
        phoneColumn.setCellValueFactory(param -> param.getValue().getValue().phone);

        JFXTreeTableColumn<Contact, String> addressColumn = new JFXTreeTableColumn<>("Address");
        addressColumn.setCellValueFactory(param -> param.getValue().getValue().address);

        // Btns
        manageAdditionalButtons(false);

        // Style preferences
        JFXTreeTableColumn[] columns = { nameColumn, surnameColumn, phoneColumn, addressColumn };
        for (JFXTreeTableColumn column: columns ) {
            column.setStyle("-fx-font-size: 11px");
        }

        TreeItem<Contact> root = new RecursiveTreeItem<>(filteredContacts, RecursiveTreeObject::getChildren);
        contactsTable.getColumns().setAll(nameColumn, surnameColumn, phoneColumn, addressColumn);
        contactsTable.setRoot(root);
        contactsTable.setShowRoot(false);


        // onSelect row
        contactsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                RecursiveTreeItem selectedContact = (RecursiveTreeItem) contactsTable.getSelectionModel().getSelectedItem();
                Contact contact = (Contact) selectedContact.getValue();

                manageAdditionalButtons(true);

                GoogleMapManager.setMarker(googleMapView, contact.location);
            }
        });


        // Search initialization
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                contactsTable.setPredicate((Predicate<TreeItem<Contact>>) treeItem ->
                        treeItem.getValue().name.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().surname.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().phone.get().contains(newValue)
                ));

        // Map initialization
        googleMapView.addMapInializedListener(() -> GoogleMapManager.configureMap(googleMapView, mapLoadingSpinner));
    }


    void addContact(String name) {
        Main.data.add(new Contact(name, name, name, name));
    }

    private void manageAdditionalButtons(boolean isShow) {
        contactInfoBtn.setVisible(isShow);
        allContactsBtn.setVisible(isShow);
        editContactBtn.setVisible(isShow);
        removeContactBtn.setVisible(isShow);
    }

    @FXML
    private GoogleMapView googleMapView;

    @FXML
    private JFXSpinner mapLoadingSpinner;

    @FXML
    private JFXTreeTableView contactsTable;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXButton contactInfoBtn;

    @FXML
    private JFXButton allContactsBtn;

    @FXML
    private JFXButton editContactBtn;

    @FXML
    private JFXButton removeContactBtn;

    @FXML
    private void onAddContact(ActionEvent event) {
        SwitchScene<newContactController> switchScene = new SwitchScene<>("../views/newContactForm.fxml");
        switchScene.switchScene(event);
    }

    @FXML
    private void onDeselectContacts(ActionEvent event) {
        contactsTable.getSelectionModel().clearSelection();
        manageAdditionalButtons(false);

        GoogleMapManager.setAllMarkers(Main.data);
    }

}

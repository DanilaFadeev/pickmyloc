package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import addressBook.Main;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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
                RecursiveTreeItem selectedContact = (RecursiveTreeItem)contactsTable.getSelectionModel().getSelectedItem();
                Contact contact = (Contact)selectedContact.getValue();

                GoogleMapManager.setMarker(contact.location);
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
        googleMapView.addMapInializedListener(() -> GoogleMapManager.configureMap(googleMapView));
    }


    void addContact(String name) {
        Main.data.add(new Contact(name, name, name, name));
    }

    @FXML
    private GoogleMapView googleMapView;

    @FXML
    private JFXTreeTableView contactsTable;

    @FXML
    private JFXTextField searchField;

    @FXML
    private void onAddContact(ActionEvent event) {
        SwitchScene<FormController> switchScene = new SwitchScene<>("../views/contactForm.fxml");
        switchScene.switchScene(event);
    }

}

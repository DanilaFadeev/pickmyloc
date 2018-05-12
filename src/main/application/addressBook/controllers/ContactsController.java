package addressBook.controllers;

import addressBook.helpers.GoogleMapManager;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

import java.util.function.Predicate;

public class ContactsController {
    @FXML
    protected JFXTreeTableView contactsTable;

    @FXML
    protected JFXTextField searchField;

    @FXML
    public void initialize() {
        ObservableList<Contact> filteredContacts = MainController.contacts;

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
            column.setStyle("-fx-font-size: 11px; -fx-alignment: center");
        }

        TreeItem<Contact> root = new RecursiveTreeItem<>(filteredContacts, RecursiveTreeObject::getChildren);
        contactsTable.getColumns().setAll(nameColumn, surnameColumn, phoneColumn, addressColumn);
        contactsTable.setRoot(root);
        contactsTable.setShowRoot(false);


        // onSelect row
        contactsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mainController.manageAdditionalButtons(true);

                if (getSelectedContact().location != null) {
                    LatLong coords = new LatLong(
                            getSelectedContact().location.getLatitude(),
                            getSelectedContact().location.getLongitude()
                    );

                    GoogleMapManager.setMarker(mainController.googleMapView, coords);
                } else {
                    GoogleMapManager.setAllMarkers(filteredContacts, this);
                    GoogleMapManager.setDefaultMapOptions();
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                contactsTable.setPredicate((Predicate<TreeItem<Contact>>) treeItem ->
                        treeItem.getValue().name.get().toLowerCase().contains(newValue.toLowerCase())
                                || treeItem.getValue().surname.get().toLowerCase().contains(newValue.toLowerCase())
                                || treeItem.getValue().phone.get().contains(newValue)
                ));
    }

    public void onSelectContact() {
        mainController.manageAdditionalButtons(true);
    }

    private MainController mainController = null;

    public void setMainController(MainController mc) {
        mainController = mc;
    }

    public Contact getSelectedContact() {
        RecursiveTreeItem selectedContact = (RecursiveTreeItem) contactsTable.getSelectionModel().getSelectedItem();
        return (Contact) selectedContact.getValue();
    }

}

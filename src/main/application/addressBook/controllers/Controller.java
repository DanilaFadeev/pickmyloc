package addressBook.controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;

import addressBook.Main;

import java.util.function.Predicate;


public class Controller {

    @FXML
    public void initialize() {

        ObservableList<Contact> filteredContacts = Main.data;

        JFXTreeTableColumn<Contact, String> nameColumn = new JFXTreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Contact, String> param) {
                return param.getValue().getValue().name;
            }
        });

        JFXTreeTableColumn<Contact, String> surnameColumn = new JFXTreeTableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Contact, String> param) {
                return param.getValue().getValue().surname;
            }
        });

        JFXTreeTableColumn<Contact, String> phoneColumn = new JFXTreeTableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Contact, String> param) {
                return param.getValue().getValue().phone;
            }
        });

        JFXTreeTableColumn<Contact, String> addressColumn = new JFXTreeTableColumn<>("Address");
        addressColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Contact, String> param) {
                return param.getValue().getValue().address;
            }
        });

        // Style preferences
        JFXTreeTableColumn[] columns = { nameColumn, surnameColumn, phoneColumn, addressColumn };
        for (JFXTreeTableColumn column: columns ) {
            column.setStyle("-fx-font-size: 11px");
        }

        TreeItem<Contact> root = new RecursiveTreeItem<>(filteredContacts, RecursiveTreeObject::getChildren);
        contactsTable.getColumns().setAll(nameColumn, surnameColumn, phoneColumn, addressColumn);
        contactsTable.setRoot(root);
        contactsTable.setShowRoot(false);

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                contactsTable.setPredicate((Predicate<TreeItem<Contact>>) treeItem ->
                        treeItem.getValue().name.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().surname.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().phone.get().contains(newValue)
                ));
    }

    @FXML
    private JFXTreeTableView contactsTable;

    @FXML
    private JFXTextField searchField;

    @FXML
    private void onAddContact(ActionEvent event) {
        SwitchScene<FormController> switchScene = new SwitchScene<>("../views/contactForm.fxml");
        switchScene.switchScene(event);
    }

    void addContact(String name) {
       Main.data.add(new Contact(name, name, name, name));
    }

}

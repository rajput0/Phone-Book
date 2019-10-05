import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

public class Main extends javafx.application.Application {
    Label lblName, lblNumberTitle, lblNumber, lblNoContact;
    Button btnAdd, btnEdit, btnCancelAdd, btnDoneAdd, btnDoneEdit, btnCancelEdit, btnDelete, btnAddFirstContact;
    TextField txtNameAdd, txtNumberAdd, txtNameEdit, txtNumberEdit;

    ObservableList<Contact> mainDb = FXCollections.observableArrayList();
    TextField search;
    ListView<Contact> contacts;

    File file = new File("src/phonebook.db");

    @Override
    public void start(Stage stage) throws Exception {
        // creating the styling strings to implement it afterwards
        String btnStyle =
            "-fx-background-color: #475561;" +
            "-fx-border-color: white;" +
            "-fx-text-fill: white;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 3;" +
            "-fx-font-size: 10;";

        String msgStyle =
            "-fx-text-fill: white;" +
            "-fx-padding: 10;";

        String controlBoxStyle =
            "-fx-background-color: #475561;" +
            "-fx-padding: 10;";

        String txtStyle =
            "-fx-background-color: #5b6e7e;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);";

        String lblNameStyle =
            "-fx-font-size: 20;" +
            "-fx-text-fill: white;";

        String lblStyle =
            "-fx-text-fill: white;";

        // creating the leftpane of the application
        // this will include the search bar and the listview in which the search results will be displayed
        search = new TextField();
            search.setPromptText("Search");
            search.setStyle(txtStyle);
        contacts = new ListView<>();
            contacts.setStyle("-fx-control-inner-background: #475561;");
        VBox leftBox = new VBox(search, contacts);
            leftBox.setStyle("-fx-background-color: #475561;");
            leftBox.setSpacing(10);
            leftBox.setPadding(new Insets(10));

        // creating the contact view of the application
        // will display the search results in details
        lblName = new Label("Name");
            lblName.setStyle(lblNameStyle);
        lblNumberTitle = new Label("Number: ");
        lblNumber = new Label("");
            lblNumber.setStyle(lblStyle);
        HBox numberBox = new HBox(lblNumberTitle, lblNumber);
            numberBox.setAlignment(Pos.CENTER);
            numberBox.setPadding(new Insets(20, 0, 0, 0));
        btnAdd = new Button("+");
            btnAdd.setStyle(btnStyle);
        btnEdit = new Button("Edit");
            btnEdit.setStyle(btnStyle);
        btnDelete = new Button("Delete");
            btnDelete.setStyle(btnStyle);
        VBox contactViewBox = new VBox(lblName, numberBox);
            contactViewBox.setStyle("-fx-background-color: #475561");
            contactViewBox.setAlignment(Pos.CENTER);
        HBox controlBoxOne = new HBox(btnAdd, btnEdit, btnDelete);
            controlBoxOne.setStyle(controlBoxStyle);
            controlBoxOne.setSpacing(5);

        // creating the add view of the application
        // when the user wants to add a number to the database
        txtNameAdd = new TextField();
            txtNameAdd.setPromptText("Name");
            txtNameAdd.setStyle(txtStyle);
        txtNumberAdd = new TextField();
            txtNumberAdd.setPromptText("Phone");
            txtNumberAdd.setStyle(txtStyle);
//        HBox numberBoxAdd = new HBox(lblNumberTitle, txtNumberAdd);
//            numberBoxAdd.setAlignment(Pos.CENTER);
//            numberBoxAdd.setPadding(new Insets(20, 0, 0, 0));
        VBox contactAddBox = new VBox(txtNameAdd, txtNumberAdd);
            contactAddBox.setStyle("-fx-background-color: #475561");
            contactAddBox.setPadding(new Insets(10));
            contactAddBox.setAlignment(Pos.CENTER);
            contactAddBox.setSpacing(10);
        btnCancelAdd = new Button("Cancel");
            btnCancelAdd.setStyle(btnStyle);
        btnDoneAdd = new Button("Done");
            btnDoneAdd.setStyle(btnStyle);
            btnDoneAdd.setDisable(true);
            btnDoneAdd.setDefaultButton(true);
        HBox controlBoxTwo = new HBox(btnCancelAdd, btnDoneAdd);
            controlBoxTwo.setStyle(controlBoxStyle);
            controlBoxTwo.setSpacing(5);

        // edit view 
        txtNameEdit = new TextField();
            txtNameEdit.setStyle(txtStyle);
        txtNumberEdit = new TextField();
            txtNumberEdit.setStyle(txtStyle);
        HBox numberBoxEdit = new HBox(lblNumberTitle, txtNumberEdit);
            numberBoxEdit.setAlignment(Pos.CENTER);
            numberBoxEdit.setPadding(new Insets(20, 0, 0, 0));
        VBox contactEditBox = new VBox(txtNameEdit, txtNumberEdit);
            contactEditBox.setStyle("-fx-background-color: #475561");
            contactEditBox.setPadding(new Insets(10));
            contactEditBox.setAlignment(Pos.CENTER);
            contactEditBox.setSpacing(10);
        btnCancelEdit = new Button("Cancel");
            btnCancelEdit.setStyle(btnStyle);
        btnDoneEdit = new Button("Done");
            btnDoneEdit.setStyle(btnStyle);
            btnDoneEdit.setDefaultButton(true);
        HBox controlBoxThree = new HBox(btnCancelEdit, btnDoneEdit);
            controlBoxThree.setStyle(controlBoxStyle);
            controlBoxThree.setSpacing(5);

        // no contact view
        // part of the application to be displayed when there is no contacts in the database
        lblNoContact = new Label("No contacts added yet");
            lblNoContact.setStyle(msgStyle);
        btnAddFirstContact = new Button("Add Contact");
            btnAddFirstContact.setStyle(btnStyle);
        VBox noContactPane = new VBox(lblNoContact, btnAddFirstContact);
            noContactPane.setAlignment(Pos.CENTER);
            noContactPane.setStyle("-fx-background-color: #475561");

        BorderPane rightBox = new BorderPane();
            rightBox.setCenter(contactViewBox);
            rightBox.setBottom(controlBoxOne);

        BorderPane pane = new BorderPane();
            pane.setLeft(leftBox);
            pane.setCenter(rightBox);
            
            
            // until now the creating the elements, aligning and styling them is over
            // now onwards the main functionality of the application has to be coded
        // populateDb() will look into the database on the running of the application if any contacts
        // are there already in the database?
        populateDb();
        contacts.getSelectionModel().selectFirst();
        Contact c = contacts.getSelectionModel().getSelectedItem();
        // Contact is a class by which we can create contacts objects
        
        if (c != null) {
            lblName.setText(c.getName());
            lblNumber.setText(c.getNumber());
            // if the database is empty at the beginning when the user run the application the
            // noContactPane is set to the stage else the contact is stored in the contact object
        } else {
            pane.setCenter(noContactPane);
        }

        Scene scene = new Scene(pane, 600, 300);
        stage.setScene(scene);
        stage.setTitle("Phonebook");
        stage.show();

//        * * * events * * *
//        contacts.setOnMouseClicked(e -> {
//            Contact contact = contacts.getSelectionModel().getSelectedItem();
//            System.out.println(contact.getName() + ": " + contact.getNumber());
//        });

        contacts.getSelectionModel().selectedItemProperty().addListener(e -> {
            try {
                Contact contact = contacts.getSelectionModel().getSelectedItem();
                lblName.setText(contact.getName());
                lblNumber.setText(contact.getNumber());
            } catch (NullPointerException ex) {
                // avoid NullPointerException
            }
        });

        // searching according to each keyword that user types in the search box.
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue);
        });

        // event listener for the add contact button
        btnAdd.setOnAction(e -> {
            rightBox.setCenter(contactAddBox);
            rightBox.setBottom(controlBoxTwo);

            leftBox.setDisable(true);

            txtNameAdd.requestFocus();
        });

        // when there is no contacts and the user tries to add a contact
        btnAddFirstContact.setOnAction(e -> {
            pane.setCenter(rightBox);
            btnAdd.fire();
        });

        
        // cancel button in the add contacts screen
        btnCancelAdd.setOnAction(e -> {
            if (mainDb.size() == 0) {
                pane.setCenter(noContactPane);
            } else {
                txtNameAdd.setText("");
                txtNumberAdd.setText("");

                rightBox.setCenter(contactViewBox);
                rightBox.setBottom(controlBoxOne);

                leftBox.setDisable(false);
            }
        });

        // edit button fetch the contact and allows user to edit it.
        btnEdit.setOnAction(e -> {
            rightBox.setCenter(contactEditBox);
            rightBox.setBottom(controlBoxThree);

            leftBox.setDisable(true);

            Contact contact = contacts.getSelectionModel().getSelectedItem();
            txtNameEdit.setText(contact.getName());
            txtNumberEdit.setText(contact.getNumber());
        });
        
        // the cancel button in the edit contact screen
        btnCancelEdit.setOnAction(e -> {
            rightBox.setCenter(contactViewBox);
            rightBox.setBottom(controlBoxOne);

            leftBox.setDisable(false);
        });

        txtNameAdd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNumberAdd.getText().equals("")) {
                    btnDoneAdd.setDisable(true);
                } else {
                    btnDoneAdd.setDisable(false);
                }
            }
        });

        txtNumberAdd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNameAdd.getText().equals("")) {
                    btnDoneAdd.setDisable(true);
                } else {
                    btnDoneAdd.setDisable(false);
                }
            }
        });

        txtNameEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneEdit.setDisable(true);
            } else {
                if (txtNumberEdit.getText().equals("")) {
                    btnDoneEdit.setDisable(true);
                } else {
                    btnDoneEdit.setDisable(false);
                }
            }
        });

        txtNumberEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNameEdit.getText().equals("")) {
                    btnDoneEdit.setDisable(true);
                } else {
                    btnDoneEdit.setDisable(false);
                }
            }
        });

        btnDoneAdd.setOnAction(e -> {
            if (!isNumber(txtNumberAdd.getText())) {
                showAlert("Phone number can only contain digits!");
                txtNumberAdd.requestFocus();
            } else {
                // save to db
                Contact contact = new Contact(txtNameAdd.getText(), txtNumberAdd.getText());

                boolean exists = false;
                for (int i = 0; i < mainDb.size(); i++) {
                    if (mainDb.get(i).equals(contact)) {
//                    System.out.println("Contact with this name already exists!");
                        showAlert("Contact with this name already exists!");
                        exists = true;
                        btnDoneAdd.setDisable(true);
                    }
                }

                if (!exists) {
                    mainDb.add(contact);

                    syncFile(); // update file

                    // switch to contact view
                    txtNameAdd.setText("");
                    txtNumberAdd.setText("");

                    rightBox.setCenter(contactViewBox);
                    rightBox.setBottom(controlBoxOne);

                    contacts.setItems(mainDb.sorted()); // add contact to listview

                    contacts.getSelectionModel().select(contact); // select added contact

                    leftBox.setDisable(false);
                }

                btnDelete.setDisable(false);
            }
        });

        btnDoneEdit.setOnAction(e -> {
            if (!isNumber(txtNumberEdit.getText())) {
                showAlert("Phone number can only contain digits!");
                txtNumberEdit.requestFocus();
            } else {
                // save to db
                Contact contact = contacts.getSelectionModel().getSelectedItem();
                mainDb.remove(contact);

                Contact new_contact = new Contact(txtNameEdit.getText(), txtNumberEdit.getText());
                mainDb.add(new_contact);

                syncFile(); // update file

                // switch to contact view
                rightBox.setCenter(contactViewBox);
                rightBox.setBottom(controlBoxOne);

                contacts.setItems(mainDb.sorted()); // add contact to listview

                contacts.getSelectionModel().select(new_contact); // select added contact

                leftBox.setDisable(false);
            }
        });
        
        // deleteing the contact (event listener for the contact button)
        btnDelete.setOnAction(e -> {
            Contact contact = contacts.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Contact");
            alert.setHeaderText("You are about to delete the entry for " + contact.getName());
            alert.setContentText("Do you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
//                Contact contact = contacts.getSelectionModel().getSelectedItem();
                mainDb.remove(contact);
                syncFile();

                contacts.setItems(mainDb.sorted());

                if (mainDb.size() == 0) {
                    pane.setCenter(noContactPane);
                }
            }
        });
    }
    
    // populateDb() will look into the database on the running of the application if any contacts
    // are there already in the database?
    private void populateDb() {
        try {
            Scanner input = new Scanner(file);

            while (input.hasNext()) {
                String line = input.nextLine();
                String[] args = line.split(":\\s*");
                Contact contact = new Contact(args[0], args[1]);
                mainDb.add(contact);
            }

            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error on populateDb(): " + e.getMessage());
        }

        contacts.setItems(mainDb.sorted());
    }

    
    // the actual functionality of the search method
    private void search(String query) {
        ObservableList<Contact> tempDb = FXCollections.observableArrayList();
        for (int i = 0; i < mainDb.size(); i++) {
            Contact contact = mainDb.get(i);
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                tempDb.add(contact);
            }
        }

        contacts.setItems(tempDb.sorted());
        contacts.getSelectionModel().selectFirst();
    }

    // updating the file
    private void syncFile() {
        try {
            PrintWriter output = new PrintWriter(file);
            for (Contact contact :
                    mainDb) {
                output.printf("%s:%s\n", contact.getName(), contact.getNumber());
            }
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // creating a showAlert method so that programmer don't have to create a alert obj and configure it
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // creating a showAlert method so that programmer don't have to create a alert obj and configure it
    public void showAlert(String message, String contactName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
//        alert.setHeaderText("Delete Contact");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // exception handling for the invalid input from the user
    public boolean isNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

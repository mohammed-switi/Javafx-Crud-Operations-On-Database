package com.example.databaseassignment;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class View extends Application {



    @Override
    public void start(Stage stage) throws SQLException {
        try {
            DAO.ConnectDB();
        } catch (Exception e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't Connect with Database");
            alert.showAndWait();
            System.exit(1);
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        ArrayList<String> tableNames = DAO.getTableNames();
        Tab addressTab = new Tab(tableNames.get(0));
        Tab carTab = new Tab(tableNames.get(1));
        Tab carPartTab = new Tab(tableNames.get(2));
        Tab customerTab = new Tab(tableNames.get(3));
        Tab deviceTab = new Tab(tableNames.get(4));
        Tab manufactureTab = new Tab(tableNames.get(5));
        Tab ordersTab = new Tab(tableNames.get(6));
        Tab aboutTab=new Tab("About");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15, 20, 15, 20));

        // Header
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 10px;");
        Label titleLabel = new Label("About: ");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        headerBox.getChildren().add(titleLabel);
        borderPane.setTop(headerBox);

        // Main Content
        VBox mainContent = new VBox(10);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().add(createStyledLabel("Version: 1.0"));
        mainContent.getChildren().add(createStyledLabel("My Name is Mohammed Sowaity"));
        mainContent.getChildren().add(createStyledLabel("I am a Software Engineer Student at Bethlehem University"));
        mainContent.getChildren().add(createStyledLabel("This application is designed to display database tables"));
        mainContent.getChildren().add(createStyledLabel("Allowing users to make"));
        mainContent.getChildren().add(createStyledLabel("search, insert, update, and delete"));
        mainContent.getChildren().add(createStyledLabel("operations on the database"));
        mainContent.getChildren().add(createStyledLabel("Developed by: Mohammed Sowaity"));
        mainContent.getChildren().add(createStyledLabel("Supervised by: Dr. Anas Samara"));
        borderPane.setCenter(mainContent);

        aboutTab.setContent(borderPane);

        // Footer
        HBox footerBox = new HBox();
        Label footerLabel = new Label("© 2023 Sowaity");
        footerBox.getChildren().add(footerLabel);
        borderPane.setBottom(footerBox);

        TableView addressTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(0)));
        addressTable.setId(tableNames.get(0));
        addressTab.setContent(addressTable);

        TableView carTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(1)));
        carTable.setId(tableNames.get(1));
        carTab.setContent(carTable);

        TableView carPartTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(2)));
        carPartTable.setId(tableNames.get(2));
        carPartTab.setContent(carPartTable);

        TableView customerTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(3)));
        customerTable.setId(tableNames.get(3));
        customerTab.setContent(customerTable);

        TableView deviceTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(4)));
        deviceTable.setId(tableNames.get(4));
        deviceTab.setContent(deviceTable);

        TableView manufactureTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(5)));
        manufactureTable.setId(tableNames.get(5));
        manufactureTab.setContent(manufactureTable);

        TableView ordersTable = DAO.UpdatingTableViews(DAO.selectTableQuery(tableNames.get(6)));
        ordersTable.setId(tableNames.get(6));
        ordersTab.setContent(ordersTable);


        HashMap<String, Control> controlHashMap = new HashMap<>();
        controlHashMap = getControlHashMap(addressTable, carTable, carPartTable, customerTable, deviceTable, manufactureTable, ordersTable);


        tabPane.getTabs().addAll(addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab,aboutTab);


        VBox controlsVbox = new VBox();
        VBox.setMargin(controlsVbox, new Insets(20, 0, 0, 0));
        HashMap<String, HBox> controlHbox = new HashMap<>();
        controlHbox = controlLabeldHashMapHboxs(controlHashMap);
        StringBuilder selectedTabText = new StringBuilder();


        HBox buttonsHbox = new HBox();
        Button searchButton = new Button();
        Button insertButton = new Button();
        Button updateButton = new Button();
        Button deleteButton = new Button();
        Button clearButton = new Button();

        Tooltip searchToolTip = new Tooltip("Enter any of the Text Fields to search");
        Tooltip insertToolTip = new Tooltip("Enter all of the Text Fields to insert");
        Tooltip updateToolTip = new Tooltip("Enter a keyword for the rows you want to update then enter the new values");
        Tooltip deleteToolTip = new Tooltip("Selected Any of the rows to delete");
        Tooltip clearToolTip = new Tooltip("click clear to clear the tables");

        searchToolTip.setShowDelay(Duration.millis(200));
        insertToolTip.setShowDelay(Duration.millis(200));
        updateToolTip.setShowDelay(Duration.millis(200));
        deleteToolTip.setShowDelay(Duration.millis(200));
        clearToolTip.setShowDelay(Duration.millis(200));

        searchButton.setTooltip(searchToolTip);
        insertButton.setTooltip(insertToolTip);
        updateButton.setTooltip(updateToolTip);
        deleteButton.setTooltip(deleteToolTip);
        clearButton.setTooltip(clearToolTip);


        searchButton.setMinWidth(60);
        insertButton.setMinWidth(60);
        updateButton.setMinWidth(60);
        deleteButton.setMinWidth(60);
        clearButton.setMinWidth(60);

        searchButton.setText("Search");
        insertButton.setText("Insert");
        updateButton.setText("Update");
        deleteButton.setText("Delete");
        clearButton.setText("Clear");

        HBox.setMargin(searchButton, new Insets(0, 15, 0, 5));
        HBox.setMargin(insertButton, new Insets(0, 15, 0, 0));
        HBox.setMargin(updateButton, new Insets(0, 15, 0, 0));
        HBox.setMargin(deleteButton, new Insets(0, 15, 0, 0));
        HBox.setMargin(clearButton, new Insets(0, 15, 0, 0));


        buttonsHbox.getChildren().addAll(searchButton, insertButton, updateButton, deleteButton, clearButton);

        StringBuilder selectedTabName = new StringBuilder();
        HashMap<String, Control> involvedControls = new HashMap<>();
        replaceControlsToSuitTab(tabPane, controlHbox, controlsVbox, selectedTabName, involvedControls, controlHashMap, addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab);


        searchRequest(selectedTabName, involvedControls, searchButton, clearButton, addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab);
        resetTables(selectedTabName, involvedControls, clearButton, searchButton, insertButton, updateButton, deleteButton);
        insertRequest(selectedTabName, involvedControls, insertButton, clearButton, addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab);
        updateRequest(selectedTabName, involvedControls, controlsVbox, updateButton, clearButton, searchButton, insertButton, deleteButton, addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab);
        deleteRequest(selectedTabName, deleteButton, clearButton, involvedControls, addressTab, carTab, carPartTab, customerTab, deviceTab, manufactureTab, ordersTab);

        VBox CRUDVbox = new VBox();
        VBox.setMargin(buttonsHbox, new Insets(80, 0, 0, 50));
        CRUDVbox.getChildren().addAll(controlsVbox, buttonsHbox);


        HBox root = new HBox();
        root.getChildren().addAll(tabPane, CRUDVbox);

        Scene scene = new Scene(root, 900, 550);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        return label;
    }

    public static HashMap<String, Control> getControlHashMap(TableView... tableViews) throws SQLException {
        HashMap<String, Control> controlHashMap = new HashMap<>();

        for (TableView tableView : tableViews) {

            ArrayList<String> getColNames = getColumnsNames(tableView);
            Set<String> foreignKeys = DAO.getAllForeignKeyNames(tableView.getId());

            for (String columnName : getColNames) {
                if (columnName.equals( "Select To Delete")) continue;
                if (foreignKeys.contains(columnName)) {
                    ComboBox<String> comboBox = new ComboBox<>();
                    comboBox.setPrefWidth(300);
                    comboBox.setMaxHeight(20);
                    HBox.setMargin(comboBox, new Insets(0, 0, 0, 0));
                    comboBox.setId("FKEY");
                    controlHashMap.put(columnName, comboBox);
                    continue;
                }
                TextField textField = new TextField();
                textField.setPromptText(columnName);
                textField.setPrefWidth(300);
                textField.setMaxHeight(20);
                textField.setId("NORMAL");
                HBox.setMargin(textField, new Insets(0, 0, 0, 0));
                controlHashMap.put(columnName, textField);

            }
        }
        return controlHashMap;
    }

    public static HashMap<String, HBox> controlLabeldHashMapHboxs(HashMap<String, Control> controlsMap) {
        HashMap<String, HBox> controlWithLabel = new HashMap<>();
        for (String text : controlsMap.keySet()) {
            HBox hBox = new HBox();
            Label label = new Label(Character.toUpperCase(text.charAt(0)) + text.substring(1));
            label.setMinWidth(55);
            hBox.getChildren().addAll(label, controlsMap.get(text));
            VBox.setMargin(hBox, new Insets(10));
            controlWithLabel.put(text, hBox);
        }
        return controlWithLabel;
    }


    public static ArrayList<String> getColumnsNames(TableView tableView) {
        ArrayList<String> colNames = new ArrayList<>();

        for (Object column : tableView.getColumns()) {
            TableColumn column1 = (TableColumn) column;
            if (column1.getText().equals("Select To Delete")) continue;
            colNames.add(column1.getText());
        }

        return colNames;

    }

    public static void replaceControlsToSuitTab(TabPane tabPane, HashMap<String, HBox> hBoxHashMap, VBox controlsVbox, StringBuilder selectedTabName, HashMap<String, Control> involvedControls, HashMap<String, Control> controlHashMap, Tab... tabs) {
        ArrayList<String> firstTabColNames = getColumnsNames((TableView) tabs[0].getContent());
        selectedTabName.append(tabs[0].getText());
        for (String colName : firstTabColNames) {
            if (colName == "Select To Delete") continue;
            controlsVbox.getChildren().add(hBoxHashMap.get(colName));
            involvedControls.put(colName, controlHashMap.get(colName));
        }

        for (Tab tab : tabs) {
            tab.setOnSelectionChanged(event -> {
                try {
                    involvedControls.clear();
                    controlsVbox.getChildren().clear();
                    ArrayList<String> colNames = getColumnsNames((TableView) tab.getContent());
                    selectedTabName.delete(0, selectedTabName.length());
                    selectedTabName.append(tab.getText());

                    Set<String> foriegnKeys = DAO.getAllForeignKeyNames(selectedTabName.toString());

                    for (String colName : colNames) {
                        if (colName == "Select To Delete") continue;
                        controlsVbox.getChildren().add(hBoxHashMap.get(colName));
                        involvedControls.put(colName, controlHashMap.get(colName));

                        if (foriegnKeys.contains(colName)) {
                            ArrayList<String> refrencedMainKey = DAO.getReferencedMainKeyAndItsTabel(colName);
                            ((ComboBox) involvedControls.get(colName)).setItems(DAO.getColumnData(refrencedMainKey.get(1), refrencedMainKey.get(0)));

                        } else {

                            ((TextField) involvedControls.get(colName)).setText("");
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            });
        }

    }


    public static Tab searchForTabByName(StringBuilder selectedName, Tab... tabs) {
        for (Tab tab : tabs)
            if (tab.getText().equals(selectedName.toString())) {
                return tab;
            }
        return null;
    }


    public static void searchRequest(StringBuilder tableName, HashMap<String, Control> involvedControls, Button searchButton, Button resetButton, Tab... tabs) {
        searchButton.setOnAction(actionEvent -> {

            if (!validateInputs(tableName,involvedControls,tabs)){
                resetButton.fire();
                return;
            }



            Tab selectedTab = searchForTabByName(tableName, tabs);
            TableView tableView = null;
            try {
                tableView = DAO.searchRequestQuery(selectedTab.getText(), involvedControls);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            selectedTab.setContent(tableView);
        });

    }


    public static void resetTables(StringBuilder tableName, HashMap<String, Control> involvedControls, Button clearButton, Button searchButton, Button insertButton, Button updateButton, Button deleteButton) {

        clearButton.setOnAction(actionEvent -> {
            searchButton.setDisable(false);
            insertButton.setDisable(false);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            clearTextFields(tableName, involvedControls);
            searchButton.fire();
        });
    }

    public static void insertRequest(StringBuilder tableName, HashMap<String, Control> involvedControls, Button insertButton, Button resetButton, Tab... tabs) {
        insertButton.setOnAction(actionEvent -> {

            if (!validateInputs(tableName,involvedControls,tabs)){
                resetButton.fire();
                return;
            }
            if (!allFieldsAreFilled(involvedControls)){
                resetButton.fire();
                return;
            }

            Tab selectedTab = searchForTabByName(tableName);
            String tableNameString = String.valueOf(tableName);
            try {
                switch (tableNameString) {
                    case "address": {
                        Controller.postAddress(tableNameString, involvedControls);
                        break;
                    }
                    case "car": {
                        Controller.postCar(tableNameString, involvedControls);
                        break;
                    }
                    case "car_part": {
                        Controller.postCarPart(tableNameString, involvedControls);
                        break;
                    }
                    case "customer": {
                        Controller.postCustomer(tableNameString, involvedControls);
                        break;
                    }
                    case "device": {
                        Controller.postDevice(tableNameString, involvedControls);
                        break;
                    }
                    case "manufacture": {
                        Controller.postManufacture(tableNameString, involvedControls);
                        break;
                    }
                    case "orders": {
                        Controller.postOrders(tableNameString, involvedControls);
                        break;
                    }
                }

                resetButton.fire();
            } catch (Exception e) {
               Alert alert=new Alert(Alert.AlertType.ERROR);
               alert.setContentText(e.getMessage());
               alert.show();
            }
        });
    }


    public static void updateRequest(StringBuilder tableName, HashMap<String, Control> involvedControls, VBox controlVbox, Button updateButton, Button resetButton, Button searchButton, Button insertButton, Button deleteButton, Tab... tabs) {

        BooleanProperty isClickedProperty = new SimpleBooleanProperty(false);
        final HashMap<String, String>[] toUpdate = new HashMap[]{null};
        updateButton.setOnAction(actionEvent -> {

            if (!validateInputs(tableName,involvedControls,tabs)){
                resetButton.fire();
                return;
            }

            if (!atLeastOneFieldIsFilled(involvedControls)){
                resetButton.fire();
                return;
            }


            if (!isClickedProperty.get()) {
                searchButton.fire();
                searchButton.setDisable(true);
                insertButton.setDisable(true);
                deleteButton.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("The table content is displayed, the update action will be applied.");
                alert.showAndWait();
                toUpdate[0] = clearTextFields(tableName, involvedControls);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("Now Enter the new Values to update");
                alert1.show();


            } else {
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation Dialog");
                confirmationDialog.setHeaderText("Are you sure you want to proceed?");
                confirmationDialog.getButtonTypes().setAll(yesButton, noButton);
                String tableNameString = String.valueOf(tableName);

                confirmationDialog.showAndWait().ifPresent(response -> {

                    if (response == yesButton) {
                        try {
                            DAO.updateFields(tableNameString, involvedControls, toUpdate[0]);
                        } catch (SQLException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText(e.getMessage());
                            alert.show();
                        } finally {
                            searchButton.setDisable(false);
                            insertButton.setDisable(false);
                            deleteButton.setDisable(false);
                        }
                        resetButton.fire();

                    } else if (response == noButton) {
                        resetButton.fire();
                    }
                });


            }
            isClickedProperty.set(!isClickedProperty.get());
        });
    }


    public static HashMap<String, String> clearTextFields(StringBuilder tableName, HashMap<String, Control> involvedControls) {
        HashMap<String, String> toUpdate = new HashMap<>();
        for (String colName : involvedControls.keySet()) {
            try {
                if (!DAO.getAllForeignKeyNames(tableName.toString()).contains(colName)) {
                    toUpdate.put(colName, ((TextField) involvedControls.get(colName)).getText());
                    ((TextField) involvedControls.get(colName)).setText("");
                } else {
                    toUpdate.put(colName, (String) ((ComboBox) involvedControls.get(colName)).getValue());
                    ((ComboBox) involvedControls.get(colName)).setValue("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return toUpdate;
    }


    public static void deleteRequest(StringBuilder tableName, Button deleteButton, Button resetButton, HashMap<String, Control> involvedControls, Tab... tabs) {

        deleteButton.setOnAction(actionEvent -> {




            Tab selectedTab = searchForTabByName(tableName, tabs);
            DAO.CheckableTableView tableView = (DAO.CheckableTableView) selectedTab.getContent();
            Map<Integer, Boolean> checkMap = tableView.getCheckMap();
            HashMap<String, String> toDeleteRow = new HashMap<>();

            for (Integer i : checkMap.keySet()) {
                if (checkMap.get(i)) {
                    ObservableList<String> list = (ObservableList) tableView.getItems().get(i);

                    ArrayList<String> cols = getColumnsNames(tableView);
                    int j = 0;
                    for (String col : cols) {

                        toDeleteRow.put(col, list.get(j));
                        j++;
                    }
                    ButtonType yesButton = new ButtonType("Yes");
                    ButtonType noButton = new ButtonType("No");
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation Dialog");
                    confirmationDialog.setHeaderText("Are you sure you want to proceed?");
                    confirmationDialog.getButtonTypes().setAll(yesButton, noButton);
                    String tableNameString = String.valueOf(tableName);

                    confirmationDialog.showAndWait().ifPresent(response -> {

                        if (response == yesButton) {
                            try {
                                DAO.DeleteRequest(tableName.toString(), toDeleteRow);
                                resetButton.fire();

                            } catch (SQLException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText(e.getMessage());
                                alert.show();
                                resetButton.fire();
                            }


                        } else if (response == noButton) {
                            resetButton.fire();
                        }
                    });

                }
            }

        });

    }

    public static boolean validateInputs(StringBuilder tableName, HashMap<String, Control> involvedControl, Tab... tabs) {
        Tab selectedTab = searchForTabByName(tableName, tabs);
        TableView tableView = (TableView) selectedTab.getContent();
        Collection<Control> list = involvedControl.values();
        List<Control> controls = new ArrayList<>(list);
        HashMap<String, String> colNameWithType = new HashMap<>();
        for (Object col : tableView.getColumns()) {
            TableColumn col1 = (TableColumn) col;
            if (col1.getText().equals("Select To Delete")) continue;
            colNameWithType.put(col1.getText(), ((DAO.TableColumnWithFieldType<ObservableList<String>, String>) col1).getFieldType());
        }

        for (String colName: colNameWithType.keySet())
            if (!checkDataType(colNameWithType.get(colName),involvedControl.get(colName),colName))
                return false;

        return true;
    }


    public static boolean checkDataType(String supposedDataType, Control data, String colName) {
           Alert alert=new Alert(Alert.AlertType.WARNING);
            String value;
        if (data.getId().equals("FKEY")){
            if (((ComboBox)data).getValue()!=null)
            value= ((ComboBox)data).getValue().toString();
            else  value="";
        }else {
             value= ((TextField)data).getText();
        }
        if (value.equals("")) return true;

        if (supposedDataType.equals("String")){
            if (isNumeric(value)){
                alert.setContentText(colName+" must be Text");
                alert.show();
                return false;
            }
        }else if (supposedDataType.equals("Integer")){
             if (!isNumeric(value)) {
                 alert.setContentText(colName+" must be anInteger");
                 alert.show();
                 return false;}
             if (!isInteger(value)){
                 alert.setContentText(colName+" must be an Integer");
                 alert.show();
                 return false;}
        }else if (supposedDataType.equals("BigDecimal")){
            if (!isNumeric(value)){
                alert.setContentText(colName+" must be a Decimal");
                alert.show();
                return false;
            }
        }else return true;


      return true;
    }

    public static boolean isInteger(String strNum){
        double value=Double.parseDouble(strNum);
        if (value-(int) value ==0) return true;
        else return false;
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }


    public static boolean allFieldsAreFilled(HashMap<String, Control> involvedControl) {

        Collection<Control> list = involvedControl.values();
        List<Control> controls = new ArrayList<>(list);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("You must Fill All fields");
        String value="";
        for (String colName : involvedControl.keySet()) {

            if (involvedControl.get(colName).getId().equals("FKEY")){
                if (((ComboBox)involvedControl.get(colName)).getValue()!=null){
                 value = (String) ((ComboBox) involvedControl.get(colName)).getValue();
                if (value.equals("") || value == null) {
                    alert.show();
                    return false;
                }
            }
            } else  {
                value = ((TextField) involvedControl.get(colName)).getText();
                if (value.equals("") || value == null) {
                    alert.show();
                    return false;
                }

            }
        }
        return true;
    }


    public static boolean atLeastOneFieldIsFilled(HashMap<String, Control> involvedControl) {

        Collection<Control> list = involvedControl.values();
        List<Control> controls = new ArrayList<>(list);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("at least fill one Field");
        String value="";
        for (String colName : involvedControl.keySet()) {


            if (involvedControl.get(colName).getId().equals("FKEY")){
                if (((ComboBox)involvedControl.get(colName)).getValue()!=null){
                 value = (String) ((ComboBox) involvedControl.get(colName)).getValue();
                if (!value.equals("") && !(value==null))
                    return true;


            }
            }else  {
                value = ((TextField) involvedControl.get(colName)).getText();
                if (!value.equals("")&& !(value==null) )
                    return true;
            }

        }
        alert.show();
        return false;
    }
}


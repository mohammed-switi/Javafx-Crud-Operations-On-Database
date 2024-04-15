package com.example.databaseassignment;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;


public  class  DAO {
    private static ObservableList<ObservableList> data;
    private static Connection connection;
    private static Set<String> foreignKeys;

    static {
        try {
            ConnectDB();
            foreignKeys = getAllForeignKeyNamesInAllTables();
        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't Connect with Database");
            alert.showAndWait();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void ConnectDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3390/dbassignment", "root", "");
    }

    public static TableView UpdatingTableViews( String SQLStatment) throws SQLException {
        CheckableTableView tableView=  new CheckableTableView();
        Map<Integer, Boolean> checkMap = new HashMap<>();

        data = FXCollections.observableArrayList();

        try {
            ResultSet rs = connection.createStatement().executeQuery(SQLStatment);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    TableColumnWithFieldType<ObservableList<String>, String> column = new TableColumnWithFieldType<>(rs.getMetaData().getColumnName(i+1));
                    final int j = i;
                     column.setCellValueFactory(param -> {
                        String value = param.getValue().get(j);
                        return new SimpleStringProperty(value);
                    });
                     String type=rs.getMetaData().getColumnClassName(i+1);
                     type= type.substring(type.lastIndexOf(".")+1);
                    column.setFieldType(type);
                    tableView.getColumns().addAll(column);
            }

            TableColumnWithFieldType<ObservableList<String>, Boolean> selectCol = new TableColumnWithFieldType<>("Select To Delete");

            selectCol.setCellValueFactory(param -> new SimpleBooleanProperty(false));
            selectCol.setCellFactory(col -> new CheckBoxTableCell<ObservableList<String>, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        CheckBox checkBox = new CheckBox();
                        checkBox.setSelected(item);
                        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            checkMap.put(getIndex(), isNowSelected);
                        });
                        setGraphic(checkBox);
                    }
                }
            });

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));

                }
                data.add(row);
            }
            tableView.setCheckMap(checkMap);
            tableView.setItems(data);
            tableView.getColumns().add(selectCol);


        } catch (SQLException e) {
            printSQLException(e);
        } finally {
            return tableView;
        }

    }

    static class CheckableTableView extends TableView {
        private Map<Integer, Boolean> checkMap = new HashMap<>();

        // Constructor and other methods
        public Map<Integer, Boolean> getCheckMap() {
            return checkMap;
        }
        public void setCheckMap(Map<Integer,Boolean> map) {
            this.checkMap=map;
        }



    }

    static class TableColumnWithFieldType<S,T> extends TableColumn<S,T> {
       String fieldType=new String();

       public <S,T> TableColumnWithFieldType (String str){
           super(str);
        }

        public <S,T> TableColumnWithFieldType (){
            super();
        }

        // Constructor and other methods
        public String getFieldType() {
            return fieldType;
        }
        public void setFieldType(String fieldType) {
            this.fieldType=fieldType;
        }


    }



    public static ArrayList<String> getTableNames(){
        ArrayList<String> tableNames=new ArrayList<>();
        try {
            ResultSet rs=connection.createStatement().executeQuery("SHOW TABLES");
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    tableNames.add(rs.getString(i));
                }
            }

        }catch (SQLException e) {
            printSQLException(e);
        }finally {
            return tableNames;
        }


    }

    public static TableView searchRequestQuery( String tableName,HashMap<String, Control> request) throws SQLException {
       String SQLStatement=searchSQLQuery(tableName,request);
       return UpdatingTableViews(SQLStatement);
    }

    public static String searchSQLQuery(String tableName,HashMap<String,Control> request){
        StringBuilder searchStatement=new StringBuilder(String.format("SELECT * From %s WHERE 1=1",tableName));

        for (String colName : request.keySet()) {
            if (foreignKeys.contains(colName)) {
                String text = (String) ((ComboBox) request.get(colName)).getValue();
                if (text!=null){
                if (!text.equals("")) {
                    searchStatement.append(String.format(" AND %s=\"%s\"", colName, text));
                }
            } }else {
                String text=((TextField) request.get(colName)).getText();
                if (!text.equals("")) {
                    searchStatement.append(String.format(" AND %s=\"%s\"", colName,text));
                }
            }
        }
        return searchStatement.toString();

    }


    public static int insertAddressDAO(AddressModel addressModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);

        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        statement.setString(1,addressModel.getCountry());
        statement.setString(2,addressModel.getCity());
        statement.setString(3,addressModel.getStreet());
        statement.setInt(4,addressModel.getBuilding());
        statement.setInt(5,addressModel.getId());

          int result = statement.executeUpdate();
        return result;
    }
    public static int insertCarDAO(CarModel carModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);

          PreparedStatement statement=connection.prepareStatement(SQLStatement);
            statement.setInt(1,carModel.getYear());
            statement.setString(2,carModel.getMade());
            statement.setString(3,carModel.getName());
            statement.setString(4,carModel.getModel());

            int result = statement.executeUpdate();
            return result;
    }
    public static int insertCustomerDAO(CustomerModel customerModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);

          PreparedStatement statement=connection.prepareStatement(SQLStatement);
            statement.setInt(1,customerModel.getAddress());
            statement.setString(2,customerModel.getLname());
            statement.setString(3,customerModel.getFname());
            statement.setInt(4,customerModel.getId());
            statement.setString(5,customerModel.getJob());

            int result = statement.executeUpdate();
            return result;
    }
    public static int insertDeviceDAO(DeviceModel deviceModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);
        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        statement.setInt(1,deviceModel.getNo());
        statement.setDouble(2,deviceModel.getPrice());
        statement.setString(3,deviceModel.getMade());
        statement.setString(4,deviceModel.getName());
        statement.setDouble(5,deviceModel.getWeight());

        int result = statement.executeUpdate();
        return result;
    }
    public static int insertManufactureDAO(ManufactureModel manufactureModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);
        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        statement.setString(1,manufactureModel.getCountry());
        statement.setString(2,manufactureModel.getCity());
        statement.setString(3,manufactureModel.getName());
        statement.setString(4,manufactureModel.getType());

        int result = statement.executeUpdate();
        return result;
    }
    public static int insertOrdersDAO(OrdersModel ordersModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);
        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        statement.setInt(1,ordersModel.getDate());
        statement.setString(2,ordersModel.getCar());
        statement.setInt(3,ordersModel.getId());
        statement.setInt(4,ordersModel.getCustomer());

        int result = statement.executeUpdate();
        return result;


    }
    public static int insertCarPartDAO(CarPartModel carPartModel,HashMap<String,Control> request,String tableName) throws SQLException {
        String SQLStatement=insertTableQuery(request,tableName);
          PreparedStatement statement=connection.prepareStatement(SQLStatement);
           statement.setString(1,carPartModel.getCar());
            statement.setInt(2,carPartModel.getPart());

            int result = statement.executeUpdate();

            return result;
    }

    public static String insertTableQuery(HashMap<String ,Control> request,String tableName){
        StringBuilder statement=new StringBuilder();
        ArrayList<String> colNames= new ArrayList<>(request.keySet());
        statement.append(String.format("INSERT INTO %s \n(",tableName));

        for (int i=0;i<colNames.size();i++){

            if (i!=colNames.size()-1){
            statement.append(colNames.get(i)+",");
            continue;
            }
            statement.append((colNames.get(i)+")\n VALUES("));
        }

        for (int i=0;i<colNames.size();i++){
          if (i!=colNames.size()-1) {
              statement.append("?,");
                continue;
          }
                statement.append("?);");
        }



        return statement.toString();

    }

    public static String selectTableQuery(String tableName){
        return "SELECT * FROM "+tableName;
    }


    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public static Set<String> getAllForeignKeyNames(String tableName) throws SQLException {
       Set<String> foreignKeyNames = new HashSet<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);

        while (foreignKeys.next()) {
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            foreignKeyNames.add(fkColumnName);
        }

        return foreignKeyNames;
    }

    public static Set<String> getAllForeignKeyNamesInAllTables() throws SQLException {
        List<String> tableNames=getTableNames();
        Set<String> foreignKeyNames = new HashSet<>();
        DatabaseMetaData metaData = connection.getMetaData();
        for (String tableName: tableNames) {
            ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);

            while (foreignKeys.next()) {
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                foreignKeyNames.add(fkColumnName);
            }
        }

        return foreignKeyNames;
    }

    public static ObservableList<String> getColumnData(String tableName,String columnName) throws SQLException {
        String sqlQuery = String.format("SELECT %s FROM %s", columnName, tableName);


        try (   Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlQuery)){

            ObservableList<String> data = FXCollections.observableArrayList();

            while (resultSet.next()) {
                // Retrieve column data for the specified column name
                data.add(resultSet.getString(columnName));
            }

            return data;
        }

    }

    public String getMainKey(String tableName) {
        String sqlQuery = String.format("SHOW KEYS FROM %s WHERE Key_name = 'PRIMARY';", tableName);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            StringBuilder primaryKeys = new StringBuilder();

            while (resultSet.next()) {
                // Assuming the "Column_name" is the column containing the primary key
                String primaryKey = resultSet.getString("Column_name");
                primaryKeys.append(primaryKey).append(", ");
            }

            if (primaryKeys.length() > 0) {
                primaryKeys.setLength(primaryKeys.length() - 2);
            }

            return primaryKeys.toString();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static int updateFields(String tableName, HashMap<String,Control> request,HashMap<String, String> toUpdate) throws SQLException {
        String SQLStatement=updateSQLQuery(tableName,request,toUpdate);
        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        int result=statement.executeUpdate();
        return result;
    }

    public static String updateSQLQuery(String tableName,HashMap<String,Control> request,HashMap<String,String> toUpdate){
        StringBuilder UpdateStatement=new StringBuilder(String.format("UPDATE %s ",tableName));
        UpdateStatement.append("SET ");

        for (String colName : request.keySet()) {
            if (foreignKeys.contains(colName)) {
                String text = (String) ((ComboBox) request.get(colName)).getValue();
                if (!text.equals("")&& text!=null) {
                    UpdateStatement.append(String.format("%s = \"%s\",", colName, text));
                }
            } else {
                String text=((TextField) request.get(colName)).getText();
                if (!text.equals("")) {
                    UpdateStatement.append(String.format("%s = \"%s\",", colName,text));
                }
            }
        }
        UpdateStatement.deleteCharAt( UpdateStatement.lastIndexOf(","));


        UpdateStatement.append(" WHERE 1=1");

        for (String colName : toUpdate.keySet()) {
            if (foreignKeys.contains(colName)) {
                String text = (String) (toUpdate.get(colName));
                if (!text.equals("")&& text!=null) {
                    UpdateStatement.append(String.format(" AND %s=\"%s\"", colName, text));
                }
            } else {
                String text=(toUpdate.get(colName));
                if (!text.equals("")) {
                    UpdateStatement.append(String.format(" AND %s=\"%s\"", colName,text));
                }
            }
        }
        return UpdateStatement.toString();


    }

    public static int DeleteRequest(String tableName,HashMap<String,String> toDelete) throws SQLException {
        String SQLStatement=DeleteQuery(tableName,toDelete);
        PreparedStatement statement=connection.prepareStatement(SQLStatement);
        int result=statement.executeUpdate();
        return result;
    }

    public static String DeleteQuery(String tableName,HashMap<String,String> toDelete){
        StringBuilder statement=new StringBuilder(String.format("DELETE FROM %s WHERE ",tableName));
        for (String col: toDelete.keySet()){
            statement.append(String.format("%s =\"%s\" AND ",col,toDelete.get(col)));
        }
        statement.delete(statement.lastIndexOf("AND "),statement.length());
        return statement.toString();
    }

    public static ArrayList<String> getReferencedMainKeyAndItsTabel(String foreignKey) throws SQLException {

        StringBuilder SQLstatement= new StringBuilder(String.format("SELECT \n" +
                "    TABLE_NAME, \n" +
                "    COLUMN_NAME, \n" +
                "    CONSTRAINT_NAME, \n" +
                "    REFERENCED_TABLE_NAME, \n" +
                "    REFERENCED_COLUMN_NAME \n" +
                "FROM \n" +
                "    INFORMATION_SCHEMA.KEY_COLUMN_USAGE \n" +
                "WHERE \n" +
                "    REFERENCED_TABLE_SCHEMA = 'dbassignment' AND \n" +
                "    COLUMN_NAME = '%s';\n",foreignKey));

        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(SQLstatement.toString());
        ArrayList<String> data=new ArrayList<>();
        while (rs.next()) {
            data.add(rs.getString("REFERENCED_COLUMN_NAME"));
            data.add(rs.getString("REFERENCED_TABLE_NAME"));
        }
        return data ;

    }



}



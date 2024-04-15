package com.example.databaseassignment;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

import javafx.scene.control.TextField;


import java.sql.SQLException;

import java.util.HashMap;

public class Controller {

    public static void postAddress(String tableName,HashMap<String,Control> request) throws SQLException {


        int id =Integer.parseInt(((TextField)request.get("id")).getText());
        int building =Integer.parseInt(((TextField)request.get("buidling")).getText());
        String street=((TextField)request.get("street")).getText();
        String city=((TextField)request.get("city")).getText();
        String country=((TextField)request.get("country")).getText();

        AddressModel addressModel=new AddressModel();
        addressModel.setId(id);
        addressModel.setBuilding(building);
        addressModel.setStreet(street);
        addressModel.setCity(city);
        addressModel.setCountry(country);

        DAO.insertAddressDAO(addressModel,request,tableName);

    }
    public static void postCar(String tableName,HashMap<String,Control> request) throws SQLException {

        String name=((TextField)request.get("name")).getText();
        String model=((TextField)request.get("model")).getText();
        int year=Integer.parseInt(((TextField)request.get("year")).getText());
        String made=((ComboBox)request.get("made")).getValue().toString();

        CarModel carModel=new CarModel();
        carModel.setName(name);
        carModel.setModel(model);
        carModel.setYear(year);
        carModel.setMade(made);


        DAO.insertCarDAO(carModel,request,tableName);

    }

    public static void postCarPart(String tableName, HashMap<String, Control> request) throws SQLException {
        String car=((ComboBox)request.get("car")).getValue().toString();
        int part=Integer.parseInt(((ComboBox)request.get("part")).getValue().toString());

        CarPartModel carPartModel=new CarPartModel();
        carPartModel.setCar(car);
        carPartModel.setPart(part);

        DAO.insertCarPartDAO(carPartModel,request,tableName);

    }

    public static void postCustomer(String tableName,HashMap<String,Control> request) throws SQLException{

        int id=Integer.parseInt(((TextField) request.get("id")).getText());
        String fname=((TextField)request.get("f_name")).getText();
        String lname=((TextField)request.get("l_name")).getText();
        int address=Integer.parseInt(((ComboBox)request.get("address")).getValue().toString());
        String job=((TextField)request.get("job")).getText();

        CustomerModel customerModel=new CustomerModel();
        customerModel.setId(id);
        customerModel.setFname(fname);
        customerModel.setLname(lname);
        customerModel.setAddress(address);
        customerModel.setJob(job);

        DAO.insertCustomerDAO(customerModel,request,tableName);



    }
    public static void postDevice(String tableName,HashMap<String,Control> request) throws SQLException{

        int no=Integer.parseInt(((TextField) request.get("no")).getText());
        String name=((TextField)request.get("name")).getText();
       double price= Double.parseDouble(((TextField)request.get("price")).getText());
       double weight= Double.parseDouble(((TextField)request.get("weight")).getText());
        String made=((ComboBox)request.get("made")).getValue().toString();

        DeviceModel deviceModel=new DeviceModel();
        deviceModel.setNo(no);
        deviceModel.setName(name);
        deviceModel.setPrice(price);
        deviceModel.setWeight(weight);
        deviceModel.setMade(made);

        DAO.insertDeviceDAO(deviceModel,request,tableName);

    }
    public static void postManufacture(String tableName,HashMap<String,Control> request) throws SQLException{

        String name=((TextField)request.get("name")).getText();
        String type=((TextField)request.get("type")).getText();
        String city=((TextField)request.get("city")).getText();
        String country=((TextField)request.get("country")).getText();

        ManufactureModel manufactureModel=new ManufactureModel();
        manufactureModel.setName(name);
        manufactureModel.setType(type);
        manufactureModel.setCity(city);
        manufactureModel.setCountry(country);

        DAO.insertManufactureDAO(manufactureModel,request,tableName);


    }
    public static void postOrders(String tableName,HashMap<String,Control> request) throws SQLException{

        int id=Integer.parseInt(((TextField) request.get("id")).getText());
        int date=Integer.parseInt(((TextField) request.get("date")).getText());
        int customer=Integer.parseInt(((ComboBox)request.get("customer")).getValue().toString());
        String car=((ComboBox)request.get("car")).getValue().toString();

        OrdersModel ordersModel=new OrdersModel();
        ordersModel.setId(id);
        ordersModel.setDate(date);
        ordersModel.setCustomer(customer);
        ordersModel.setCar(car);

        DAO.insertOrdersDAO(ordersModel,request,tableName);



    }



}

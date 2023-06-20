package com.project.zuev.pharmacymangementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;

public class dashboardController implements Initializable {

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private Button addMedicines_btn;

    @FXML
    private Button addMedicines_clearBtn;

    @FXML
    private TableView<medicineData> addMedicines_tableView;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_brand;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_date;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_medicineID;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_price;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_productName;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_status;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_type;

    @FXML
    private Button addMedicines_deleteBtn;

    @FXML
    private AnchorPane addMedicines_form;

    @FXML
    private Button addMedicines_importBtn;

    @FXML
    private TextField addMedicines_medicineID;

    @FXML
    private TextField addMedicines_price;

    @FXML
    private TextField addMedicines_productName;

    @FXML
    private TextField addMedicines_search;

    @FXML
    private ComboBox<?> addMedicines_status;

    @FXML
    private ComboBox<?> addMedicines_type;

    @FXML
    private ImageView addMedicines_imageView;

    @FXML
    private Button addMedicines_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label dashboard_availableMed;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_chart;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_totalCustomers;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private TextField purchase_amout;

    @FXML
    private Label purchase_balance;

    @FXML
    private ComboBox<?> purchase_brand;

    @FXML
    private Button purchase_btn;


    @FXML
    private TableColumn<?, ?> purchase_col_brand;

    @FXML
    private TableColumn<?, ?> purchase_col_medicineid;

    @FXML
    private TableColumn<?, ?> purchase_col_price;

    @FXML
    private TableColumn<?, ?> purchase_col_productName;

    @FXML
    private TableColumn<?, ?> purchase_col_qty;

    @FXML
    private TableColumn<?, ?> purchase_col_type;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_medicineID;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private ComboBox<?> purchase_productName;

    @FXML
    private Label purchase_total;

    @FXML
    private ComboBox<?> purchase_type;

    @FXML
    private Label username;



    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;

    public void addMedicinesAdd(){

        String sql = "INSERT INTO medicine (medicine_id, brand, productName, type, status, price, image, date)"
                + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {

            Alert alert;

            if(addMedicines_medicineID.getText().isEmpty()
                || addMedicines_brand.getText().isEmpty()
                || addMedicines_productName.getText().isEmpty()
                || addMedicines_type.getSelectionModel().getSelectedItem() == null
                || addMedicines_status.getSelectionModel().getSelectedItem() == null
                || addMedicines_price.getText().isEmpty()
                || getData.path == null || getData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все поля");
                alert.showAndWait();
            }else {

                String checkData = "SELECT medicine_id FROM medicine WHERE medicine_id = '" +
                        addMedicines_medicineID.getText()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("ID лекарства " + addMedicines_medicineID.getText() + "уже существует");
                    alert.showAndWait();
                }else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addMedicines_medicineID.getText());
                    prepare.setString(2, addMedicines_brand.getText());
                    prepare.setString(3, addMedicines_productName.getText());
                    prepare.setString(4, (String) addMedicines_type.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String) addMedicines_status.getSelectionModel().getSelectedItem());
                    prepare.setString(6, addMedicines_price.getText());

                    String urlImage = getData.path;
                    urlImage = urlImage.replace("\\", "\\\\");


                    prepare.setString(7, urlImage);

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    addMedicineShowListData();
                }
            }

        }catch(Exception e) {e.printStackTrace();}

    }

    public void addMedicineUpdate(){

        String urlImage = getData.path;
        urlImage = urlImage.replace("\\", "\\\\");

        String sql = "UPDATE medicine SET brand '"
                +addMedicines_brand.getText()+"', productName= '"
                +addMedicines_productName.getText()+"', type= '"
                +addMedicines_type.getSelectionModel().getSelectedItem()+"', status= '"
                +addMedicines_status.getSelectionModel().getSelectedItem()+"', price= '"
                +urlImage+"'WHERE medicine_id = '"
                +addMedicines_medicineID.getText()+"'";

        connect = database.connectDb();

        try {
            Alert alert;

            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все поля");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтверждение");
                alert.setHeaderText(null);
                alert.setContentText("Вы уверены?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    addMedicineShowListData();
                    addMedicineReset();
                }


            }


        }catch (Exception e) {e.printStackTrace();}
    }

    public void addMedicineDelete(){

        String sql = "DELETE FROM medicine WHERE medicine_id = '"
                +addMedicines_medicineID.getText()+"'";

        connect = database.connectDb();

        try {

            Alert alert;

            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все поля");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Вы точно хотите удалить?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    addMedicineShowListData();
                    addMedicineReset();
                }


            }

        }catch (Exception e) {e.printStackTrace();}
    }

    public void addMedicineReset() {
        addMedicines_medicineID.setText("");
        addMedicines_brand.setText("");
        addMedicines_productName.setText("");
        addMedicines_price.setText("");
        addMedicines_type.getSelectionModel().clearSelection();
        addMedicines_status.getSelectionModel().clearSelection();
        addMedicines_imageView.setImage(null);

        getData.path = "";
    }

    private String[] addMedicineListT = {"Антибиотики", "Антивирусные", "Гормональные", "БАД", "Анальгетики"};
    public void addMedicineListType() {
        List<String> listT = new ArrayList<>();

        for (String data: addMedicineListT){
            listT.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listT);
        addMedicines_type.setItems(listData);
    }

    private String[] addMedicineStatus = {"Доступные", "Недоступные"};
    public void addMedicineListStatus(){
        List<String> listS = new ArrayList<>();
        for (String data: addMedicineStatus){
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        addMedicines_status.setItems(listData);
    }
    public void addMedicineImportImage(){

        FileChooser open = new FileChooser();
        open.setTitle("Импорт фотографии");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if(file != null){
            image = new Image(file.toURI().toString(), 92, 149, false, true);

            addMedicines_imageView.setImage(image);

            getData.path = file.getAbsolutePath();
        }

    }

    public ObservableList<medicineData> addMedicineListDate(){

        String sql = "SELECT * FROM medicine";

        ObservableList<medicineData> listData = FXCollections.observableArrayList();

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            medicineData medData;
            while(result.next()){
                medData = new medicineData(result.getInt("medicine_id"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getString("type"),
                        result.getString("status"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(medData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    private ObservableList<medicineData>addMedicineList;
    public void addMedicineShowListData(){
        addMedicineList = addMedicineListDate();

        addMedicines_col_medicineID.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        addMedicines_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addMedicines_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addMedicines_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addMedicines_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addMedicines_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addMedicines_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addMedicines_tableView.setItems(addMedicineList);
    }


    public void addMedicineSelect(){
        medicineData medData = addMedicines_tableView.getSelectionModel().getSelectedItem();
        int num = addMedicines_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < - 1){return;}

        addMedicines_medicineID.setText(String.valueOf(medData.getMedicineId()));
        addMedicines_brand.setText(medData.getBrand());
        addMedicines_productName.setText(medData.getProductName());
        addMedicines_price.setText(String.valueOf(medData.getPrice()));

        String urlImage = "file:" + medData.getImage();

        image = new Image(urlImage, 92, 149, false, true);
        addMedicines_imageView.setImage(image);

        getData.path = medData.getImage();
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(false);
        } else if (event.getSource() == addMedicines_btn) {
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(true);
            purchase_form.setVisible(false);

            addMedicineShowListData();
            addMedicineListStatus();
            addMedicineListType();
        }else if (event.getSource() == purchase_btn) {
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(true);
        }
    }


    public void displayUsername() {
        String user = getData.username;

        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
    }

    private double x = 0;
    private double y = 0;

    public void logout() {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите выйти?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("loginform.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        addMedicineShowListData();
        addMedicineListStatus();
        addMedicineListType();
    }
}

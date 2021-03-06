package org.loose.fis.sre.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.loose.fis.sre.exceptions.*;
import org.loose.fis.sre.model.Instrument;
import org.loose.fis.sre.model.Review;
import org.loose.fis.sre.services.InstrService;
import org.loose.fis.sre.services.ReviewService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BuyerController implements Initializable {


    @FXML
    private TableView<Instrument> Table = new TableView<>();
    @FXML
    private TableColumn<Instrument, String> TableName = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> TableCategory = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> TablePrice = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> TableSeller = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> TableDescr = new TableColumn<>();
    @FXML
    private TableView<Instrument> Table2 = new TableView<>();
    @FXML
    private TableColumn<Instrument, String> Hname = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> Hcat = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> Hprice = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> Hseller = new TableColumn<>();
    @FXML
    private TableColumn<Instrument, String> Hdescr = new TableColumn<>();

    private Parent root;
    private Stage stage;
    @FXML
    private Button SignOutBuyer;
    @FXML
    private Button AvailableProducts, Buy, HomePage, History, Review;
    //private ArrayList<Instrument> list = new ArrayList<>();
    ObservableList<Instrument> instr = FXCollections.observableArrayList();
    private ArrayList<Instrument> list = new ArrayList<>();
    ObservableList<Instrument> Hinstr = FXCollections.observableArrayList();
    private ArrayList<Instrument> Hlist = new ArrayList<>();
    @FXML
    private TextField InstrTobuy;
    @FXML
    private TextField Revseller;
    @FXML
    private TextArea Rtext;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableName.setCellValueFactory(new PropertyValueFactory<Instrument, String>("name"));
        TableCategory.setCellValueFactory(new PropertyValueFactory<Instrument, String>("category"));
        TablePrice.setCellValueFactory(new PropertyValueFactory<Instrument, String>("price"));
        TableSeller.setCellValueFactory(new PropertyValueFactory<Instrument, String>("seller"));
        TableDescr.setCellValueFactory(new PropertyValueFactory<Instrument, String>("descr"));
        Table.setItems(getInstruments());

        Hname.setCellValueFactory(new PropertyValueFactory<Instrument, String>("name"));
        Hcat.setCellValueFactory(new PropertyValueFactory<Instrument, String>("category"));
        Hprice.setCellValueFactory(new PropertyValueFactory<Instrument, String>("price"));
        Hseller.setCellValueFactory(new PropertyValueFactory<Instrument, String>("seller"));
        Hdescr.setCellValueFactory(new PropertyValueFactory<Instrument, String>("descr"));
        Table2.setItems(getHistory());
    }

    private ObservableList<Instrument> getInstruments()  {
        for (Instrument in : InstrService.GetRepository().find())
            if(in.getBuyer().equals("none")) {
                list.add(in);
            }

        instr.addAll(list);
        return instr;
    }
    private ObservableList<Instrument> getHistory()  {
        for (Instrument in : InstrService.GetRepository().find())
            if(in.getBuyer().equals(LoginController.getDenBuyer())) {
                Hlist.add(in);
            }

        Hinstr.addAll(Hlist);
        return Hinstr;
    }
    public void handleReviewAction() throws UsernameAlreadyExistsException {
        if(Revseller.getText().equals("") || Rtext.getText().equals("")){
            ReviewExceptions.displayInvalid();
            return;
        }
        else try {
            ReviewService.addReview(getIndex(), Rtext.getText(), LoginController.getDenBuyer(), Revseller.getText());
            ReviewExceptions.displayValid();
        } catch (SellerNotExistsException e) {
            ReviewExceptions.displayInvalid();
        }
    }
    private int getIndex()  {
        int count = 0;
        for (Review rv : ReviewService.GetRepository().find())
            count++;

        return count;
    }
    public void handleBuyAction() {
        if(InstrTobuy.getText().equals("")){
            BuyerExceptions.displayInvalid();
            return;
        }
        else {
            try {
                switch (InstrService.setBuyer(InstrTobuy.getText(), LoginController.getDenBuyer())) {
                    case 1:
                        BuyerExceptions.alreadyBought();
                        break;
                    case 2:
                        BuyerExceptions.displayValid();
                        break;
                    default:
                        break;

                }
            } catch (UsernameNotExistsException e) {
                BuyerExceptions.displayInvalid();
            }
        }

    }


    public void gotoPages(ActionEvent event)throws Exception{
        if(event.getSource() == AvailableProducts){
            this.root = (Parent)FXMLLoader.load(getClass().getClassLoader().getResource("AvailableProducts.fxml"));
            this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(this.root);
            this.stage.setTitle("Products Available");
            this.stage.setScene(scene);
            this.stage.show();
        }
        if(event.getSource() == HomePage){
            this.root = (Parent)FXMLLoader.load(getClass().getClassLoader().getResource("BuyerHomePage.fxml"));
            this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(this.root);
            this.stage.setTitle("Home Page");
            this.stage.setScene(scene);
            this.stage.show();
        }
        if(event.getSource() == Buy){
            this.root = (Parent)FXMLLoader.load(getClass().getClassLoader().getResource("Buy.fxml"));
            this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(this.root);
            this.stage.setTitle("Buy Products");
            this.stage.setScene(scene);
            this.stage.show();
        }
        if(event.getSource() == History){
            this.root = (Parent)FXMLLoader.load(getClass().getClassLoader().getResource("HistoryBuyer.fxml"));
            this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(this.root);
            this.stage.setTitle("History");
            this.stage.setScene(scene);
            this.stage.show();
        }
        if(event.getSource() == Review){
            this.root = (Parent)FXMLLoader.load(getClass().getClassLoader().getResource("BuyerReview.fxml"));
            this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(this.root);
            this.stage.setTitle("Review");
            this.stage.setScene(scene);
            this.stage.show();
        }
    }
    public static void display() {
        Stage window = new Stage();
        Parent root ;
        try {
            root = FXMLLoader.load(BuyerController.class.getClassLoader().getResource("BuyerHomePage.fxml"));
            Scene scene = new Scene(root);
            window.setTitle("Home Page");
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SignOut()
    {
        Stage stage = (Stage) SignOutBuyer.getScene().getWindow();
        stage.close();
    }
}

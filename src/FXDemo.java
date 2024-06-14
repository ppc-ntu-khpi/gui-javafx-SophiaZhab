
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Alexander 'Taurus' Babich modified by Sofiia Zhabotynska 
 */
public class FXDemo extends Application {

    private Text title;
    private Text details;
    private ComboBox clients;

    @Override
    public void start(Stage primaryStage) {

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);

        Scene scene = new Scene(border, 370, 250);

        primaryStage.setTitle("MyBank Clients");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        title = new Text("Client Name");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vbox.getChildren().add(title);

        Line separator = new Line(10, 10, 280, 10);
        vbox.getChildren().add(separator);

        details = new Text("Account:\t\t#0\nAcc Type:\tChecking\nBalance:\t\t$0000");
        details.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        vbox.getChildren().add(details);

        return vbox;
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            items.add(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }

        clients = new ComboBox(items);
        clients.setPrefSize(150, 20);
        clients.setPromptText("Click to choose...");

        Button buttonShow = new Button("Show");
        buttonShow.setPrefSize(100, 20);

        Button buttonReport = new Button("Report");
        buttonReport.setPrefSize(100, 20);

        buttonShow.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                
                    int custNo = clients.getItems().indexOf(clients.getValue());
                    String Customerinfo = "";
                    title.setText(clients.getValue().toString());
                    int numberOfAccounts = Bank.getCustomer(custNo).getNumberOfAccounts();
                    for (int accNo = 0; accNo < numberOfAccounts; accNo++) {
                        String accType = Bank.getCustomer(custNo).getAccount(accNo) instanceof CheckingAccount ? "Checking" : "Savings";
                        Customerinfo += "Account:\t\t#" + accNo + "\nAcc Type:\t" + accType + "\nBalance:\t\t$" + Bank.getCustomer(custNo).getAccount(accNo).getBalance() + "\n----------\n";

                    }
                    details.setText(Customerinfo);
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error getting client...");
                    // Header Text: null
                    alert.setHeaderText(null);
                    String details = e.getMessage() != null ? e.getMessage() : "You need to choose a client first!";
                    alert.setContentText("Error details: " + details);
                    alert.showAndWait();
                }
            }
        });

        buttonReport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String reportInfo = "";
                    title.setText("Customers report");
                    for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                        Customer current = Bank.getCustomer(i);
                        reportInfo += "\n" + current.getFirstName() + " " + current.getLastName() + "\n--------------------------------\n";

                        for (int j = 0; j < current.getNumberOfAccounts(); j++) {
                            String accType = current.getAccount(j) instanceof CheckingAccount ? "Checking" : "Savings";
                            reportInfo += "Account:\t\t#" + j + "\nAcc Type:\t" + accType + "\n"
                                    + "Balance:\t\t$"
                                    + current.getAccount(j).getBalance() + "\n\n";
                        }
                        reportInfo += "\n";
                    }
                    reportInfo += "";
                    details.setText(reportInfo);

                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error getting client...");
                    // Header Text: null
                    alert.setHeaderText(null);

                    alert.showAndWait();
                }
            }
        });

        hbox.getChildren().addAll(clients, buttonShow, buttonReport);

        return hbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                    new Stop(0, Color.web("#4977A3")),
                    new Stop(0.5, Color.web("#B0C6DA")),
                    new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        helpIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });

        helpText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }

    private static void loadCustomerData(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    int numAccounts = Integer.parseInt(parts[2]);
                    Bank.addCustomer(firstName, lastName);
                    for (int i = 0; i < numAccounts; i++) {
                        line = reader.readLine();
                        parts = line.split("\t");
                        if (parts[0].equals("S")) {
                            double balance = Double.parseDouble(parts[1]);
                            double interestRate = parts.length >= 3 ? Double.parseDouble(parts[1]) : 0.0;
                            Bank.getCustomer(Bank.getNumberOfCustomers() - 1).addAccount(new SavingsAccount(balance, interestRate));
                        } else if (parts[0].equals("C")) {
                            double balance = Double.parseDouble(parts[1]);
                            double overdraftLimit = parts.length >= 3 ? Double.parseDouble(parts[2]) : 0.0; // Опціональний параметр для CheckingAccount
                            Bank.getCustomer(Bank.getNumberOfCustomers() - 1).addAccount(new CheckingAccount(balance, overdraftLimit));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowAboutInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Just a simple JavaFX demo.\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich");
        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        loadCustomerData("data\\test.dat");

        launch(args);
    }

}

package com.andreas.SelfScannerPOS.main.java.controller;

import com.andreas.SelfScannerPOS.main.java.common.ScreenController;
import com.andreas.SelfScannerPOS.main.java.common.Shared;
import com.andreas.SelfScannerPOS.main.java.common.StageManager;
import com.andreas.SelfScannerPOS.main.java.dao.ProductsDB;
import com.andreas.SelfScannerPOS.main.java.model.Product;
import com.andreas.SelfScannerPOS.main.java.utils.AlertMessage;
import com.andreas.SelfScannerPOS.main.java.utils.Transitions;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;
import java.net.URL;
import java.util.*;

public class ShoppingController implements Initializable {
    @FXML
    private AnchorPane MainPane;
    @FXML
    private CustomTextField FieldSearch;
    @FXML
    private TableView OrderView;
    @FXML
    private TableColumn OrderNumber;
    @FXML
    private TableColumn OrderName;
    @FXML
    private TableColumn OrderQt;
    @FXML
    private TableColumn OrderPrice;
    @FXML
    private TableColumn OrderTotal;
    private ResourceBundle resourceBundle;
    @FXML
    private ScrollPane SP;
    @FXML
    private VBox ShopBox;
    @FXML
    private VBox RightBox;
    @FXML
    private Text textTotal;
    @FXML
    private Text textTVA;
    @FXML
    private Text textSubTotal;
    @FXML
    private Text textDiscount;
    private List<Product> shoppingCart = new ArrayList<Product>();
    private BooleanProperty changedCart = new SimpleBooleanProperty();
    private int discountValue = 0;
    private Node categories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        Shared.updateCart = new SimpleBooleanProperty(false);
        changedCart.bind(Shared.updateCart.isEqualTo(new SimpleBooleanProperty(true)));
        changedCart.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Shared.updateCart.set(false);
                OrderView.refresh();
                updateShoppingReceipt();
            }
        });

        Transitions.fadeInPanel(MainPane);
        FieldSearch.setFocusTraversable(false);
        FieldSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode()== KeyCode.ENTER) {
                    search();
                }
            }
        });

        OrderView.setPlaceholder(new Label(""));

        OrderNumber.prefWidthProperty().bind(OrderView.widthProperty().multiply(0.1));
        OrderName.prefWidthProperty().bind(OrderView.widthProperty().multiply(0.44));
        OrderQt.prefWidthProperty().bind(OrderView.widthProperty().multiply(0.12));
        OrderPrice.prefWidthProperty().bind(OrderView.widthProperty().multiply(0.15));
        OrderTotal.prefWidthProperty().bind(OrderView.widthProperty().multiply(0.19));

        OrderNumber.setCellValueFactory(new PropertyValueFactory<Product, Integer>("index"));
        OrderName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        OrderQt.setCellValueFactory(new PropertyValueFactory<Product, Spinner<Integer>>("quantity"));
        OrderPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        OrderTotal.setCellValueFactory(new PropertyValueFactory<Product, String>("total"));

        OrderNumber.setReorderable(false);
        OrderName.setReorderable(false);
        OrderQt.setReorderable(false);
        OrderPrice.setReorderable(false);
        OrderTotal.setReorderable(false);

        SP.setContent(ShopBox);
        SP.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });
        OrderView.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        categories = ShopBox.getChildren().get(0);
    }

    @FXML
    public void search() {
        if (!FieldSearch.getText().equals("")) {
            if (RightBox.getChildren().size() > 1) RightBox.getChildren().remove(0);
            ShopBox.getChildren().clear();
            ShopBox.getChildren().add(new ImageView(new Image(getClass().getResource("/images/loading.gif").toExternalForm())));
            List<Product> listProducts = new ArrayList<>();

            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    for (Product prod : ProductsDB.getProductsByName(FieldSearch.getText())) {
                        listProducts.add(prod);
                    }
                    return null;
                }
            };
            task.setOnSucceeded(e -> {
                Label backButton = new Label();
                backButton.setGraphic(new ImageView(new Image(getClass().getResource("/images/back.png").toExternalForm())));
                backButton.getStyleClass().add("click");
                backButton.setOnMouseClicked(event -> {
                    RightBox.getChildren().remove(0);
                    ShopBox.getChildren().clear();
                    ShopBox.getChildren().add(categories);
                    FieldSearch.clear();
                });

                FlowPane productsPane = new FlowPane();
                productsPane.setAlignment(Pos.TOP_LEFT);
                productsPane.setVgap(15.0);
                productsPane.setHgap(20.0);

                Task task2 = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        //Button test = new Button(resourceBundle.getString("button_fruits"));
                        for (Product prod : listProducts) {
                            Button btnProduct = new Button(prod.getName() + "\n" + prod.getPrice());
                            btnProduct.setMnemonicParsing(false);
                            btnProduct.getStyleClass().add("cat-button");
                            btnProduct.setPrefWidth(160.0);
                            btnProduct.setMaxWidth(160.0);
                            btnProduct.setPrefHeight(200.0);
                            btnProduct.setMaxHeight(200.0);
                            btnProduct.setWrapText(true);
                            btnProduct.setTextAlignment(TextAlignment.CENTER);
                            btnProduct.setAlignment(Pos.CENTER);
                            btnProduct.setContentDisplay(ContentDisplay.TOP);
                            ImageView graphic = new ImageView(new Image(prod.getPicture()));
                            graphic.setFitHeight(100.0);
                            graphic.setFitWidth(100.0);
                            btnProduct.setGraphic(graphic);
                            btnProduct.setOnAction(e2 -> {
                                OrderView.getItems().clear();
                                updateShoppingCart(prod);
                            });
                            productsPane.getChildren().add(btnProduct);
                        }
                        return null;
                    }
                };
                task2.setOnSucceeded(event -> {
                    RightBox.getChildren().add(0, backButton);
                    ShopBox.getChildren().clear();
                    if (listProducts.size() == 0) {
                        Text textEmptyCategory = new Text(resourceBundle.getString("text_not_found_product"));
                        textEmptyCategory.getStyleClass().add("text-progress");
                        ShopBox.getChildren().add(textEmptyCategory);
                    } else {
                        ShopBox.getChildren().add(productsPane);
                    }
                });
                new Thread(task2).start();
            });
            new Thread(task).start();
        }
    }

    @FXML
    public void cancel_order() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setGraphic(new ImageView(new Image(getClass().getResource("/images/cancelorder.png").toExternalForm())));
        alert.setTitle(resourceBundle.getString("text_cancel_order"));
        alert.setHeaderText(resourceBundle.getString("text_cancel_order"));
        alert.setContentText(resourceBundle.getString("text_cancel_order_content"));
        alert.getButtonTypes().setAll(new ButtonType(resourceBundle.getString("text_cancel_order_yes"), ButtonData.OK_DONE), new ButtonType(resourceBundle.getString("text_cancel_order_no"), ButtonData.CANCEL_CLOSE));
        alert.initOwner(StageManager.getStage());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("confirmation-dialog");
        alert.setDialogPane(dialogPane);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == alert.getButtonTypes().get(0)){
            ScreenController.changeScreen(ScreenController.Screen.WELCOME);
        }

    }

    @FXML
    public void request_assistance() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setGraphic(new ImageView(new Image(getClass().getResource("/images/assistance.png").toExternalForm())));
        alert.setTitle(resourceBundle.getString("text_assistance_help"));
        alert.setHeaderText(resourceBundle.getString("text_assistance_help"));
        alert.initOwner(StageManager.getStage());
        ButtonType done = new ButtonType(resourceBundle.getString("text_assistance_done"), ButtonData.LEFT);
        alert.getButtonTypes().setAll(done);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("assistance-dialog");
        VBox boxProgress = new VBox(25.0);
        boxProgress.setAlignment(Pos.BASELINE_CENTER);
        boxProgress.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
        Text textProgress = new Text(resourceBundle.getString("text_assistance_content"));
        textProgress.getStyleClass().add("text-progress");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progress.setMinWidth(120.0);
        progress.setMinHeight(120.0);
        boxProgress.getChildren().addAll(textProgress, progress);
        dialogPane.setContent(boxProgress);
        alert.setDialogPane(dialogPane);
        alert.showAndWait();
    }

    @FXML
    public void enter_barcode() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initOwner(StageManager.getStage());
        dialog.setGraphic(new ImageView(new Image(getClass().getResource("/images/barcode.png").toExternalForm())));
        dialog.setTitle(resourceBundle.getString("text_enter_barcode"));
        dialog.setHeaderText(resourceBundle.getString("text_enter_barcode"));
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("barcode-dialog");
        dialog.setDialogPane(dialogPane);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(resourceBundle.getString("text_enter_barcode_ok"));
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(resourceBundle.getString("text_enter_barcode_cancel"));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(barcode -> {
            OrderView.getItems().clear();
            OrderView.setPlaceholder(new ImageView(new Image(getClass().getResource("/images/loading.gif").toExternalForm())));
            final Product[] p = new Product[1];
            Task task=new Task() {
                @Override
                protected Object call() throws Exception {
                    p[0] = ProductsDB.getProductByBarcode(barcode);
                    return null;
                }
            };
            task.setOnSucceeded(e -> {
                if (p[0] == null) {
                    if (shoppingCart.size() > 0) {
                        OrderView.setItems(FXCollections.observableArrayList(shoppingCart));
                        updateShoppingReceipt();
                    } else {
                        OrderView.setPlaceholder(new Label(""));
                    }
                    new AlertMessage(resourceBundle.getString("text_error_barcode_title"), resourceBundle.getString("text_error_barcode_content"), Alert.AlertType.WARNING, resourceBundle.getString("text_assistance_done"));
                } else {
                    updateShoppingCart(p[0]);
                }
            });
            new Thread(task).start();

        });
    }

    @FXML
    public void add_bag() {
        OrderView.getItems().clear();
        OrderView.setPlaceholder(new ImageView(new Image(getClass().getResource("/images/loading.gif").toExternalForm())));
        Task task=new Task() {
            @Override
            protected Object call() throws Exception {
                if (ProductsDB.getProductByBarcode(Shared.bagBarcode) != null) updateShoppingCart(ProductsDB.getProductByBarcode(Shared.bagBarcode));
                return null;
            }
        };
        new Thread(task).start();
    }

    private void updateShoppingCart(Product p) {
        Boolean add_product = true;
        if (shoppingCart.size() != 0) {
            for (Product prod : shoppingCart) {
                if (prod.getBarcode().equals(p.getBarcode())) {
                    if (prod.getQuant() < prod.getUnits()) {
                        prod.setQuantity(prod.getQuant()+1);
                    } else {
                        new AlertMessage(resourceBundle.getString("text_error_quantity_title"), resourceBundle.getString("text_error_quantity_content"), Alert.AlertType.WARNING, resourceBundle.getString("text_assistance_done"));
                    }
                    add_product = false;
                    break;
                }
            }
        }

        if (add_product || shoppingCart.size() == 0) {
            p.setIndex(shoppingCart.size()+1);
            p.setQuantity(1);
            shoppingCart.add(p);
        }

        OrderView.setItems(FXCollections.observableArrayList(shoppingCart));
        updateShoppingReceipt();
    }

    public void updateShoppingReceipt() {
        float cartTotal = 0;
        for (Product prod : shoppingCart) {
            cartTotal += 100*Float.parseFloat(prod.getTotal().replace(Shared.currency,""))/100;
        }
        float discount = (100*cartTotal*discountValue)/10000;
        cartTotal = (100*cartTotal - 100*discount)/100;
        float cartTVA = (100*cartTotal*Shared.TVA)/10000;

        textDiscount.setText(Shared.currency + String.format ("%.2f", discount));
        textTotal.setText(Shared.currency + String.format ("%.2f", cartTotal));
        textSubTotal.setText(Shared.currency + String.format ("%.2f", (100*cartTotal - 100*cartTVA + 100*discount)/100));
        textTVA.setText(Shared.currency + String.format ("%.2f", cartTVA));
    }

    @FXML
    public void card_payment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setGraphic(new ImageView(new Image(getClass().getResource("/images/paycard.png").toExternalForm())));
        alert.setTitle(resourceBundle.getString("text_pay_card"));
        alert.setHeaderText(resourceBundle.getString("text_pay_card"));
        alert.initOwner(StageManager.getStage());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("payment-dialog");
        if (shoppingCart.size() == 0) {
            ButtonType done = new ButtonType(resourceBundle.getString("text_assistance_done"), ButtonData.LEFT);
            alert.getButtonTypes().setAll(done);
            VBox boxError = new VBox(10.0);
            boxError.setAlignment(Pos.BASELINE_CENTER);
            boxError.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
            Text textError = new Text(resourceBundle.getString("text_payment_error"));
            textError.getStyleClass().add("text-progress");
            boxError.getChildren().addAll(new ImageView(new Image(getClass().getResource("/images/error.png").toExternalForm())), textError);
            dialogPane.setContent(boxError);
            alert.setDialogPane(dialogPane);
            alert.showAndWait();
        } else {
            alert.getButtonTypes().setAll();
            VBox boxProgress = new VBox(25.0);
            boxProgress.setAlignment(Pos.BASELINE_CENTER);
            boxProgress.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
            Text textProgress = new Text(resourceBundle.getString("text_payment_content"));
            textProgress.getStyleClass().add("text-progress");
            ProgressIndicator progress = new ProgressIndicator();
            progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progress.setMinWidth(120.0);
            progress.setMinHeight(120.0);
            boxProgress.getChildren().addAll(textProgress, progress);
            dialogPane.setContent(boxProgress);
            alert.setDialogPane(dialogPane);
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(event -> {
                VBox boxSuccess = new VBox(10.0);
                boxSuccess.setAlignment(Pos.BASELINE_CENTER);
                boxSuccess.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
                Text textSuccess = new Text(resourceBundle.getString("text_payment_success"));
                textSuccess.getStyleClass().add("text-progress");
                boxSuccess.getChildren().addAll(new ImageView(new Image(getClass().getResource("/images/success.png").toExternalForm())), textSuccess);
                dialogPane.setContent(boxSuccess);
                PauseTransition delay2 = new PauseTransition(Duration.seconds(5));
                delay2.setOnFinished(e -> {
                    alert.setResult(ButtonType.CANCEL);
                    alert.hide();
                    PauseTransition delay3 = new PauseTransition(Duration.seconds(1));
                    delay3.setOnFinished(e2 -> ScreenController.changeScreen(ScreenController.Screen.WELCOME));
                    delay3.play();
                });
                delay2.play();
            });
            delay.play();
        }
    }

    @FXML
    public void cash_payment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setGraphic(new ImageView(new Image(getClass().getResource("/images/paycash.png").toExternalForm())));
        alert.setTitle(resourceBundle.getString("text_pay_cash"));
        alert.setHeaderText(resourceBundle.getString("text_pay_cash"));
        alert.initOwner(StageManager.getStage());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("payment-dialog");
        if (shoppingCart.size() == 0) {
            ButtonType done = new ButtonType(resourceBundle.getString("text_assistance_done"), ButtonData.LEFT);
            alert.getButtonTypes().setAll(done);
            VBox boxError = new VBox(10.0);
            boxError.setAlignment(Pos.BASELINE_CENTER);
            boxError.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
            Text textError = new Text(resourceBundle.getString("text_payment_error"));
            textError.getStyleClass().add("text-progress");
            boxError.getChildren().addAll(new ImageView(new Image(getClass().getResource("/images/error.png").toExternalForm())), textError);
            dialogPane.setContent(boxError);
            alert.setDialogPane(dialogPane);
            alert.showAndWait();
        } else {
            alert.getButtonTypes().setAll();
            VBox boxProgress = new VBox(25.0);
            boxProgress.setAlignment(Pos.BASELINE_CENTER);
            boxProgress.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
            Text textProgress = new Text(resourceBundle.getString("text_payment_content"));
            textProgress.getStyleClass().add("text-progress");
            ProgressIndicator progress = new ProgressIndicator();
            progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progress.setMinWidth(120.0);
            progress.setMinHeight(120.0);
            boxProgress.getChildren().addAll(textProgress, progress);
            dialogPane.setContent(boxProgress);
            alert.setDialogPane(dialogPane);
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(event -> {
                VBox boxSuccess = new VBox(10.0);
                boxSuccess.setAlignment(Pos.BASELINE_CENTER);
                boxSuccess.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
                Text textSuccess = new Text(resourceBundle.getString("text_payment_success"));
                textSuccess.getStyleClass().add("text-progress");
                boxSuccess.getChildren().addAll(new ImageView(new Image(getClass().getResource("/images/success.png").toExternalForm())), textSuccess);
                dialogPane.setContent(boxSuccess);
                PauseTransition delay2 = new PauseTransition(Duration.seconds(5));
                delay2.setOnFinished(e -> {
                    alert.setResult(ButtonType.CANCEL);
                    alert.hide();
                    PauseTransition delay3 = new PauseTransition(Duration.seconds(1));
                    delay3.setOnFinished(e2 -> ScreenController.changeScreen(ScreenController.Screen.WELCOME));
                    delay3.play();
                });
                delay2.play();
            });
            delay.play();
        }
    }

    @FXML
    public void enter_discount_code() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initOwner(StageManager.getStage());
        dialog.setGraphic(new ImageView(new Image(getClass().getResource("/images/discount.png").toExternalForm())));
        dialog.setTitle(resourceBundle.getString("text_enter_discount_code"));
        dialog.setHeaderText(resourceBundle.getString("text_enter_discount_code"));
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("barcode-dialog");
        dialog.setDialogPane(dialogPane);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(resourceBundle.getString("text_enter_barcode_ok"));
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(resourceBundle.getString("text_enter_barcode_cancel"));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(discount -> {
            if (Shared.discountCodes.containsKey(discount.toLowerCase())) {
                discountValue = Shared.discountCodes.get(discount.toLowerCase());
                updateShoppingReceipt();
            } else {
                new AlertMessage(resourceBundle.getString("text_error_discount_code_title"), resourceBundle.getString("text_error_dicount_code_content"), Alert.AlertType.WARNING, resourceBundle.getString("text_assistance_done"));
            }

        });
    }

    public void showProductsByCategory(String category) {
        ShopBox.getChildren().clear();
        ShopBox.getChildren().add(new ImageView(new Image(getClass().getResource("/images/loading.gif").toExternalForm())));
        List<Product> listProducts = new ArrayList<>();

        Task task=new Task() {
            @Override
            protected Object call() throws Exception {
                for (Product prod : ProductsDB.getProductsByCategory(category)) {
                    listProducts.add(prod);
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            Label backButton = new Label();
            backButton.setGraphic(new ImageView(new Image(getClass().getResource("/images/back.png").toExternalForm())));
            backButton.getStyleClass().add("click");
            backButton.setOnMouseClicked(event -> {
                RightBox.getChildren().remove(0);
                ShopBox.getChildren().clear();
                ShopBox.getChildren().add(categories);
            });

            FlowPane productsPane = new FlowPane();
            productsPane.setAlignment(Pos.TOP_LEFT);
            productsPane.setVgap(15.0);
            productsPane.setHgap(20.0);

            Task task2=new Task() {
                @Override
                protected Object call() throws Exception {
                    //Button test = new Button(resourceBundle.getString("button_fruits"));
                    for (Product prod : listProducts) {
                        Button btnProduct = new Button(prod.getName() + "\n" + prod.getPrice());
                        btnProduct.setMnemonicParsing(false);
                        btnProduct.getStyleClass().add("cat-button");
                        btnProduct.setPrefWidth(160.0);
                        btnProduct.setMaxWidth(160.0);
                        btnProduct.setPrefHeight(200.0);
                        btnProduct.setMaxHeight(200.0);
                        btnProduct.setWrapText(true);
                        btnProduct.setTextAlignment(TextAlignment.CENTER);
                        btnProduct.setAlignment(Pos.CENTER);
                        btnProduct.setContentDisplay(ContentDisplay.TOP);
                        ImageView graphic = new ImageView(new Image(prod.getPicture()));
                        graphic.setFitHeight(100.0);
                        graphic.setFitWidth(100.0);
                        btnProduct.setGraphic(graphic);
                        btnProduct.setOnAction(e2-> {
                            OrderView.getItems().clear();
                            updateShoppingCart(prod);
                        });
                        productsPane.getChildren().add(btnProduct);
                    }
                    return null;
                }
            };
            task2.setOnSucceeded(event -> {
                RightBox.getChildren().add(0, backButton);
                ShopBox.getChildren().clear();
                if (listProducts.size() == 0) {
                    Text textEmptyCategory = new Text(resourceBundle.getString("text_empty_category"));
                    textEmptyCategory.getStyleClass().add("text-progress");
                    ShopBox.getChildren().add(textEmptyCategory);
                } else {
                    ShopBox.getChildren().add(productsPane);
                }
            });
            new Thread(task2).start();
        });
        new Thread(task).start();
    }

    @FXML
    public void showFruits() {
        showProductsByCategory("fruits");
    }

    @FXML
    public void showVegetables() {
        showProductsByCategory("vegetables");
    }

    @FXML
    public void showBreakfast() {
        showProductsByCategory("breakfast");
    }

    @FXML
    public void showMeat() {
        showProductsByCategory("meat");
    }

    @FXML
    public void showSeafood() {
        showProductsByCategory("seafood");
    }

    @FXML
    public void showFrozen() {
        showProductsByCategory("frozen");
    }

    @FXML
    public void showBaby() {
        showProductsByCategory("baby");
    }

    @FXML
    public void showPet() {
        showProductsByCategory("pet");
    }

    @FXML
    public void showBaking() {
        showProductsByCategory("baking");
    }

    @FXML
    public void showSnacks() {
        showProductsByCategory("snacks");
    }

    @FXML
    public void showBakery() {
        showProductsByCategory("bakery");
    }

    @FXML
    public void showPastaRice() {
        showProductsByCategory("pasta&rice");
    }

    @FXML
    public void showCansJars() {
        showProductsByCategory("cans&jars");
    }

    @FXML
    public void showRefrigerated() {
        showProductsByCategory("refrigerated");
    }

    @FXML
    public void showSeasoning() {
        showProductsByCategory("seasoning");
    }

    @FXML
    public void showSaucesCondiments() {
        showProductsByCategory("sauces&condiments");
    }

    @FXML
    public void showDrinks() {
        showProductsByCategory("drinks");
    }

    @FXML
    public void showPaperProducts() {
        showProductsByCategory("paper-products");
    }

    @FXML
    public void showCleaning() {
        showProductsByCategory("cleaning");
    }

    @FXML
    public void showPersonalCare() {
        showProductsByCategory("personal-care");
    }

    @FXML
    public void showMiscItems() {
        showProductsByCategory("misc");
    }
}

package com.project.qler;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

public class CreateListController {
    @FXML
    private VBox btnAdd;
    @FXML
    private TextField listNameField;
    @FXML
    private VBox btnCreate;
    @FXML
    private VBox createField;
    @FXML
    private HBox listContainer;

    @FXML
    protected void onBtnAddClicked() {
        createField.setVisible(!createField.isVisible());
    }
    @FXML
    protected void onBtnCreateClicked() {
        if (listNameField.getText().isEmpty()){
            System.out.println("You need to enter name list to create a new list");
        } else {
            VBox listPane = new VBox();
            listPane.setId("listPane" + listNameField.getText().toUpperCase());
            listPane.setPrefWidth(250);

            VBox listComponent = new VBox();
            listComponent.setId("listComponent" + listNameField.getText().toUpperCase());
            listComponent.setPrefWidth(250);
            listComponent.setStyle("-fx-background-color: ebecf0; -fx-background-radius: 5;");

            FlowPane flowPane = new FlowPane();
            flowPane.setAlignment(Pos.CENTER);
            Label listName = new Label(listNameField.getText());
            listName.setAlignment(Pos.CENTER);
            listName.setId("listName");
            listName.setTextFill(Color.valueOf("#222223"));
            Font font = new Font("Roboto", 18);
            listName.setFont(font);
            flowPane.getChildren().add(listName);
            flowPane.setPadding(new Insets(8, 16, 8, 16));
            listComponent.getChildren().add(flowPane);

            VBox cardContainer = new VBox();
            cardContainer.setId("cardContainer");
            cardContainer.setAlignment(Pos.CENTER_LEFT);
            cardContainer.setPadding(new Insets(4, 4, 8, 4));

            VBox cardList = new VBox();
            cardList.setPrefHeight(30);
            cardList.setPrefWidth(240);
            cardList.setId("cardList");

            VBox tempCardComponent = new VBox();
            cardList.getChildren().add(tempCardComponent);

            for (Node node : cardList.getChildren()) {
                if (node instanceof VBox) {
                    enableCardDragging((VBox) node, cardList);
                }
            }

            VBox btnAddNewCard = new VBox();
            btnAddNewCard.setStyle("-fx-background-color: #a0a0a3; -fx-background-radius: 5");
            btnAddNewCard.setAlignment(Pos.CENTER);
            btnAddNewCard.setPadding(new Insets(8));
            Label addNewCardLabel = new Label("+ Add new cards");
            addNewCardLabel.setFont(new Font("Roboto Bold", 14));
            addNewCardLabel.setTextFill(Color.WHITE);
            addNewCardLabel.setId("addNewCardLabel");
            btnAddNewCard.getChildren().add(addNewCardLabel);

            VBox addNewCardField = new VBox();
            addNewCardField.setId("addNewCardField");
            addNewCardField.setStyle("-fx-background-color: white;");
            addNewCardField.setPadding(new Insets(8));
            addNewCardField.setSpacing(8);
            Label cardTitle = new Label("Card title:");
            cardTitle.setFont(new Font("Roboto", 14));
            TextField cardNameField = new TextField();
            Label desTitle = new Label("Description:");
            desTitle.setFont(new Font("Roboto", 14));
            TextArea desTextField = new TextArea();
            Label memberTitle = new Label("Members:");
            memberTitle.setFont(new Font("Roboto", 14));
            CheckComboBox<String> checkComboBox = new CheckComboBox<>();
            checkComboBox.getItems().addAll("Kittipong Prasompong", "Ikkyuu");
            StringBuilder sb = new StringBuilder();
            checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
                sb.setLength(0);
                for (String item : checkComboBox.getCheckModel().getCheckedItems()) {
                    sb.append(item).append(", ");
                }if (sb.length() > 0) {
                    sb.delete(sb.length() - 2, sb.length()); // remove the last comma and space separator
                }
                System.out.println("Checked items:\n" + sb.toString());
            });
            Label storyPoint = new Label("Story points:");
            storyPoint.setFont(new Font("Roboto", 14));
            Slider slider = new Slider(1, 13, 1);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            slider.setSnapToTicks(true);

            slider.setLabelFormatter(new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    int intValue = value.intValue();
                    switch (intValue) {
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 8:
                        case 13:
                            return String.valueOf(intValue);
                        default:
                            return "";
                    }
                }

                @Override
                public Double fromString(String string) {
                    return Double.valueOf(string);
                }
            });

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 1 && newValue.intValue() < 2) {
                    slider.setValue(1);
                } else if (newValue.intValue() >= 2 && newValue.intValue() < 3) {
                    slider.setValue(2);
                } else if (newValue.intValue() >= 3 && newValue.intValue() < 5) {
                    slider.setValue(3);
                } else if (newValue.intValue() >= 5 && newValue.intValue() < 8) {
                    slider.setValue(5);
                } else if (newValue.intValue() >= 8 && newValue.intValue() < 13) {
                    slider.setValue(8);
                } else {
                    slider.setValue(13);
                }
            });

            Button btnAdd = new Button("Add");
            btnAdd.setOnMouseClicked(e -> {
                VBox newCardComponent = createCardComponent(cardNameField, desTextField, cardList, sb, slider);
                newCardComponent.setId(cardNameField.getText());

                cardList.getChildren().add(newCardComponent); // Add the new card component to the cardList

                cardContainer.getChildren().removeAll(addNewCardField);
                cardList.getChildren().remove(tempCardComponent);
                for (Node node : addNewCardField.getChildren()){
                    if (node instanceof TextField){
                        ((TextField) node).setText("");
                    }
                    if (node instanceof TextArea){
                        ((TextArea) node).setText("");
                    }
                    if(node instanceof CheckComboBox){
                        ((CheckComboBox<?>) node).getCheckModel().clearChecks();
                    }
                    if(node instanceof Slider){
                        ((Slider) node).setValue(1);
                    }
                }
            } );
            addNewCardField.getChildren().addAll(cardTitle,cardNameField,desTitle, desTextField, memberTitle, checkComboBox, storyPoint, slider, btnAdd);

            btnAddNewCard.setOnMouseClicked(event -> {
                if (!cardContainer.getChildren().contains(addNewCardField)) {
                    cardContainer.getChildren().add(addNewCardField);
                } else {
                    cardContainer.getChildren().remove(addNewCardField);
                }
            });
            cardContainer.getChildren().addAll(cardList, btnAddNewCard);

            listComponent.getChildren().add(cardContainer);
            listPane.getChildren().add(listComponent);

            listContainer.getChildren().add(listPane);
            createField.setVisible(!createField.isVisible());
            listNameField.setText("");
            // Enable drag and drop for the new listPane
            enableListPaneDragging(listPane);

            // Enable drop targets for all listPanes in the listContainer
            for (Node node : listContainer.getChildren()) {
                if (node instanceof VBox) {
                    enableDropTarget((VBox) node, listContainer);
                }
            }
        }
    }

    private void enableCardDragging(VBox cardComponent, VBox cardList) {
        cardComponent.setOnDragDetected(event -> {
            Dragboard db = cardComponent.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cardComponent.getId()); // add card ID to dragboard
            db.setContent(content);
            db.setDragView(cardComponent.snapshot(null, null));
            event.consume();
        });

        cardList.setOnDragOver(event -> {
            if (event.getGestureSource() != cardList && event.getDragboard().hasString()) {
                // Only accept the transfer mode if the target card list doesn't already contain the dragged card component
                String cardId = event.getDragboard().getString();
                if (!cardList.getChildren().stream().anyMatch(node -> node instanceof VBox && node.getId() != null && node.getId().equals(cardId))) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            event.consume();
        });


        cardList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                VBox sourceCardList = (VBox) ((VBox) event.getGestureSource()).getParent();
                VBox sourceListContainer = (VBox) sourceCardList.getParent().getParent();
                VBox targetListContainer = (VBox) cardList.getParent().getParent();
                if (sourceListContainer != targetListContainer) {
                    // Remove the source card component from the source card list
                    sourceCardList.getChildren().remove((VBox) event.getGestureSource());
                }
                // Add the source card component to the target card list
                cardList.getChildren().add((VBox) event.getGestureSource());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private VBox createCardComponent(TextField cardNameField, TextArea desTextField, VBox cardList, StringBuilder stringBuilder, Slider slider) {
        VBox newCardComponent = new VBox();
        newCardComponent.setId("cardComponent");
        newCardComponent.setMinWidth(0);
        newCardComponent.setPrefWidth(240);
        newCardComponent.setPrefHeight(Double.MAX_VALUE); // allow TextArea to take up full height
        newCardComponent.setSpacing(8);
        newCardComponent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        // Add an EventHandler to the newCardComponent that listens for triple-clicks and removes the card component from the cardList when triggered
        newCardComponent.setOnMouseClicked(event -> {
            if (event.getClickCount() == 3) {
                cardList.getChildren().remove(newCardComponent);
            }
        });

        FlowPane member = new FlowPane();
        member.setId("memberContainer");

        VBox info = new VBox();
        info.setPrefWidth(240);
        info.setPrefHeight(Region.USE_COMPUTED_SIZE);
        info.setFillWidth(true); // allow VBox to expand
        info.setId("info");

        Label description = new Label();
        description.setWrapText(true);
        description.setFont(new Font("Roboto", 14));
        description.setStyle("-fx-text-fill: #a0a0a3; -fx-background-color: transparent; -fx-border-color: transparent;");
        description.setText(desTextField.getText());
        description.setMaxWidth(info.getPrefWidth());
        description.setMinHeight(Label.USE_PREF_SIZE);

        Label cardName = new Label(cardNameField.getText() + "\n");
        cardName.setWrapText(true);
        cardName.setFont(new Font("Roboto Bold", 18));
        cardName.setId(cardNameField.getText());
        cardName.setMaxWidth(info.getPrefWidth());
        cardName.setMinHeight(Label.USE_PREF_SIZE);

        info.getChildren().addAll(cardName, description);

        Label listMember = new Label("Members: " + stringBuilder);
        listMember.setFont(new Font("Roboto", 12));
        listMember.setTextFill(Color.valueOf("#a0a0a3"));
        listMember.setAlignment(Pos.CENTER);
        listMember.setId("listMember");

        member.getChildren().add(listMember);

        HBox points = new HBox();
        points.setPrefHeight(10);
        points.setMinWidth(50);
        points.setMaxWidth(50);
        points.setAlignment(Pos.CENTER);
        String pointColor = slider.getValue() == 1.0 ? "#2196f3" :
                slider.getValue() == 2.0 ? "#61bd4f" :
                        slider.getValue() == 3.0 ? "#ffbe35" :
                                slider.getValue() == 5.0 ? "#d09645" :
                                        slider.getValue() == 8.0 ? "#d09645" :
                                                "#c75450" ;
        points.setStyle("-fx-background-radius: 5; -fx-background-color: " + pointColor + ";");
        points.setId("points");

        points.setPadding(new Insets(4));
        String sliderValue = String.valueOf((int) slider.getValue());
        Label pointText = new Label(sliderValue + " PTS");
        pointText.setFont(new Font("Roboto Bold", 12));
        pointText.setAlignment(Pos.CENTER);
        pointText.setTextFill(Color.WHITE);
        pointText.setStyle("-fx-font-weight: BOLD");
        points.getChildren().add(pointText);

        newCardComponent.getChildren().addAll( info, member, points);
        newCardComponent.setPadding(new Insets(8));
        VBox.setMargin(newCardComponent, new Insets(0,0,8,0));
        enableCardDragging(newCardComponent, cardList);

        return newCardComponent;
    }

    private void handleDragAndDrop(VBox dragNode, VBox dropNode, HBox parent) {
        int dragIndex = parent.getChildren().indexOf(dragNode);
        int dropIndex = parent.getChildren().indexOf(dropNode);

        if (dragIndex != -1 && dropIndex != -1 && dragIndex != dropIndex) {
            parent.getChildren().remove(dragIndex);
            parent.getChildren().add(dropIndex < dragIndex ? dropIndex : dropIndex - 1, dragNode);
        }
    }
    private void enableListPaneDragging(VBox listPane) {
        listPane.setOnDragDetected(event -> {
            Dragboard db = listPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(listPane.getId());
            db.setContent(content);
            event.consume();
        });
    }
    private void enableDropTarget(VBox listPane, HBox listContainer) {
        listPane.setOnDragOver(event -> {
            if (event.getGestureSource() != listPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        listPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                VBox dragNode = (VBox) listContainer.lookup("#" + db.getString());
                handleDragAndDrop(dragNode, listPane, listContainer);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }


}

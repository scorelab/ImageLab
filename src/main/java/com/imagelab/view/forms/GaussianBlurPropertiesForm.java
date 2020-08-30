package com.imagelab.view.forms;

import com.imagelab.operator.imagebluring.ApplyGaussianBlurEffect;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Applying gaussian blur effects operation related
 * UI properties form.
 */
public class GaussianBlurPropertiesForm extends AbstractPropertiesForm {
    /**
     * Builds the GaussianBlurPropertiesForm.
     *
     * @param operator - operator which requires this properties form.
     */
    public GaussianBlurPropertiesForm(ApplyGaussianBlurEffect operator) {
        setPrefSize(224.0, 523.0);
        //Simple blur tittle container.
        PropertiesFormTitleContainer gaussianBlurTitleContainer;
        gaussianBlurTitleContainer = new PropertiesFormTitleContainer("Gaussian Blur Properties");

        //Size - width.
        VBox widthSizeContainer = new VBox();
        widthSizeContainer.setPrefWidth(205.0);
        widthSizeContainer.setSpacing(10);
        Label lblWidthSize = new Label("Size: width");
        TextField widthSizeTextField = new TextField(String.valueOf(45.0));
        widthSizeTextField.setPrefSize(205.0, 27.0);
        Label lblErrWidth = new Label("Error");
        lblErrWidth.setTextFill(Color.web("#f20028"));
        lblErrWidth.setVisible(false);
        //Listener to capture text change.
        widthSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            double newVal;
            if ("".equals(newValue)) {
                newVal = 0.0;
                lblErrWidth.setVisible(true);
                lblErrWidth.setText("Enter an odd value");
            } else {
                newVal = Double.parseDouble(newValue);
                lblErrWidth.setVisible(false);
                if (newVal % 2 == 1) {
                    operator.setWidthSize(newVal);
                } else {
                    lblErrWidth.setVisible(true);
                    lblErrWidth.setText("Enter an odd value");
                }
            }
        });
        widthSizeContainer.getChildren().addAll(lblWidthSize, widthSizeTextField, lblErrWidth);

        //Size - height.
        VBox heightSizeContainer = new VBox();
        heightSizeContainer.setPrefWidth(205.0);
        heightSizeContainer.setSpacing(10);
        Label lblHeightSize = new Label("Size height");
        TextField heightSizeTextField = new TextField(String.valueOf(45.0));
        heightSizeTextField.setPrefSize(205.0, 27.0);
        Label lblErrHeight = new Label("Error");
        lblErrHeight.setTextFill(Color.web("#f20028"));
        lblErrHeight.setVisible(false);
        //Listener to capture text change.
        heightSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            double newVal;
            if ("".equals(newValue)) {
                newVal = 0.0;
                lblErrHeight.setVisible(true);
                lblErrHeight.setText("Enter an odd value");
            } else {
                newVal = Double.parseDouble(newValue);
                lblErrHeight.setVisible(false);
                if (newVal % 2 == 1) {
                    operator.setHeightSize(newVal);
                } else {
                    lblErrHeight.setVisible(true);
                    lblErrHeight.setText("Enter an odd value");
                }
            }
        });
        heightSizeContainer.getChildren().addAll(lblHeightSize, heightSizeTextField, lblErrHeight);

        VBox gaussianBlurPropertiesContainer = new VBox();
        gaussianBlurPropertiesContainer.setPrefSize(205, 47);
        gaussianBlurPropertiesContainer.setSpacing(20);
        gaussianBlurPropertiesContainer.setLayoutX(14);
        gaussianBlurPropertiesContainer.setLayoutY(14);
        gaussianBlurPropertiesContainer.getChildren().addAll(
                gaussianBlurTitleContainer,
                widthSizeContainer,
                heightSizeContainer
        );
        getChildren().addAll(gaussianBlurPropertiesContainer);
    }
}

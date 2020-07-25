package com.imagelab.components;

import com.imagelab.components.events.OnUIElementCloneCreated;
import com.imagelab.components.events.OnUIElementDragDone;
import com.imagelab.operators.RotateImage;
import com.imagelab.operators.WriteImage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import static com.imagelab.utils.Constants.ANY_NODE;
import static javafx.scene.input.TransferMode.MOVE;

/**
 * Class which builds the Read image operation
 * related UI element
 */
public class WriteImageOpUIElement extends OperatorUIElement<Button, AnchorPane> implements Draggable, Cloneable {

    private final ScrollPane uiElementPropertiesPane;

    public WriteImageOpUIElement(OnUIElementCloneCreated onCloneCreated,
                                 OnUIElementDragDone onDragDone,
                                 ScrollPane uiElementPropertiesPane
    ) {
        super(
                new WriteImage(), // To invoke openCV related logic
                WriteImage.class.getCanonicalName(),
                WriteImage.class.getSimpleName(),
                onCloneCreated,
                onDragDone,
                "writeImage",
                100d,
                60d,
                true,
                true,
                false
        );
        this.uiElementPropertiesPane = uiElementPropertiesPane;
    }


    /**
     * Overridden method from the draggable interface to
     * detect if the user is dragging and UI element
     *
     * @param event - Mouse event at the time of element being dragged
     */
    @Override
    public void dragDetected(MouseEvent event) {

        // create new clone
        assert getOnCloneCreated() != null;
        try {
            if (!isCloneable()) {
                getOnCloneCreated().accept(this);
            } else {
                getOnCloneCreated().accept(this.clone());
            }
        } catch (CloneNotSupportedException e) {
            System.err.println("operator does not support dragging / cloning");
        }

        Dragboard dragboard = getNode().startDragAndDrop(MOVE);
        dragboard.setDragView(getNode().snapshot(null, null));
        ClipboardContent content = new ClipboardContent();
        content.put(ANY_NODE, "operation");
        dragboard.setContent(content);
        event.consume();

    }

    /**
     * Custom clone method to create a clone from the
     * dragged UI element.
     *
     * @return - a cloned ReadImageOpUIElement
     * @throws CloneNotSupportedException x
     */
    public OperatorUIElement<Button, AnchorPane> clone() throws CloneNotSupportedException {

        super.clone();

        final OperatorUIElement<Button, AnchorPane> copy = new WriteImageOpUIElement(
                this.getOnCloneCreated(),
                this.getOnDragDone(),
                this.uiElementPropertiesPane
        );
        copy.setCloneable(false);
        copy.setPreviewOnly(false);
        copy.setAddedToOperatorsStack(false);
        copy.buildNode();
        copy.buildForm();

        return copy;

    }

    /**
     * Method to be triggered when user clicks on a UI element
     * in the build pane.
     * <p>
     * Usage - this can be used to populate the side pane when needed
     */
    public void onClicked() {
        this.uiElementPropertiesPane.setContent(this.getForm());
        System.out.println("now this element is enabled");
    }

    /**
     * Overridden method which builds an UI element.
     */
    @Override
    public void buildNode() {

        final Button button = new Button();

        button.setId(getStylingId());
        button.setText(super.getOperatorName());
        button.prefHeight(super.getHeight());
        button.setPrefWidth(super.getWidth());
        button.setOnDragDetected(this::dragDetected);

        // source

        button.setOnDragDone((event) -> {
            if (event.isAccepted()) {
                getOnDragDone().accept(this);
            } else {
                getOnDragDone().accept(null);
            }
        });
        button.setOnMouseClicked((event) -> {
            if (!isPreviewOnly()) {
                System.out.println("Clicked");
                this.onClicked();
            }
        });

        setNode(button);

    }

    @Override
    public void buildForm() {
        setForm(new OperatorPropertiesForm());
    }

    /**
     * Operator's Property form. It can be in the form of a pane, vbox, anything
     */
    private class OperatorPropertiesForm extends AnchorPane {

        private OperatorPropertiesForm() {

            // When initially creating the form you can populate the form with
            // default values from the this operator's model.

            WriteImage operator = (WriteImage) WriteImageOpUIElement.this.getOperator();

            setPrefSize(523.0, 197.0);

            Label label = new Label("URL to save");

            TextField field = new TextField(operator.getUrl());
            field.setPrefSize(169, 27);
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                newValue = "imageFile/main/resources/com/imagelab/images/scorelabLogo1.jpg";
                operator.setUrl(newValue);
            });

            VBox box = new VBox();
            box.setSpacing(10);
            box.setLayoutX(14);
            box.setLayoutY(14);
            box.setPrefSize(170, 47);
            box.getChildren().addAll(label, field);

            getChildren().addAll(box);

        }

    }
}

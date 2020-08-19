package com.imagelab;

import com.imagelab.components.OperatorUIElement;
import com.imagelab.components.ReadImageOpUIElement;
import com.imagelab.components.RotateImageOpUIElement;
import com.imagelab.components.WriteImageOpUIElement;
import com.imagelab.components.events.OnUIElementCloneCreated;
import com.imagelab.components.events.OnUIElementDragDone;
import com.imagelab.operators.OpenCVOperator;
import com.imagelab.operators.ReadImage;
import com.imagelab.utils.Utilities;
import com.imagelab.views.ProcessedImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.opencv.core.Mat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

import static com.imagelab.utils.Constants.ANY_NODE;

/**
 * The controller class of the dashboard
 */
public class DashboardController implements Initializable {

    private final Stack<OperatorUIElement<Node, Node, Node>> appliedOperators;
    @FXML
    private Pane playground;
    @FXML
    private VBox operatorsContainer;
    @FXML
    private AnchorPane previewPane;


    @FXML
    private ScrollPane uiElementPropertiesPane;

    @FXML
    private ScrollPane informationScrollPane;

    private OperatorUIElement<Node, Node, Node> curApplyingOpUIElement;

    //To capture mouse position of the user
    private double dropX, dropY;

    public DashboardController() {
        this.appliedOperators = new Stack<>();
    }

    @FXML
    public void onExecuteClicked(ActionEvent event) throws IOException {

        Mat image = null;

        for (OperatorUIElement<Node, Node, Node> op : appliedOperators) {
            image = op.getOperator().compute(image);
        }

        //Displaying the processed image in the preview pane
        WritableImage writableImage = Utilities.loadImage(image);
        ProcessedImageView processedImage = new ProcessedImageView(writableImage);
        previewPane.getChildren().addAll(processedImage);
    }

    /**
     * To handle when users initial drag event.
     *
     * @param event Triggered Drag event.
     *              When user tried to drag something and
     *              move over to another area
     *              this function will be invoked.
     */
    @FXML
    private void handleDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasContent(ANY_NODE)) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        dropX = event.getX();
        dropY = event.getY();
    }

    /**
     * To handle when users initial drag event.
     *
     * @param event Triggered Drop event.
     *              When user dropped the dragged
     *              element this function will
     *              trigger.
     */
    @FXML
    private void handleDrop(DragEvent event) {

        // if this was the initial move then no need to validate. just move on to the next step.
        if (appliedOperators.size() == 0 && curApplyingOpUIElement.getOperator() instanceof ReadImage) {
            proceedToMoveOperator(event);
        } else {
            if (appliedOperators.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("Invalid initial operator!");
                alert.showAndWait();
                return;
            }
            OpenCVOperator operator = appliedOperators.peek().getOperator();
            boolean isValid = curApplyingOpUIElement.getOperator().validate(operator);
            if (!isValid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("You are trying to apply an invalid operator on top of " + curApplyingOpUIElement.getOperatorName());
                alert.showAndWait();
                System.err.println("cannot drag this element on top of : " + curApplyingOpUIElement.getOperatorName());
                return;
            }
            proceedToMoveOperator(event);
        }

    }

    private void proceedToMoveOperator(DragEvent event) {

        assert curApplyingOpUIElement != null : "currentlyApplyingOperator cannot be null here...";

        double relocateX = dropX - (curApplyingOpUIElement.getWidth() / 2);
        double relocateY = dropY - (curApplyingOpUIElement.getHeight() / 4);

        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasContent(ANY_NODE)) {
            if (playground.getChildren().contains(curApplyingOpUIElement.getNode())) {
                curApplyingOpUIElement.getNode().relocate(relocateX, relocateY);
            } else {
                curApplyingOpUIElement.getNode().setLayoutX(relocateX);
                curApplyingOpUIElement.getNode().setLayoutY(relocateY);
                playground.getChildren().add(curApplyingOpUIElement.getNode());
            }
            if (!this.curApplyingOpUIElement.isAddedToOperatorsStack()) {
                this.appliedOperators.push(curApplyingOpUIElement);
                this.curApplyingOpUIElement.setAddedToOperatorsStack(true);
                System.out.println(curApplyingOpUIElement.getOperatorName() + " has been added to the operation stack");
            } else {
                System.out.println("This operator is already in the queue");
            }

            event.setDropCompleted(true);
            event.consume();
        }
        System.out.println("Drop detected: " + curApplyingOpUIElement.getOperatorName() + "\n");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        final OnUIElementCloneCreated anyCloneCreated = (uiElement) -> {
            //noinspection unchecked
            this.curApplyingOpUIElement = ((OperatorUIElement<Node, Node, Node>) uiElement);
        };

        final OnUIElementDragDone anyElementDragDone = (opUiEl) -> {
            if (opUiEl == null) {
                this.uiElementPropertiesPane.setContent(null);
                this.informationScrollPane.setContent(null);
            } else {
                this.uiElementPropertiesPane.setContent(opUiEl.getForm());
                this.informationScrollPane.setContent(opUiEl.getInformation());
            }
            this.curApplyingOpUIElement = null;
        };


        operatorsContainer.setSpacing(15);

        // Create the Operator UI Elements

        // Initiating read image operation related UI element
        ReadImageOpUIElement readImageOpUIElement = new ReadImageOpUIElement(
                anyCloneCreated,
                anyElementDragDone,
                uiElementPropertiesPane,
                informationScrollPane
        );

        RotateImageOpUIElement rotateImageOpUIElement = new RotateImageOpUIElement(
                anyCloneCreated,
                anyElementDragDone,
                uiElementPropertiesPane,
                informationScrollPane
        );

        WriteImageOpUIElement writeImageOpUIElement = new WriteImageOpUIElement(
                anyCloneCreated,
                anyElementDragDone,
                uiElementPropertiesPane,
                informationScrollPane
        );
        readImageOpUIElement.buildNode();
        rotateImageOpUIElement.buildNode();
        writeImageOpUIElement.buildNode();

        // Populating or adding created UI elements to left operators panel
        operatorsContainer.getChildren().addAll(
                readImageOpUIElement.getNode(),
                rotateImageOpUIElement.getNode(),
                writeImageOpUIElement.getNode()
        );

    }

}

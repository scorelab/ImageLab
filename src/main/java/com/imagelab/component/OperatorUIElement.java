package com.imagelab.component;

import com.imagelab.component.event.OnUIElementCloneCreated;
import com.imagelab.component.event.OnUIElementDragDone;
import com.imagelab.operator.OpenCVOperator;
import com.imagelab.util.Constants;
import com.imagelab.view.AbstractInformationUI;
import com.imagelab.view.forms.AbstractPropertiesFormUI;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;

import static com.imagelab.util.Constants.ANY_NODE;
import static javafx.scene.input.TransferMode.MOVE;

/**
 * This is class is the base of operator UI elements.
 */
public class OperatorUIElement {
    // Width of the OperatorUIElement.
    public static final double WIDTH = Constants.OPERATOR_UI_ELEMENT.WIDTH;
    // Height of the OperatorUIElement.
    public static final double HEIGHT = Constants.OPERATOR_UI_ELEMENT.HEIGHT;

    // Events related:
    // Must be initialized before creating any instances of this class.
    public static OnUIElementCloneCreated onCloneCreated;
    public static OnUIElementDragDone onDragDone;

    // Passed from parent
    public static ScrollPane propertiesPane;
    public static ScrollPane informationPane;

    // Must pass when creating an instance
    public OpenCVOperator operator;
    public String operatorId;
    public String operatorName;
    public String elementStyleId;

    // Controller logic related.
    public boolean addedToOperatorsStack = true;
    public boolean cloneable = true;
    public boolean previewOnly = false;

    // Element and its UIs.
    public Node element;
    public AbstractPropertiesFormUI propertiesFormUI;
    public AbstractInformationUI informationUI;

    /**
     * To build an Operator UI element.
     */
    public void buildElement() {
        final Button button = new Button();
        button.setId(this.elementStyleId);
        button.setText(this.operatorName);
        button.prefHeight(HEIGHT);
        button.setPrefWidth(WIDTH);
        button.setOnDragDetected(this::onElementDragDetected);

        button.setOnDragDone((event) -> {
            if (event.isAccepted()) {
                onDragDone.accept(this);
            } else {
                onDragDone.accept(null);
            }
        });

        button.setOnMouseClicked((event) -> {
            if (!this.previewOnly) {
                propertiesPane.setContent(this.propertiesFormUI);
                informationPane.setContent(this.informationUI);
            }
        });
        this.element = button;
    }

    /**
     * To build the properties form UI during the cloning.
     *
     * @return - null
     */
    public AbstractPropertiesFormUI buildPropertiesFormUI() {
        return null;
    }

    /**
     * To build the information UI during the cloning.
     *
     * @return null;
     */
    public AbstractInformationUI buildInformationUI() {
        return null;
    }

    /**
     * To capture the operator dragging from the operator \
     * tool bar on the left.
     *
     * @param e - MouseEvent.
     */
    private void onElementDragDetected(MouseEvent e) {
        // create a new clone if cloneable.
        assert onCloneCreated != null;
        if (!cloneable) {
            onCloneCreated.accept(this);
        } else {
            onCloneCreated.accept(this.cloneElement());
        }
        Dragboard dragboard = element.startDragAndDrop(MOVE);
        dragboard.setDragView(element.snapshot(null, null));
        ClipboardContent content = new ClipboardContent();
        content.put(ANY_NODE, "operation");
        dragboard.setContent(content);
        e.consume();
    }

    /**
     * To clone the dragged element from the side
     * operator bar.
     *
     * @return - cloned of the dragged element.
     */
    private OperatorUIElement cloneElement() {
        OperatorUIElement clonedElement = new OperatorUIElement();
        clonedElement.operator = this.operator;
        clonedElement.operatorId = this.operatorId;
        clonedElement.operatorName = this.operatorName;
        clonedElement.cloneable = false;
        clonedElement.previewOnly = false;
        clonedElement.addedToOperatorsStack = false;
        clonedElement.propertiesFormUI = buildPropertiesFormUI();
        clonedElement.informationUI = buildInformationUI();
        clonedElement.elementStyleId = this.elementStyleId;
        clonedElement.buildElement(); //Building the clone.
        return clonedElement;
    }
}

package com.imagelab.component.event;

import com.imagelab.component.OperatorUIElement;

/**
 * Functional interface which only have an abstract method to
 * handle drag done logic.
 */
@FunctionalInterface
public interface OnUIElementDragDone {
    /**
     * Call back function to accept an OperatorUIElement
     * during onDrag.
     *
     * @param element - OperatorUIElement.
     */
    void accept(OperatorUIElement element);
}

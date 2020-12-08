/*******************************************************************************
 * Copyright (C) 2010, Matthias Sohn <matthias.sohn@sap.com>
 * Copyright (C) 2010, Stefan Lay <stefan.lay@sap.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.example.calc.internal;

import org.eclipse.example.calc.BinaryOperation;
import org.eclipse.example.calc.Operation;
import org.eclipse.example.calc.Operations;
import org.eclipse.example.calc.UnaryOperation;
import org.eclipse.example.calc.internal.operations.Equals;
import org.eclipse.example.calc.internal.operations.Minus;
import org.eclipse.example.calc.internal.operations.Plus;
import org.eclipse.example.calc.internal.operations.Square;

public class Calculator {
    // Kommentar für Branch masterGitHub
    private final TextProvider textProvider;

    private String cmd;

    private boolean clearText;

    private float value;

    public static String NAME = "Simple Calculator";

    public Calculator(final TextProvider textProvider) {
        this.textProvider = textProvider;
        setupDefaultOperations();
    }

    private void setupDefaultOperations() {
        // Das ist der erste Kommentar in masterGithub
        // Das ist der zweite Kommentar in master
        // auch noch ein Kommentar von Ruth
        new Equals();
        new Minus();
        new Plus();
        new Square();
    }

    private void calculate(final String cmdName) {
        float curValue;
        float newValue = 0;

        // get current value of display
        curValue = Float.parseFloat(this.textProvider.getDisplayText());

        final Operation currentOp = Operations.INSTANCE.getOperation(cmdName);
        if ((currentOp instanceof BinaryOperation) && (this.cmd == null)) {
            // if last clicked operation was binary and there is no saved
            // operation, store it
            this.cmd = cmdName;
            setClearText(true);
        } else {
            // if saved command is binary perform it
            final Operation savedOp = Operations.INSTANCE.getOperation(this.cmd);
            if (savedOp instanceof BinaryOperation) {
                final BinaryOperation bop = (BinaryOperation) savedOp;
                newValue = bop.perform(this.value, curValue);
            } // if current operation is unary perform it
            else if (currentOp instanceof UnaryOperation) {
                final UnaryOperation uop = (UnaryOperation) currentOp;
                newValue = uop.perform(curValue);
            }

            // display the result and prepare clear on next button
            this.textProvider.setDisplayText("" + newValue);
            setClearText(true);
            if (currentOp instanceof Equals) {
                // do not save "=" command
                this.cmd = null;
            } else if (currentOp instanceof BinaryOperation) {
                // save binary commands as they are executed on next operation
                this.cmd = cmdName;
            } else {
                // clear saved command
                this.cmd = null;
            }
        }

    }

    private boolean isCommand(final String name) {
        return (Operations.INSTANCE.getOperation(name) != null);
    }

    public void handleButtonClick(final String str) {
        if (isCommand(str)) {
            calculate(str);
        } else {
            final char digit = (str.toCharArray())[0];
            if (Character.isDigit(digit) || digit == '.') {
                if (this.clearText) {
                    // save current value and clear the display
                    this.value = Float.parseFloat(this.textProvider.getDisplayText());
                    this.textProvider.setDisplayText("");
                    setClearText(false);
                }

                // add new digit to display
                this.textProvider.setDisplayText(this.textProvider.getDisplayText() + digit);
            }
        }
    }

    public void setClearText(final boolean clearText) {
        this.clearText = clearText;
    }
}

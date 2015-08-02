package pl.edu.agh.hubert.differentiation.symbolic.simplification.rules;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.functions.DoubleOperandFunction;
import pl.edu.agh.hubert.differentiation.symbolic.functions.SingleOperandFunction;
import pl.edu.agh.hubert.differentiation.symbolic.simplification.AbstractSimplificationRule;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:13
 */
public class OperationOnConstantsRule extends AbstractSimplificationRule {


    @Override
    public boolean canSimplify(Function function) {
        if (function instanceof SingleOperandFunction) {
            SingleOperandFunction singleOperandFunction = (SingleOperandFunction) function;
            return isConstant(singleOperandFunction);
        }

        if (function instanceof DoubleOperandFunction) {
            DoubleOperandFunction doubleOperandFunction = (DoubleOperandFunction) function;
            return isConstant(doubleOperandFunction.getRightOperand()) && isConstant(doubleOperandFunction.getLeftOperand());
        }

        return false;
    }

    @Override
    public Function apply(Function function) {

        return function;
    }
}
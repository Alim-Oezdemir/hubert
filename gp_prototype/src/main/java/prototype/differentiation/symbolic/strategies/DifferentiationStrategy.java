package prototype.differentiation.symbolic.strategies;

import org.apache.log4j.Logger;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeToFunctionTranslator;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:12
 */
public abstract class DifferentiationStrategy {
    private static final TreeNodeToFunctionTranslator TRANSLATOR = new TreeNodeToFunctionTranslator();
    private static final Logger logger = Logger.getLogger(DifferentiationStrategy.class);

    protected abstract Function differentiateSpecific(TreeNode treeNode, String variable);

    public Function differentiate(TreeNode treeNode, String variable) {
        if (logger.isDebugEnabled()) {
            logger.debug(treeNode.toString());
        }

        return differentiateSpecific(treeNode, variable);
    }

    protected Function differentiateChild(TreeNode childNode, String variable) {
        FunctionType functionType = childNode.getFunctionType();
        DifferentiationStrategy differentiationStrategy = functionType.getDifferentiationStrategy();
        return differentiationStrategy.differentiate(childNode, variable);
    }

    protected Function translate(TreeNode node) {
        return TRANSLATOR.translate(node);
    }
}
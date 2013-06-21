package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Exp;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:47
 */
public class ExpDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode child = treeNode.getChildren()[0];
        return new Multiply(new Exp(translate(child)), differentiateChild(child, variable));
    }
}

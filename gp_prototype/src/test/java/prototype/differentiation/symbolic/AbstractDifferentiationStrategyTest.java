package prototype.differentiation.symbolic;

import prototype.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 22:43
 */
public class AbstractDifferentiationStrategyTest {
    protected DifferentiationStrategy strategy;

    protected void checkDifferentiation(TreeNode treeNode, Function expected, String variable) {
        Function actual = strategy.differentiate(treeNode, variable);
        TestTools.assertTheSameAs(expected, actual);
    }
}
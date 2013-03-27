package prototype.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.MapVariablesValuesContainer;
import prototype.data.VariablesValuesContainer;
import prototype.data.container.DataContainer;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.TreeNodeToFunctionTranslator;
import prototype.evolution.fitness.errorcalculator.ErrorCalculator;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 10.03.13
 * Time: 23:32
 */
class AllChromosomesAbsoluteErrorFitnessFunction extends GPFitnessFunction {

    private List<String> variablesNames;
    private final DataContainer dataContainer;
    private ErrorCalculator errorCalculator;

    public AllChromosomesAbsoluteErrorFitnessFunction(DataContainer dataContainer) {
        this.variablesNames = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double error = 0.0;

        for (String variableName : variablesNames) {
            error += evaluateChromosome(a_subject.getChromosome(0), variableName);
        }

        return error;
    }

    private double evaluateChromosome(ProgramChromosome chromosome, String chromosomeVariableName) {
        VariablesValuesContainer variablesValuesContainer = new MapVariablesValuesContainer();
        double chromosomeError = 0.0;
        TreeNode chromosomeAsTree = new TreeNodeFactory(variablesValuesContainer).createTreeNode(chromosome);
        Function chromosomeAsFunction = new TreeNodeToFunctionTranslator().translate(chromosomeAsTree);

        for (int i = 0; i < dataContainer.getRowsCount(); i++) {
            populateVariableValues(i, variablesValuesContainer);

            double chromosomeValueAtPointI = chromosomeAsFunction.evaluate();
            double dataPoint = dataContainer.getValue(chromosomeVariableName, i);

            chromosomeError += Math.abs(dataPoint - chromosomeValueAtPointI);
        }
        return chromosomeError;
    }

    private void populateVariableValues(int dataRow, VariablesValuesContainer variablesValuesContainer) {
        for (String variableName : variablesNames) {
            variablesValuesContainer.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
        }
    }

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

}

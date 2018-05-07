package ahp.decision;

import ahp.Main;
import ahp.models.AHP;
import ahp.models.PairwiseComparison;

import java.util.List;

public class Calculate {

    public static double[] weightVector(PairwiseComparison comparison) {
        boolean criterion = !AHP.areBasicAlternatives(comparison.alternatives, Main.AHP.alternatives);
        double[][] alternativeVectors = null;
        double[] criterionVector;
        if( criterion ){
            alternativeVectors = new double[comparison.alternatives.size()][];
            for( int i=0; i<comparison.alternatives.size(); i++ ){
                alternativeVectors[i] = PairwiseComparison.get(comparison.alternatives.get(i)).getWeightVector();
            }
        }
        comparison.weightRatios = weightRatios(comparison);
        if( criterion ){
            criterionVector = geometricMean(comparison.weightRatios);
            return linearCombination(criterionVector, alternativeVectors);
        }else{
            return geometricMean(comparison.weightRatios);
        }

    }

    private static double[] linearCombination(double[] criterionVector, double[][] alternativeVectors) {
        double[] result = new double[alternativeVectors[0].length];
        for( int r=0; r<result.length; r++ ){
            for( int c=0; c<criterionVector.length; c++ ){
                result[r] += criterionVector[c]*alternativeVectors[c][r];
            }
        }
        return result;
    }

    private static double[] geometricMean(double[][] weightRatios) {
        int n = weightRatios.length;
        double[] rowProducts = new double[n];
        double sum = 0.0;
        for( int r=0; r<n; r++ ){
            rowProducts[r] = 1.0;
            for( int c=0; c<weightRatios[0].length; c++ ){
                rowProducts[r] *= weightRatios[r][c];
            }
            rowProducts[r] = Math.pow(rowProducts[r],(1.0/(n*1.0)));
            sum += rowProducts[r];
        }
        for( int r=0; r<n; r++ ){
            rowProducts[r] /= sum;
        }
        return rowProducts;
    }

    private static double[][] weightRatios(PairwiseComparison comparison) {
        double[][] previous = comparison.weightRatios;
        int size = comparison.alternatives.size();
        boolean copyPrevious = ( previous != null && previous.length <= size );

        double[][] current = new double[size][];
        for( int i=0; i<size; i++ ){
            current[i] = new double[size];
        }
        for( int i=0; i<size; i++ ){
            current[i][i] = 1.0;
            for( int j=i+1; j<size; j++ ){
                if( copyPrevious && i<previous.length && j<previous.length && previous[i][j] > 0.0 ){
                    current[i][j] = previous[i][j];
                }else{
                    current[i][j] = DecisionMaker.ask(
                            comparison.alternatives.get(i),
                            comparison.alternatives.get(j),
                            comparison.id);
                }
                current[j][i] = 1.0 / current[i][j];
            }
        }
        return current;
    }
}

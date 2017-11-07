package lilium.beaconnavigation.Implementations;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealVector;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Enums.LoggerTypeEnum;
import lilium.beaconnavigation.Interfaces.LeastSquaresSolver;
import lilium.beaconnavigation.Interfaces.TrilaterationFunction;
import lilium.beaconnavigation.MainActivity;

/**
 * Solves a Trilateration problem with an instance of a
 * {@link LeastSquaresOptimizer}
 *
 * @author scott
 *
 */
public class NonLinearLeastSquaresSolver implements LeastSquaresSolver {

    protected final TrilaterationFunction function;
    protected final LeastSquaresOptimizer leastSquaresOptimizer;

    public NonLinearLeastSquaresSolver(TrilaterationFunction function, LeastSquaresOptimizer leastSquaresOptimizer) {
        this.function = function;
        this.leastSquaresOptimizer = leastSquaresOptimizer;
    }

    private Optimum solve(double[] target, double[] weights, double[] initialPoint, boolean debugInfo) {
        if (debugInfo) {
            System.out.println("Max Number of Iterations : " + AppConfig.get_solver_max_iterations());
        }

        LeastSquaresProblem leastSquaresProblem = LeastSquaresFactory.create(
                // function to be optimized
                function,
                // target values at optimal point in least square equation
                // (x0+xi)^2 + (y0+yi)^2 + ri^2 = target[i]
                new ArrayRealVector(target, false), new ArrayRealVector(initialPoint, false), new DiagonalMatrix(weights), null, 1000, AppConfig.get_solver_max_iterations());

        return leastSquaresOptimizer.optimize(leastSquaresProblem);
    }

    private Optimum solve(double[] target, double[] weights, double[] initialPoint) {
            return solve(target, weights, initialPoint, false);
    }

    private Optimum solve(boolean debugInfo) {
        int numberOfPositions = function.getPositions().length;
        int positionDimension = function.getPositions()[0].length;

        double[] initialPoint = new double[positionDimension];
        // initial point, use average of the vertices
        for (int i = 0; i < function.getPositions().length; i++) {
            double[] vertex = function.getPositions()[i];
            for (int j = 0; j < vertex.length; j++) {
                initialPoint[j] += vertex[j];
            }
        }
        for (int j = 0; j < initialPoint.length; j++) {
            initialPoint[j] /= numberOfPositions;
        }

        if (debugInfo) {
            StringBuilder output = new StringBuilder("initialPoint: ");
            for (int i = 0; i < initialPoint.length; i++) {
                output.append(initialPoint[i]).append(" ");
            }
            System.out.println(output.toString());
        }

        double[] target = new double[numberOfPositions];
        double[] distances = function.getDistances();
        double[] weights = new double[target.length];
        // Weights are inversely proportional to the the square of the distances I think
        for (int i = 0; i < target.length; i++) {
            target[i] = 0.0;
            // weights[i] = 1.0;
            weights[i] = (distances[i] * distances[i]);
        }

        return solve(target, weights, initialPoint, debugInfo);
    }

    public RealVector solve() {
        MainActivity.logger.log(LoggerTypeEnum.NonLinearLeastSquaresSolver + "," + System.currentTimeMillis() + ",started solve()");
        RealVector result =  solve(false).getPoint();
        MainActivity.logger.log(LoggerTypeEnum.NonLinearLeastSquaresSolver + "," + System.currentTimeMillis() + ",finished solve()");
        return result;
    }
}

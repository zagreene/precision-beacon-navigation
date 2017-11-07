package lilium.beaconnavigation.Implementations;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Enums.LoggerTypeEnum;
import lilium.beaconnavigation.Interfaces.TrilaterationFunction;
import lilium.beaconnavigation.MainActivity;

/**
 * Models the Trilateration problem. This is a formulation for a nonlinear least
 * squares optimizer.
 *
 * @author scott
 *
 */
public class StandardTrilaterationFunction implements TrilaterationFunction {

    protected static final double epsilon = AppConfig.get_trilateration_epsilon();

    /**
     * Known positions of static nodes
     */
    protected final double positions[][];

    /**
     * Euclidean distances from static nodes to mobile node
     */
    protected final double distances[];

    public StandardTrilaterationFunction(double positions[][], double distances[]) {
        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",started constructor()");
        }

        if(positions.length < 2) {
            throw new IllegalArgumentException("Need at least two positions.");
        }

        if(positions.length != distances.length) {
            throw new IllegalArgumentException("The number of positions you provided, " + positions.length + ", does not match the number of distances, " + distances.length + ".");
        }

        // bound distances to strictly positive domain
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Math.max(distances[i], epsilon);
        }

        int positionDimension = positions[0].length;
        for (int i = 1; i < positions.length; i++) {
            if(positionDimension != positions[i].length) {
                throw new IllegalArgumentException("The dimension of all positions should be the same.");
            }
        }

        this.positions = positions;
        this.distances = distances;
        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",finished constructor()");
        }
    }

    public final double[] getDistances() {
        return distances;
    }

    public final double[][] getPositions() {
        return positions;
    }

    /**
     * Calculate and return Jacobian function Actually return initialized function
     *
     * Jacobian matrix, [i][j] at
     * J[i][0] = delta_[(x0-xi)^2 + (y0-yi)^2 - ri^2]/delta_[x0] at
     * J[i][1] = delta_[(x0-xi)^2 + (y0-yi)^2 - ri^2]/delta_[y0] partial derivative with respect to the parameters passed to value() method
     *
     * @param point for which to calculate the slope
     * @return Jacobian matrix for point
     */
    public RealMatrix jacobian(RealVector point) {
        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",started jacobian()");
        }
        double[] pointArray = point.toArray();

        double[][] jacobian = new double[distances.length][pointArray.length];
        for (int i = 0; i < jacobian.length; i++) {
            for (int j = 0; j < pointArray.length; j++) {
                jacobian[i][j] = 2 * pointArray[j] - 2 * positions[i][j];
            }
        }

        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",finished jacobian()");
        }
        return new Array2DRowRealMatrix(jacobian);
    }

    @Override
    public Pair<RealVector, RealMatrix> value(RealVector point) {
        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",started value()");
        }
        // input
        double[] pointArray = point.toArray();

        // output
        double[] resultPoint = new double[this.distances.length];

        // compute least squares
        for (int i = 0; i < resultPoint.length; i++) {
            resultPoint[i] = 0.0;
            // calculate sum, add to overall
            for (int j = 0; j < pointArray.length; j++) {
                resultPoint[i] += (pointArray[j] - this.getPositions()[i][j]) * (pointArray[j] - this.getPositions()[i][j]);
            }
            resultPoint[i] -= (this.getDistances()[i]) * (this.getDistances()[i]);
        }

        RealMatrix jacobian = jacobian(point);
        if(MainActivity.walking) {
            MainActivity.logger.log(LoggerTypeEnum.StandardTrilaterationFunction + "," + System.currentTimeMillis() + ",finished value()");
        }
        return new Pair<RealVector, RealMatrix>(new ArrayRealVector(resultPoint), jacobian);
    }
}

package lilium.beaconnavigation.Interfaces;

import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

/**
 * Created by boylec on 1/29/17.
 */

public interface TrilaterationFunction extends MultivariateJacobianFunction {
    double[] getDistances();

    double[][] getPositions();

    RealMatrix jacobian(RealVector point);

    @Override
    Pair<RealVector, RealMatrix> value(RealVector point);
}

package lilium.beaconnavigation.Implementations;

import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Interfaces.Beacon;
import lilium.beaconnavigation.Interfaces.LeastSquaresSolver;
import lilium.beaconnavigation.Interfaces.PositionUpdater;
import lilium.beaconnavigation.Interfaces.TrilaterationFunction;
import lilium.beaconnavigation.MainActivity;

public class ClusteredMultiThreadedPositionUpdater extends MultiThreadedPositionUpdater implements PositionUpdater {

    private int _clusterSize;

    public ClusteredMultiThreadedPositionUpdater(int clusterSize) {
        super();
        this._clusterSize = clusterSize;
    }

    public void start()
    {
        super.start();
    }

    private ArrayList<ArrayList<Beacon>> Cluster(ArrayList<Beacon> beacons)
    {
        ArrayList<ArrayList<Beacon>> clusters = new ArrayList<ArrayList<Beacon>>();
        int currentClusterIndex = 0;
        int currentClusterSize = 0;
        clusters.add(new ArrayList<Beacon>(_clusterSize));
        for(Beacon b: beacons)
        {
            clusters.get(currentClusterIndex).add(b);
            currentClusterIndex++;
            currentClusterSize++;
            if(currentClusterSize == _clusterSize)
            {
                clusters.add(new ArrayList<Beacon>(_clusterSize));
                currentClusterIndex = 0;
                currentClusterSize = 0;
            }
        }
        return clusters;
    }

    @Override
    protected void updatePosition(ArrayList<Beacon> beacons) {
        if (beacons.size() > 1) {

        ArrayList<ArrayList<Beacon>> clusters = Cluster(beacons);

        double[][] positions = new double[clusters.size()][2];
        double[] distances = new double[clusters.size()];

            for (int i = 0; i < clusters.size(); i++) {
                float totalX = 0;
                float totalY = 0;
                float totalDist = 0;
                float count = clusters.get(i).size();

                //Averaging distances, x's, and y's for each cluster of beacons
                for(Beacon b: clusters.get(i)) {
                    totalX += b.getX();
                    totalY += b.getY();
                    totalDist += b.distance();
                }

                positions[i][0] = totalX/count;
                positions[i][1] = totalY/count;

                //we want linear distances, the distance readings don't have to be accurate
                //they just need to be consistent across all beacons
                //because the trilateration function uses them as relative to each other
                distances[i] = totalDist/count;
            }
            try {
                TrilaterationFunction triFunc = new StandardTrilaterationFunction(positions,distances);
                LeastSquaresSolver solver = new NonLinearLeastSquaresSolver(triFunc, new LevenbergMarquardtOptimizer());
                RealVector vec = solver.solve();
                double[] calculatedPosition = vec.toArray();
                MainActivity.position = new PointF((float) calculatedPosition[0], (float) calculatedPosition[1]);
                lastUpdate = System.currentTimeMillis();
                try{
                    Thread.sleep(AppConfig.get_minimium_position_delay());
                } catch (InterruptedException e){
                    Log.e("async_position","Interrupted Exception");
                }
            } catch (TooManyEvaluationsException e) {
                // position stays the same
                Log.e("async_position", "TOO MANY CALCULATIONS");
            }
        }
    }
}



package lilium.beaconnavigation.Implementations;

import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Enums.LoggerTypeEnum;
import lilium.beaconnavigation.Interfaces.Beacon;
import lilium.beaconnavigation.Interfaces.LeastSquaresSolver;
import lilium.beaconnavigation.Interfaces.PositionUpdater;
import lilium.beaconnavigation.Interfaces.TrilaterationFunction;
import lilium.beaconnavigation.MainActivity;

public class MultiThreadedPositionUpdater implements PositionUpdater {
    protected long lastUpdate;
    protected Thread positionUpdate;
    protected AtomicBoolean stop;
    protected AtomicInteger running;

    public MultiThreadedPositionUpdater() {
        lastUpdate = System.currentTimeMillis();
        stop = new AtomicBoolean(false);
        running = new AtomicInteger(0);

        //Start a new thread to run the encompassed method
        positionUpdate = new Thread(new Runnable(){

            //Here is the encompassed method that runs on the thread
            public void run(){
                while(! stop.get()) {
                    if (running.compareAndSet(0,1)) { // is an async position updater running on any threads?
                        if (System.currentTimeMillis() - lastUpdate > AppConfig.get_minimium_position_delay()) {
                            //Runs position update only on "placed" beacons
                            new PositionUpdate().execute(MainActivity.beaconKeeper.clonePlaced());
                           try{
                                Thread.sleep(AppConfig.get_maximum_spawn_wait());
                            } catch (InterruptedException e){
                                Log.e("PositionThread","Interrupted Exception");
                                e.printStackTrace();
                            }
                        }
                        else{
                            running.decrementAndGet();
                        }
                    }
                }
            }

        }, "PositionThread");
    }

    public void start()
    {
        //Starts the thread process
        positionUpdate.start();
    }

    protected void updatePosition(ArrayList<Beacon> beacons) {

        double[][] positions = new double[beacons.size()][2];
        double[] distances = new double[beacons.size()];
        if (beacons.size() > 1) {
            if(MainActivity.walking)
            {
                MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",started updatePositionArray()," + MainActivity.position.x + "," + MainActivity.position.y + "," + Build.VERSION.SDK_INT );
            }

            for (int i = 0; i < beacons.size(); i++) {
                positions[i][0] = beacons.get(i).getX();
                positions[i][1] = beacons.get(i).getY();
                //we want linear distances, the distance readings don't have to be accurate
                //they just need to be consistent across all beacons
                //because the trilateration function uses them as relative to each other
                distances[i] = beacons.get(i).distance();
            }
            try {
                if(MainActivity.walking) {
                    MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",constructing trilateration function," + Build.VERSION.SDK_INT);
                }
                TrilaterationFunction triFunc = new StandardTrilaterationFunction(positions,distances);
                if(MainActivity.walking) {
                    MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",finished constructing trilateration function," + Build.VERSION.SDK_INT);
                }
                if(MainActivity.walking) {
                    MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",starting linear least squares solver function," + Build.VERSION.SDK_INT);
                }
                LeastSquaresSolver solver = new NonLinearLeastSquaresSolver(triFunc, new LevenbergMarquardtOptimizer());

                RealVector vec = solver.solve();

                if(MainActivity.walking) {
                    MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",finished linear least squares solver function," + Build.VERSION.SDK_INT);
                }

                double[] calculatedPosition = vec.toArray();
                MainActivity.position = new PointF((float) calculatedPosition[0], (float) calculatedPosition[1]);
                if(MainActivity.walking) {
                    MainActivity.logger.log(LoggerTypeEnum.PositionUpdater + "," + System.currentTimeMillis() + ",finished updatePositionArray()," + MainActivity.position.x + "," + MainActivity.position.y + "," + Build.VERSION.SDK_INT);
                }
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

    protected class PositionUpdate extends AsyncTask<Object,Void,Void> {

        protected Void doInBackground(Object ... args) {
            ArrayList<Beacon> beacons;
            beacons = (ArrayList<Beacon>) args[0];
            updatePosition(beacons);
            return null;
        }

        protected void onPostExecute(Void result) {
            MainActivity.map.invalidate();
            running.decrementAndGet();
        }
    }
}



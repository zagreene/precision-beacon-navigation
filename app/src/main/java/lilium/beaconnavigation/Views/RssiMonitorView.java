package lilium.beaconnavigation.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Classes.Location;
import lilium.beaconnavigation.Interfaces.Beacon;
import lilium.beaconnavigation.MainActivity;
import lilium.beaconnavigation.R;

public class RssiMonitorView extends TextView {
    Paint p = new Paint();

    public RssiMonitorView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String advString = "Waiting...";
        if(MainActivity.beaconToMonitor != null)
        {
            ArrayList<Integer> rssiAdvArray = new ArrayList<Integer>(MainActivity.beaconToMonitor.getRssiQueue());
            double smoothed = MainActivity.beaconToMonitor.smoothRssi();

            advString = rssiQueueToString(rssiAdvArray,15);
            advString += "\r\nSmoothed: " + smoothed;
        }

        setText(advString);
    }


    private String rssiQueueToString(ArrayList<Integer> rssiQueue, int maxNumberOfAdvToDisplay)
    {
        String result = "";
        int actualNumber = Math.min(maxNumberOfAdvToDisplay,rssiQueue.size());
        for(int x = 0; x < actualNumber; x++)
        {
            Integer queueValue = rssiQueue.get(x);
            if(queueValue != 0) {
                result += queueValue;
                if (x < actualNumber - 1) {
                    result += ", ";
                }
            }
        }
        return result;
    }
}

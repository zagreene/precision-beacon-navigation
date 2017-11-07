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

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Classes.Location;
import lilium.beaconnavigation.Interfaces.Beacon;
import lilium.beaconnavigation.MainActivity;
import lilium.beaconnavigation.R;

public class DrawableImageView extends SubsamplingScaleImageView {
    Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.beacon);
    Bitmap marker = BitmapFactory.decodeResource(getResources(), R.mipmap.marker);
    Beacon beaconToMonitor = null;
    Paint p = new Paint();
    List<Location> path;

    public DrawableImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mapWidthConstant = AppConfig.get_map_width_constant();
        int mapHeightConstant = AppConfig.get_map_height_constant();

        super.onDraw(canvas);

        if(!MainActivity.loaded) return;

        Paint p=new Paint();

        //Walking nav stuff
        int height = this.getMeasuredHeight();
        int width = this.getMeasuredWidth();

        if(path!=null){
            Location prev=path.get(0);

            p.setAntiAlias(true);

            if (path != null && path.size() >= 2) {
                Path vPath = new Path();

                PointF vPrev = sourceToViewCoord(width* path.get(0).x/mapWidthConstant, height*path.get(0).y/mapHeightConstant);

                vPath.moveTo(vPrev.x, vPrev.y);
                for (int i = 1; i < path.size(); i++) {
                    PointF vPoint = sourceToViewCoord(path.get(i).x*width/mapWidthConstant, path.get(i).y*height/mapHeightConstant);

                    vPath.lineTo(vPoint.x,vPoint.y);
                    vPrev = vPoint;
                }

                p.setStyle(Paint.Style.STROKE);
                p.setStrokeCap(Paint.Cap.ROUND);
                p.setStrokeWidth(10);
                p.setColor(Color.argb(150,255,255,255));
                canvas.drawPath(vPath, p);
                p.setStrokeWidth(5);
                p.setColor(Color.argb(175, 51, 181, 229));
                canvas.drawPath(vPath, p);
            }
        }

        //End walking nav stuff

        ArrayList<Beacon> beacons = MainActivity.beaconKeeper.clonePlaced();
        for(int i = 0; i < beacons.size(); i++) {
            PointF offset = sourceToViewCoord(beacons.get(i).getX(), beacons.get(i).getY());
            if (offset != null) {
                Matrix matrix = new Matrix();
                matrix.postTranslate(offset.x - b.getWidth() / 2, offset.y - b.getHeight() / 2);
                canvas.drawBitmap(b, matrix, p);
            }
        }

        PointF offset = sourceToViewCoord(MainActivity.position.x, MainActivity.position.y);

        if (offset != null && beacons.size() > 1) {
            Matrix matrix = new Matrix();
            matrix.postTranslate(offset.x - marker.getWidth() / 2, offset.y - marker.getHeight() / 2);
            canvas.drawBitmap(marker, matrix, p);
        }
    }


    public void setPath(List<Location> path){
        this.path=path;
    }
}

package lilium.beaconnavigation.Services;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import lilium.beaconnavigation.Classes.ImageProcessingData;
import lilium.beaconnavigation.Interfaces.ImageProcessingService;

/**
 * Created by boylec on 2/21/17.
 */

public class BasicImageProcessingService implements ImageProcessingService {

    private final static float _wallCutoff = (float)255/2;

    public ImageProcessingData DeduceWallPxPositions(Bitmap image)
    {
        ArrayList<Integer[]> wallPositions = new ArrayList<>();
        Bitmap filteredBitmap = image;

        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                // Get the color of a pixel within myBitmap.
                int px = image.getPixel(x, y);

//                int[] pxColor = getRgb(px,image.hasAlpha());
//
//                if(isWall(pxColor))
//                {
//                    wallPositions.add(new Integer[]{x,y});
//                    filteredBitmap.setPixel(x,y,Color.RED);
//                }

                if(isWall(px))
                {
                    wallPositions.add(new Integer[]{x,y});
                    filteredBitmap.setPixel(x,y,Color.RED);
                }
            }
        }

        ImageProcessingData result = new ImageProcessingData();
        result.WallPositions = wallPositions.toArray(new Integer[wallPositions.size()][]);
        result.FilteredBitmap = filteredBitmap;
        return result;
    }

    private static boolean isWall(int px)
    {
        return Color.MAGENTA == px;
    }

    private static boolean isWall(int[] pxColor)
    {
        float avgColor = getAverage(new int[]{pxColor[0],pxColor[1],pxColor[2]});
        return avgColor <= _wallCutoff;
    }


    private static float getAverage(int[] variables)
    {
        float total = 0;
        int size = variables.length;
        for(int x = 0; x < size; x++)
        {
            total += variables[x];
        }

        return total/size;
    }

    private static int[] getRgb(int px, boolean hasAlpha) {
        if(hasAlpha)
        {
            return new int[]{
                    Color.red(px),
                    Color.green(px),
                    Color.blue(px),
                    Color.alpha(px)
            };
        }
        else{
            return new int[]{
                    Color.red(px),
                    Color.green(px),
                    Color.blue(px),
            };
        }
    }
}

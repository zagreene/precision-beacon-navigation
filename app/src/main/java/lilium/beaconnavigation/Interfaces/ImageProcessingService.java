package lilium.beaconnavigation.Interfaces;


import android.graphics.Bitmap;

import lilium.beaconnavigation.Classes.ImageProcessingData;

/**
 * Created by boylec on 1/29/17.
 */

public interface ImageProcessingService {
    ImageProcessingData DeduceWallPxPositions(Bitmap image);
}

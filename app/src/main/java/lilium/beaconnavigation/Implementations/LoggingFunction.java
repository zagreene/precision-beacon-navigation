package lilium.beaconnavigation.Implementations;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import lilium.beaconnavigation.Interfaces.Logger;
import lilium.beaconnavigation.Implementations.StandardBluetoothMonitor;

/**
 * Created by Saber on 4/6/2017.
 */

public class LoggingFunction implements Logger {
    FileWriter writer;

    public LoggingFunction(String logFileName)
    {
        try {

            File root=Environment.getExternalStorageDirectory();

            File testFile = new File(root, logFileName);
            if(!testFile.exists())
            {
                root.mkdirs();
                root.createNewFile();
            }


            writer = new FileWriter(testFile);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String logString)
    {
        try
        {
            if(writer != null) {
                //This is where the actual values will be written into the text file
                writer.append(logString + "\n");
                writer.flush();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void cleanUp()
    {
        try {
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

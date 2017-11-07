package lilium.beaconnavigation.Implementations;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Interfaces.BluetoothMonitor;
import lilium.beaconnavigation.MainActivity;

public class StandardBluetoothMonitor implements BluetoothMonitor {

    private ScanCallback scanCallback;
    private BluetoothAdapter.LeScanCallback deprecated_scanCallback;
    private ScanSettings settings;
    private ArrayList<ScanFilter> filters;
    private java.util.Calendar cal = Calendar.getInstance();
    private byte beacon_filter[];
    int thisScansRssi;

    //The minimum RSSI that we care about (we won't treat any other scans as viable beacons unless their RSSIs are greater than this number

    public StandardBluetoothMonitor() {
        beacon_filter = new byte[]{0x02, 0x01, 0x06, 0x1a, (byte) 0xff, 0x4c, 0x00, 0x02, 0x15};

        //If our Android version is greater than 21 then device discovery is better
        if (Build.VERSION.SDK_INT >= 21) {
            scanCallback = new ScanCallback() {
                @Override
                @TargetApi(21)
                public void onScanResult(int callbackType, final ScanResult result) {
                    //filter outlier beacons
                    thisScansRssi = result.getRssi();
                    if (is_Beacon(result.getScanRecord().getBytes()) && thisScansRssi > AppConfig.get_bt_mon_filter_min()) {
                        //Do an idempotent update of beacons in beacon keeper (whether "placed" or "unplaced" within the beacon keeper)
                        String beaconId = result.getDevice().getAddress().replace(":", "");
                        MainActivity.beaconKeeper.async_updateBeacon(beaconId, thisScansRssi);
                    }
                }
            };
            settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            filters = new ArrayList<>();
        } else {
            //device discovery for API level 18-20, this is very slow
            deprecated_scanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    //filter outlier beacons
                    if (is_Beacon(scanRecord) && rssi > AppConfig.get_bt_mon_filter_min()) {
                        //update beacons
                        String beaconId = device.getAddress().replace(":", "");
                        MainActivity.beaconKeeper.async_updateBeacon(beaconId, rssi);
                    }
                }
            };
        }
    }


    //Compares the scanned unique identifier to the beacon_filter we have defined to check if it the beacon type we are looking for
    private boolean is_Beacon(byte[] scanRecord) {
        byte[] prefix = new byte[9];
        System.arraycopy(scanRecord, 0, prefix, 0, 9);
        if (Arrays.equals(prefix, beacon_filter)) {
            return true;
        }
        return false;
    }

    //This is called to start the monitoring
    public void start() {
        if (MainActivity.btAdapter != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                MainActivity.btAdapter.getBluetoothLeScanner().startScan(filters, settings, scanCallback);
            } else {
                //device discovery for API level 18-20, this is very slow
                MainActivity.btAdapter.startLeScan(deprecated_scanCallback);
            }
        }
    }

    public int rssiValue() {
        if (Build.VERSION.SDK_INT >= 21) {
            return thisScansRssi;
        } else {
            //return value for lower than build version 21
            return thisScansRssi;
        }
    }
}

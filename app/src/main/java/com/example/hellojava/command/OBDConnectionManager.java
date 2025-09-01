package com.example.hellojava.command;
import android.bluetooth.BluetoothSocket;

public class OBDConnectionManager {
    private static BluetoothSocket socket;
    private static Thread obdThread;

    public static void setSocket(BluetoothSocket s) {
        socket = s;
    }

    public static BluetoothSocket getSocket() {
        return socket;
    }

    public static void startOBDReading(Runnable readingRunnable) {
        if (obdThread == null || !obdThread.isAlive()) {
            obdThread = new Thread(readingRunnable);
            obdThread.start();
        }
    }

    public static void stopOBDReading() {
        if (obdThread != null && obdThread.isAlive()) {
            obdThread.interrupt();
        }
    }
}


package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BatteryVoltageCommand {

    private String rawData;
    private float voltage = 0;
    private String status = "";

    public void run(InputStream in, OutputStream out) throws IOException {
        // OBD-II PID for control module voltage
        String cmd = "01 42\r"; // PID 42 = Control module voltage
        out.write(cmd.getBytes());
        out.flush();

        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '>') break; // end of response
            sb.append((char) c);
        }
        rawData = sb.toString().trim();
        parseResponse();
    }

    private void parseResponse() {
        if (rawData == null || rawData.isEmpty()) return;

        try {
            String[] bytes = rawData.split("\\s+");
            if (bytes.length >= 3 && bytes[0].equals("41") && bytes[1].equals("42")) {
                int A = Integer.parseInt(bytes[2], 16);
                int B = Integer.parseInt(bytes[3], 16);
                voltage = ((A * 256) + B) / 1000f; // convert to volts

                // Determine battery status
                if (voltage < 12.0) {
                    status = "Low";
                } else if (voltage <= 12.6) {
                    status = "Normal (engine off)";
                } else if (voltage <= 14.8) {
                    status = "Charging (engine on)";
                } else {
                    status = "High";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getVoltage() {
        return voltage;
    }

    public String getStatus() {
        return status;
    }

    public String getFormattedResult() {
        return voltage + " V (" + status + ")";
    }

    public String getRawResult() {
        return rawData;
    }
}

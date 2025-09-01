package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Fuel Level Command (PID 01 2F)
 * Returns fuel level in percentage of tank.
 * Robust version: handles extra spaces, headers, and empty lines.
 */
public class FuelLevelCommand {

    private String rawData;
    private double fuelLevelPercent = 0;

    public void run(InputStream in, OutputStream out) throws IOException, InterruptedException {
        String cmd = "01 2F\r"; // PID for Fuel Level
        out.write(cmd.getBytes());
        out.flush();

        // Small delay for adapter
        Thread.sleep(50);

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
        fuelLevelPercent = 0; // default

        if (rawData == null || rawData.isEmpty()) return;

        // Split by any whitespace or newline
        String[] tokens = rawData.split("[\\s\\r\\n]+");

        for (int i = 0; i < tokens.length - 2; i++) {
            // Look for "41 2F" in response
            if (tokens[i].equalsIgnoreCase("41") && tokens[i + 1].equalsIgnoreCase("2F")) {
                try {
                    int A = Integer.parseInt(tokens[i + 2], 16);
                    fuelLevelPercent = (A * 100.0) / 255.0; // OBD-II formula
                    return;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Log for debugging
        System.out.println("FuelLevel rawData: " + rawData);
    }

    /**
     * Returns fuel level in percent
     */
    public double getFuelLevel() {
        return fuelLevelPercent;
    }

    /**
     * Raw OBD-II response string
     */
    public String getRawResult() {
        return rawData;
    }
}

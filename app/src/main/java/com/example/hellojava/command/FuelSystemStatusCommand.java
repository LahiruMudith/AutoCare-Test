package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Fuel System Status Command (PID 01 03)
 * Short one-word results
 */
public class FuelSystemStatusCommand {

    private String rawData;
    private String result;

    public void run(InputStream in, OutputStream out) throws IOException {
        String cmd = "01 03\r";
        out.write(cmd.getBytes());
        out.flush();

        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '>') break;
            sb.append((char) c);
        }
        rawData = sb.toString().trim();
        parseResponse();
    }

    private void parseResponse() {
        if (rawData == null || rawData.isEmpty()) {
            result = "NoData";
            return;
        }

        try {
            String[] bytes = rawData.split("\\s+"); // handles big white spaces too
            int sys1 = Integer.parseInt(bytes[2], 16);
            int sys2 = Integer.parseInt(bytes[3], 16);

            result = "S1:" + decodeStatus(sys1) + " | S2:" + decodeStatus(sys2);
        } catch (Exception e) {
            result = "Err:" + rawData;
        }
    }

    private String decodeStatus(int value) {
        switch (value) {
            case 1: return "OpenTemp";      // Open loop (insufficient engine temp)
            case 2: return "ClosedO2";      // Closed loop (O2 feedback)
            case 4: return "OpenLoad";      // Open loop (engine load/decel)
            case 8: return "Failure";       // Open loop (system failure)
            case 16: return "O2Fault";      // Closed loop (O2 sensor fault)
            default: return "Unknown";
        }
    }



    public String getFormattedResult() {
        return result;
    }

    public String getRawResult() {
        return rawData;
    }
}

package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EngineCoolantTemperatureCommand {
    private String rawData;
    private int coolantTempC = 0;

    public void run(InputStream in, OutputStream out) throws IOException {
        String cmd = "01 05\r"; // Coolant temp PID
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
            for (int i = 0; i < bytes.length - 2; i++) {
                if (bytes[i].equals("41") && bytes[i + 1].equals("05")) {
                    int A = Integer.parseInt(bytes[i + 2], 16);
                    coolantTempC = A - 40; // formula from OBD-II standard
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCoolantTempC() {
        return coolantTempC + " °C";
    }
}

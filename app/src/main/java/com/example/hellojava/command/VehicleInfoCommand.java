package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VehicleInfoCommand {
    private String rawData;
    private String vin = "";

    public void run(InputStream in, OutputStream out) throws IOException {
        String cmd = "09 02\r"; // PID for VIN
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
        if (rawData == null || rawData.isEmpty()) return;

        String[] lines = rawData.split("\\r?\\n");
        StringBuilder vinBuilder = new StringBuilder();
        for (String line : lines) {
            line = line.replaceAll(" ", "");
            if (line.startsWith("4902")) { // VIN response
                vinBuilder.append(line.substring(4));
            }
        }
        vin = vinBuilder.toString();
    }

    public String getVin() {
        return vin;
    }

    public String getRawResult() {
        return rawData;
    }
}

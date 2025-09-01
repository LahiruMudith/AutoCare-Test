package com.example.hellojava.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SpeedCommand {
    private String rawData;
    private int speedKmh = 0;

    public void run(InputStream in, OutputStream out) throws IOException {
        // OBD-II PID for Vehicle Speed
        String cmd = "01 0D\r";
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
            String[] bytes = rawData.split("\\s+"); // split by any whitespace
            for (int i = 0; i < bytes.length - 2; i++) {
                if (bytes[i].equals("41") && bytes[i + 1].equals("0D")) {
                    speedKmh = Integer.parseInt(bytes[i + 2], 16);
                    return; // stop after first valid match
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getSpeedKmh() {
        return speedKmh;
    }
}

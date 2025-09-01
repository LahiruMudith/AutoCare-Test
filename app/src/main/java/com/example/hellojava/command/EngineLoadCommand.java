package com.example.hellojava.command;

public class EngineLoadCommand extends ObdCommand {

    public EngineLoadCommand() {
        super("0104"); // PID for Calculated Engine Load
    }

    @Override
    protected void processResponse() {
        // The rawResponse should be something like "41 04 80"
        try {
            String[] bytes = rawResponse.split(" ");
            // Check if response is valid: has enough parts and correct header
            if (bytes.length > 2 && bytes[0].equals("41") && bytes[1].equals("04")) {
                // Parse the data byte
                int byteValue = Integer.parseInt(bytes[2], 16);
                // Apply the standard formula
                double load = (byteValue / 255.0) * 100.0;

                // Store the results
                calculatedResult = String.valueOf(load);
                formattedResult = String.format("%.1f%%", load);
            } else {
                calculatedResult = "N/A";
                formattedResult = "N/A";
            }
        } catch (Exception e) {
            calculatedResult = "Error";
            formattedResult = "Error";
        }
    }
}
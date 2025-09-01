package com.example.hellojava.command;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ObdCommand {

    protected String cmd; // The raw OBD command string (e.g., "010D")
    protected String rawResponse; // The raw response from the adapter
    protected String formattedResult; // The human-readable result (e.g., "85.0 km/h")
    protected String calculatedResult; // The processed value (e.g., "85.0")

    public ObdCommand(String command) {
        this.cmd = command;
    }

    // The main method to execute the command
    public void run(InputStream in, OutputStream out) throws IOException, InterruptedException {
        sendCommand(out);
        readResponse(in);
        processResponse();
    }

    // Sends the command to the OBD adapter
    protected void sendCommand(OutputStream out) throws IOException, InterruptedException {
        // Ensure the command is in the correct format and has a carriage return
        String command = cmd + "\r\n";
        out.write(command.getBytes());
        out.flush();
        // Small delay to let the ECU process the command. Crucial!
        Thread.sleep(200);
    }

    // Reads the raw response from the adapter
    protected void readResponse(InputStream in) throws IOException {
        byte[] buffer = new byte[256];
        int bytesRead;
        StringBuilder response = new StringBuilder();

        // Wait a bit for the data to arrive in the stream
        try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }

        // Read all available data until the stream is empty or the '>' prompt is found
        while (in.available() > 0) {
            bytesRead = in.read(buffer);
            if (bytesRead > 0) {
                String part = new String(buffer, 0, bytesRead);
                response.append(part);
                // ELM327 signals the end of a response with a '>' prompt
                if (part.contains(">")) {
                    break;
                }
            }
            try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        // Clean the response: remove the prompt, whitespace, and the echoed command
        rawResponse = response.toString()
                .replace(">", "")
                .replace(cmd, "")
                .replace("\r", "")
                .replace("\n", "")
                .replace("SEARCHING...", "")
                .trim();
    }

    // This method must be implemented by each specific command to parse its unique response
    protected abstract void processResponse();

    // Public getters for the results
    public String getFormattedResult() {
        return formattedResult;
    }
    public String getCalculatedResult() {
        return calculatedResult;
    }
    public String getRawResponse() {
        return rawResponse;
    }
}
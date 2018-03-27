package server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainServerController {

	public static void executeCommands(String script) throws IOException, InterruptedException {

	   // File tempScript = createTempScript();

	    try {
	        ProcessBuilder pb = new ProcessBuilder("bash", script);
	        pb.inheritIO();
	        Process process = pb.start();
	        process.waitFor();
	    } finally {

	    }
	}


	/**
	 * Execute a bash command. We can handle complex bash commands including
	 * multiple executions (; | && ||), quotes, expansions ($), escapes (\), e.g.:
	 *     "cd /abc/def; mv ghi 'older ghi '$(whoami)"
	 * @param command
	 * @return true if bash got started, but your command may have failed.
	 */
	public static boolean executeBashCommand(String command) {
	    boolean success = false;
	    System.out.println("Executing BASH command:   " + command);
	    Runtime r = Runtime.getRuntime();
	    // Use bash -c so we can handle things like multi commands separated by ; and
	    // things like quotes, $, |, and \. My tests show that command comes as
	    // one argument to bash, so we do not need to quote it to make it one thing.
	    // Also, exec may object if it does not have an executable file as the first thing,
	    // so having bash here makes it happy provided bash is installed and in path.
	    String[] commands = {"bash", "-c", command};
	    try {
	        Process p = r.exec(commands);

	        p.waitFor();
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	            System.out.println(line);
	        }

	        b.close();
	        success = true;
	    } catch (Exception e) {
	        System.err.println("Failed to execute bash with command: " + command);
	        e.printStackTrace();
	    }
	    return success;
	}

}

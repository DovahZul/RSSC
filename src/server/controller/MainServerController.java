package server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.pty4j.PtyProcess;

public class MainServerController {

	private static final String BASH_GREETING="[\\d \\A]\\u@\\h:\\w\\$'";

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
	public static String executeBashCommand(String command) {
	    boolean success = false;
	    String output="";
	    System.out.println("Executing BASH command:   " + command);
	    Runtime r = Runtime.getRuntime();
	    // Use bash -c so we can handle things like multi commands separated by ; and
	    // things like quotes, $, |, and \. My tests show that command comes as
	    // one argument to bash, so we do not need to quote it to make it one thing.
	    // Also, exec may object if it does not have an executable file as the first thing,
	    // so having bash here makes it happy provided bash is installed and in path.
	    String[] commands = {"bash", "-c", command};
	    try {
	      Process p = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", command });
	        //ProcessBuilder p = new ProcessBuilder("/bin/bash", scriptPath + script).start();

	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	            System.out.println(line);
	            output+=line+"\n";
	        }
	        b.close();

	        while ((line = err.readLine()) != null) {
	            System.out.println(line);
	            output+=line+"\n";
	        }
	        err.close();
	        p.waitFor();

	        success = true;
	    } catch (Exception e) {
	        System.err.println("[SERVER] Failed to execute bash command: " + command);
	        e.printStackTrace();
	       output+=e.getLocalizedMessage();
	    }
	    return output;
	}
	public  static String getTerminalGreeting()
	{
		String str="";
		 String sysEnvStr = System.getenv("PS1");
		return sysEnvStr;

	}

}

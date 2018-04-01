package client.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
	  public static void main(String[] args) throws Exception {
	    String[] command = { "./home/mike/myscript.sh", "key", "ls -t | tail -n 1" };
	    Process process = Runtime.getRuntime().exec(command);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
	        process.getInputStream()));
	    String s;
	    while ((s = reader.readLine()) != null) {
	      System.out.println("Script output: " + s);
	    }
	  }
	}
package server.model;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommandModel {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	private List<Date> dates = new ArrayList<>();
	private String command = new String();
	private Week w;
	private Months m;
	private CommandType type;
	private RepeatType repeat;


}

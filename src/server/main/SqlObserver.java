package server.main;

import java.sql.ResultSet;

public interface SqlObserver {

	public void reaction(ResultSet res);

}

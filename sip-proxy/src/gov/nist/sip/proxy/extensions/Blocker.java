package gov.nist.sip.proxy.extensions;

import java.sql.*;

public class Blocker 
{
    private ConnectToDB database;

    public Blocker(ConnectToDB database) 
	{
        this.database = database;
    }

    public boolean checkBlock(String Caller, String Callee)
	{
        boolean res = false;
        PreparedStatement stmt = null;
        String sqlQuery = "SELECT * FROM blocks WHERE blocker=? AND blocked=?";
		
        try 
		{
            Connection conn= database.start();
            stmt = conn.prepareStatement(sqlQuery);
            stmt.setString(1, Callee);
            stmt.setString(2, Caller);
            ResultSet queryRS = stmt.executeQuery();
																		
			// Check db for corresponding entries
			// If the query returns anything, the caller is blocked
            if (queryRS.first()) res = true;
																
            stmt.close();
			database.close(conn);
			
            return res;
        }
        catch (SQLException e) 
		{
            e.printStackTrace();
            return res;
        }
    }

}

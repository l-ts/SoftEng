package gov.nist.sip.proxy.extensions;

import java.sql.*;

public class Biller {
	
	private String caller,callee;
	private ConnectToDB database;

	public void setUsers(String caller, String callee){
		this.caller=caller;
		this.callee=callee;
	}
	
	public Biller(ConnectToDB database){
		this.database = database;

	}
	
	public BillingStrategyObs decide(){
		BillingStrategyObs obs;

		// apply standard billing
		// can easily be extended
		obs = new BillingStrategyObs(new DefaultStrategy());

		return obs;
	}
	
	public void updateCost(String user, float cost){
        Connection conn = database.start();
        PreparedStatement stmt = null;
        String sql = "SELECT total_cost FROM billing WHERE user=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()){
                cost = cost + rs.getFloat("total_cost");
            }
            stmt.close();
            sql = "UPDATE billing SET total_cost=? WHERE user=?";
            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, cost);
            stmt.setString(2, user);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
			
        e.printStackTrace();
    }
        database.close(conn);
    }
	
	public void bill(long duration){
		BillingStrategyObs obs = this.decide();

        float bill = obs.executeStrategy(duration);

        updateCost(this.caller, bill);
	}
	
}


package gov.nist.sip.proxy.extensions;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

public class Forwarder {
    private ConnectToDB database;

    public Forwarder(ConnectToDB database) {
        this.database = database;
    }

    public String getTarget(String caller, String callee){
        String target;
        PreparedStatement stmt = null;
        boolean query_flag = true;
        try {
            Connection conn= database.start();
            while (query_flag){
                String sql = "SELECT toUser FROM forwards WHERE fromUser=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, callee);
                ResultSet rs = stmt.executeQuery();
                if (rs.first()) {
					callee = rs.getString("toUser");
                }
				else 
				{
					query_flag=false;
				}
            }
            stmt.close();

            database.close(conn);
			
			return(callee);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return ("exception\n");
        }

    }

    public Request forwardRequest(Request request, AddressFactory adfactory, HeaderFactory headfactory, String caller, String callee){
        String target = getTarget(caller,callee);
		
        SipURI test = (SipURI) request.getRequestURI();
        try {
            test.setUser(target);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Address newAddress = adfactory.createAddress(test) ;
        ToHeader toHeader;
        try {
            toHeader = headfactory.createToHeader(newAddress, null);
            Request newRequest = (Request) request.clone();
            newRequest.setHeader(toHeader);
            return newRequest;
        } catch (ParseException e) {
            e.printStackTrace();
            return request;
        }

    }


}

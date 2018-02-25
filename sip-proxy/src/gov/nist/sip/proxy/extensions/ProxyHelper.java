package gov.nist.sip.proxy.extensions;


import java.io.Console;
import java.sql.*;
import java.text.ParseException;

import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import gov.nist.sip.proxy.Proxy;



public class ProxyHelper {

    Blocker blocker;
    ConnectToDB database;
    Forwarder forwarder;
    Biller biller;
	
    private long startTime;
	
	
    public ProxyHelper() {
        this.database = new ConnectToDB();
        this.blocker = new Blocker(database);
        this.forwarder = new Forwarder(database);
        this.biller = new Biller(database);
    }			
		
			
						
    public String getUserNamefromHeader(String header)
    {
        String[] parts = header.split("@");
        String[] parts2 = parts[0].split("<");
        String[] parts3 = parts2[1].split(":");
        return parts3[1];
    }
								
    public String getCallerName(Request request){
        String head = request.getHeader(FromHeader.NAME).toString();
        String callerName = getUserNamefromHeader(head);
        return callerName;
    }
							
    public String getCalleeName(Request request){
        String head = request.getHeader(ToHeader.NAME).toString();
        String calleeName = getUserNamefromHeader(head);
        return calleeName;
    }
			

				
	
    public void beginCallTimeCount(Request request){
        startTime = System.nanoTime();
        String caller = getCallerName(request);
        String callee = getCalleeName(request);
        biller.setUsers(caller, callee);
    }

    public void endCallTimeCount(){
        long duration = (System.nanoTime() - startTime) / 1000000;
        if (duration > 1200) { duration = duration - 1200; }
        System.out.println("Call time = " + duration + " ms");
		
		biller.bill(duration);
    }

			
					

					
    public Request forwardRequest(Request request, AddressFactory adfactory, HeaderFactory headfactory){
		String caller = getCallerName(request);
        String callee = getCalleeName(request);
		return forwarder.forwardRequest(request, adfactory, headfactory, caller, callee);
    }

				
				
					
    public boolean isBlocked(Request request){
        String caller = getCallerName(request);
        String callee = getCalleeName(request);
        return blocker.checkBlock(caller, callee);
    }



}

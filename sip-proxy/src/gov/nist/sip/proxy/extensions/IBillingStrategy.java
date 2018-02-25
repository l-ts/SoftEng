package gov.nist.sip.proxy.extensions;

public interface IBillingStrategy {
	
	public float calculateCost(long time);
	
	
}

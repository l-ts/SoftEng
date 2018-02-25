package gov.nist.sip.proxy.extensions;

public class DefaultStrategy implements IBillingStrategy{

	@Override
	public float calculateCost(long time){
		/* 
		 * 0-20s :free
		 * 20-120s : 0.05 euro/s
		 * >120s : 0.01 euro/s
		 */
		float sec = time/1000;
		float bill;
		if (sec<=20){
			bill = 0.0f;
		}
		else if (sec<120){
			bill = (sec-20)*0.05f;
		}
		else{
			bill = (sec-20)*0.01f;
		}
		return bill;
	}

}

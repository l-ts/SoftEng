package gov.nist.sip.proxy.extensions;

public class BillingStrategyObs {

   private IBillingStrategy strategy;

   public BillingStrategyObs(IBillingStrategy strategy){
      this.strategy = strategy;
   }

   public float executeStrategy(long time){
      return strategy.calculateCost(time);
   }
}

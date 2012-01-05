package org.jboss.as.quickstarts.payment.beans;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.events.PaymentTypeEnum;
import org.jboss.as.quickstarts.payment.qualifiers.Credit;
import org.jboss.as.quickstarts.payment.qualifiers.Debit;


@Named
@SessionScoped

public class PaymentBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Inject
	 private Logger log;

	//Events producers
	@Inject
	@Credit 
	Event<PaymentEvent> creditEventProducer;
	
	@Inject
	@Debit
	Event<PaymentEvent> debitEventProducer;
	
	private BigDecimal amount= new BigDecimal(10.0);
			
	private String paymentOption=PaymentTypeEnum.DEBIT.toString();
	
	
	
	
	//Pay Action
	public String pay(){

		PaymentEvent currentEvtPayload = new PaymentEvent();
		currentEvtPayload.setType(PaymentTypeEnum.fromString(paymentOption));
		currentEvtPayload.setAmount(amount);
		currentEvtPayload.setDatetime(new Date());
		
		switch (currentEvtPayload.getType()) {
		case DEBIT:

			debitEventProducer.fire(currentEvtPayload);
			
			break;
		case CREDIT:
			creditEventProducer.fire(currentEvtPayload);

			break;

		default: log.severe("invalid payment option");
			break;
		}
		
		//paymentAction
		
	
		return "index";
	}
	
	
	//Reset Action
	public void reset()
	{
		amount= null;
		paymentOption="";
	 
	}
	
	
	
	public Event<PaymentEvent> getCreditEventLauncher() {
		return creditEventProducer;
	}


	public void setCreditEventLauncher(Event<PaymentEvent> creditEventLauncher) {
		this.creditEventProducer = creditEventLauncher;
	}


	public Event<PaymentEvent> getDebitEventLauncher() {
		return debitEventProducer;
	}


	public void setDebitEventLauncher(Event<PaymentEvent> debitEventLauncher) {
		this.debitEventProducer = debitEventLauncher;
	}


	public String getPaymentOption() {
		return paymentOption;
	}


	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	


}

package org.camunda.bpm.carInsurance.bpmn;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class RejectApplication implements JavaDelegate {

	//private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	  
	public void execute(DelegateExecution execution) throws Exception {
		  
		
	   /* LOGGER.info("\n\n  ... LoggerDelegate invoked by "
	            + "processDefinitionId=" + execution.getProcessDefinitionId()
	            + ", activtyId=" + execution.getCurrentActivityId()
	            + ", activtyName='" + execution.getCurrentActivityName() + "'"
	            + ", processInstanceId=" + execution.getProcessInstanceId()
	            + ", businessKey=" + execution.getProcessBusinessKey()
	            + ", executionId=" + execution.getId()
	            + " \n\n");*/
	    System.out.println("Application has been rejected!\n");
	  }
}

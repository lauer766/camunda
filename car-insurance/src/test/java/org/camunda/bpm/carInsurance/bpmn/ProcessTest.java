package org.camunda.bpm.carInsurance.bpmn;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.init;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import org.camunda.bpm.engine.task.Task;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.FoxFailedJobParseListener;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTest {

	
	protected ProcessEngine createProcessEngineProgramatically() {
		  StandaloneInMemProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration();
		  processEngineConfiguration.setCustomPostBPMNParseListeners(Arrays.asList(new BpmnParseListener[]{new FoxFailedJobParseListener()}));
		  return processEngineConfiguration.buildProcessEngine();
		}
	
	@Rule
	public ProcessEngineRule rule = new ProcessEngineRule(createProcessEngineProgramatically());
	
	private static final String PROCESS_DEFINITION_KEY = "car-insurance";

	
	
	@Before
	public void setup() {
		init(rule.getProcessEngine());
	}
	
	@Test
	@Deployment(resources = {"car-insurance.bpmn", "rating.dmn", "risks.dmn"})
	public void testDeployment() {
		//check for any exceptions
	}
	
	
	//test accept
		@Test
		@Deployment(resources = {"car-insurance.bpmn", "rating.dmn", "risks.dmn"})
		public void testApplicationAccepted() {
					
			//ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
			ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
			
			// Then it should be active
		    assertThat(processInstance).isActive();
		    // And it should be the only instance
		    assertThat(processInstanceQuery().count()).isEqualTo(1);
		    // And there should exist just a single task within that process instance
		    assertThat(task(processInstance)).isNotNull();
		    
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("age", 20);
			variables.put("name", "Gipsz Jakab");
			variables.put("brand", "Skoda");
			variables.put("licensePlate", "HGS-007");
			variables.put("category", "Standard");
			variables.put("insuranceType", "Bronze");
			variables.put("carAge", 10);
			variables.put("hadAccident", false);
			variables.put("stolen", false);
			
			TaskService taskService = rule.getTaskService();
		    Task task = taskService.createTaskQuery().singleResult();
			taskService.complete(task.getId(), variables);
			
		    // Then the process instance should be ended
		    assertThat(processInstance).isEnded();
			
		}
		
		//test manual acceptance
		@Test
		@Deployment(resources = {"car-insurance.bpmn", "rating.dmn", "risks.dmn"})
		public void testManualAcception() {

			//ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
			ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
			
			// Then it should be active
		    assertThat(processInstance).isActive();
		    // And it should be the only instance
		    assertThat(processInstanceQuery().count()).isEqualTo(1);
		    // And there should exist just a single task within that process instance
		    assertThat(task(processInstance)).isNotNull();
		    
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("age", 21);
			variables.put("name", "Gipsz Jakab");
			variables.put("brand", "Skoda");
			variables.put("licensePlate", "HGS-007");
			variables.put("category", "Luxury");
			variables.put("insuranceType", "Silver");
			variables.put("carAge", 10);
			variables.put("hadAccident", false);
			variables.put("stolen", false);
			
			TaskService taskService = rule.getTaskService();
		    Task task = taskService.createTaskQuery().singleResult();
			taskService.complete(task.getId(), variables);
			
			assertThat(processInstance).task()
			.hasDefinitionKey("manualAcceptance");
			
			complete(task(), withVariables("manualDecision", Boolean.TRUE));
			
		    assertThat(processInstance).isEnded();
		}
		
		
		//test manualreject
		@Test
		@Deployment(resources = {"car-insurance.bpmn", "rating.dmn", "risks.dmn"})
		public void testManualRejection() {

			//ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
			ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
			
			// Then it should be active
		    assertThat(processInstance).isActive();
		    // And it should be the only instance
		    assertThat(processInstanceQuery().count()).isEqualTo(1);
		    // And there should exist just a single task within that process instance
		    assertThat(task(processInstance)).isNotNull();
		    
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("age", 20);
			variables.put("name", "Gipsz Jakab");
			variables.put("brand", "Skoda");
			variables.put("licensePlate", "HGS-007");
			variables.put("category", "Luxury");
			variables.put("insuranceType", "Gold");
			variables.put("carAge", 10);
			variables.put("hadAccident", true);
			variables.put("stolen", false);
			
			TaskService taskService = rule.getTaskService();
		    Task task = taskService.createTaskQuery().singleResult();
			taskService.complete(task.getId(), variables);
			
			assertThat(processInstance).task()
			.hasDefinitionKey("manualAcceptance");
			
			complete(task(), withVariables("manualDecision", Boolean.FALSE));
			
		    assertThat(processInstance).isEnded();
			
		}
		
		//test stolencar
		@Test
		@Deployment(resources = {"car-insurance.bpmn", "rating.dmn", "risks.dmn"})
		public void testStolenCar() {

			//ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
			ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
			
			// Then it should be active
		    assertThat(processInstance).isActive();
		    // And it should be the only instance
		    assertThat(processInstanceQuery().count()).isEqualTo(1);
		    // And there should exist just a single task within that process instance
		    assertThat(task(processInstance)).isNotNull();
		    
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("age", 20);
			variables.put("name", "Gipsz Jakab");
			variables.put("brand", "Skoda");
			variables.put("licensePlate", "ABC-001");
			variables.put("category", "Luxury");
			variables.put("insuranceType", "Gold");
			variables.put("carAge", 10);
			variables.put("hadAccident", true);
			variables.put("stolen", false);
			
			TaskService taskService = rule.getTaskService();
		    Task task = taskService.createTaskQuery().singleResult();
			taskService.complete(task.getId(), variables);
			
			//assertThat(processInstance).task()
			//.hasDefinitionKey("manualAcceptance");
			//complete(task(), withVariables("manualDecision", Boolean.FALSE));
			
		    assertThat(processInstance).isEnded();
			
		}
		

}

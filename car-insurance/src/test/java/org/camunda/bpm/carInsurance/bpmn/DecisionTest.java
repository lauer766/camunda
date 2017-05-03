package org.camunda.bpm.carInsurance.bpmn;

import static org.junit.Assert.*;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;
import org.junit.*;
import org.junit.rules.TestWatcher;

public class DecisionTest {

	@Rule
	//define DMN Engrine Rule
	public DmnEngineRule rule = new DmnEngineRule();
	
	@Test
	public void DMNTableTest() throws FileNotFoundException {
		
		//Get the DMN Engine
		DmnEngine dmnEngine = rule.getDmnEngine();
		
		//Get the DMN table as InputStream
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("rating.dmn");
		//Parse the Decision with the right table key
		DmnDecision decision = dmnEngine.parseDecision("rating", inputStream);
		
		//Put the input data
		VariableMap variables = Variables.createVariables()
				.putValue("age", 18)
				.putValue("name", "Gipsz Jakab")
				.putValue("brand", "BMW")
				.putValue("licensePlate", "BDE-001")
				.putValue("category", "Luxury")
				.putValue("insuranceType", "Gold")
				.putValue("carAge", 10);
		
		//Get the actual results from the DMN table
		DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decision, variables);
		//Get the result
		System.out.println("Rating:" + results.getSingleEntry());
				
		InputStream inputStream2 = this.getClass().getClassLoader().getResourceAsStream("risks.dmn");
		DmnDecision decision2 = dmnEngine.parseDecision("risks", inputStream2);
		
		//Put the input data
		VariableMap variables2 = Variables.createVariables()
				.putValue("rating", results.getSingleEntry())
				.putValue("hadAccident", true);
		
		DmnDecisionTableResult results2 = dmnEngine.evaluateDecisionTable(decision2, variables2);
		
		System.out.println("Risk level: " + results2.getSingleEntry());
	}
	
}

package org.camunda.bpm.carInsurance.bpmn;

import java.io.File;
import java.util.Scanner;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class LicensePlateChecker implements JavaDelegate {

	public void execute(DelegateExecution execute) throws Exception {
		
		String licensePlate = (String) execute.getVariable("licensePlate");
		
		String txtFile = "C:\\Users\\Lauer János\\workspace\\car-insurance\\src\\main\\resources\\licensePlates.txt";
		
		File file = new File(txtFile);
		final Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
		   final String lineFromFile = scanner.nextLine();
		   if(lineFromFile.contains(licensePlate)) { 
		       execute.setVariable("stolen", true);
		       System.out.println(licensePlate + " is a stolen car! ");
		       break;
		   }
		}
		
	}

}

package it.prova.gestionesatelliti.validations;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.prova.gestionesatelliti.model.Satellite;

public class Validazioni implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Satellite.class.equals(clazz);
	}

	@Override
	public  void validate(Object target, Errors errors) {
		Satellite satellite = (Satellite) target;
		
		if(satellite.getDataRientro().before(satellite.getDataLancio())) {
			errors.rejectValue("message" ,"Inserimento Date errato,"
					+ " la data di lancio deve essere prima della data di rientro.");
			
		}
		
		if(satellite.getStato().equals("FISSO") || satellite.getStato().equals("IN_MOVIMENTO")
				&& satellite.getDataRientro() != null) {
			errors.reject("Impossibile inserire data Rientro per Satellite ancora"
					+ "  in Orbita.");
		}
		
	}

	
	
	
}

package it.prova.gestionesatelliti.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;


public interface SatelliteService {

	public List<Satellite> listAllElements();

	public Satellite caricaSingoloElemento(Long id);
	
	public void aggiorna(Satellite satelliteInstance);

	public void inserisciNuovo(Satellite satelliteInstance);

	public void rimuovi(Long idSatelliteInstance);
	
	public List<Satellite> findByExample(Satellite example);
	
	public List<Satellite> FindAllByDataLancioBeforeAndStatoNotLike(Date data,StatoSatellite stato) throws ParseException;
	
	public List<Satellite> FindAllByDataRientroIsNullAndStatoLike(StatoSatellite stato) throws ParseException;
	
	public List<Satellite> FindAllByDataLancioBeforeAndStatoLike(Date data,StatoSatellite stato) throws ParseException;
	
}

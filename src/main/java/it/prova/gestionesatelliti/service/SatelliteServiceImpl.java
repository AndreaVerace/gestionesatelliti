package it.prova.gestionesatelliti.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	private SatelliteRepository satelliteRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) satelliteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return satelliteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		satelliteRepository.save(satelliteInstance);
		
	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		satelliteRepository.save(satelliteInstance);
		
	}

	@Override
	@Transactional
	public void rimuovi(Long idSatelliteInstance) {
		satelliteRepository.deleteById(idSatelliteInstance);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite example) {
		Specification<Satellite> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")), "%" + example.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + example.getCodice().toUpperCase() + "%"));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));

			if (example.getDataLancio() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancio"), example.getDataLancio()));
			
			if (example.getDataRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataRientro"), example.getDataRientro()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return satelliteRepository.findAll(specificationCriteria);
	}

	@Override
	public List<Satellite> FindAllByDataLancioBeforeAndStatoNotLike(Date data, StatoSatellite stato) throws ParseException {
		return satelliteRepository.findAllByDataLancioBeforeAndStatoNotLike(data,stato);
	}

	@Override
	public List<Satellite> FindAllByDataRientroIsNullAndStatoLike(StatoSatellite stato) throws ParseException{
		return satelliteRepository.findAllByDataRientroIsNullAndStatoLike(stato);
	}

	@Override
	public List<Satellite> FindAllByDataLancioBeforeAndStatoLike(Date data, StatoSatellite stato) throws ParseException {
		return satelliteRepository.findAllByDataLancioBeforeAndStatoLike(data,stato);
	}

}

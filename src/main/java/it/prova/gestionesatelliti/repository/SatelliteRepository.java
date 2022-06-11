package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {

	List<Satellite> findAllByDataLancioBeforeAndStatoNotLike(Date data,StatoSatellite stato);
	
	List<Satellite> findAllByDataRientroIsNullAndStatoLike(StatoSatellite stato);
	
	List<Satellite> findAllByDataLancioBeforeAndStatoLike(Date data,StatoSatellite stato);
	
}

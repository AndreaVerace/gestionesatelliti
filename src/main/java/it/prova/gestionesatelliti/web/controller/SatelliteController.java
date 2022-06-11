package it.prova.gestionesatelliti.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;
	
	
	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}
	
	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}
	
	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs,Model model) {
		
		String errorCodeData = "Impossibile che la data di rientro sia prima della data di lancio";
		String errorCodeStato = "Impossibile che un satellite in orbita abbia una data di rientro";
		
		if(satellite.getDataRientro() != null)
			if(satellite.getDataRientro().before(satellite.getDataLancio())) {
				model.addAttribute("messageData", errorCodeData);
				return "satellite/insert";
				
			}
		
		if(satellite.getDataRientro() != null) 
				if(satellite.getStato().equals(satellite.getStato().FISSO) 
				|| satellite.getStato().equals(satellite.getStato().IN_MOVIMENTO)) {
					model.addAttribute("messageStato", errorCodeStato);
					return "satellite/insert";
				}
		
		
		if (result.hasErrors()) {
			return "satellite/insert";
		}
		
		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite,Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam(required=true) Long idSatelliteDaEliminare,RedirectAttributes redirectAttrs,Model model) {
		
		String errorCode = "Impossibile eliminare satellite che è ancora in orbita.";
		
		if(satelliteService.caricaSingoloElemento(idSatelliteDaEliminare).getDataRientro() == null 
				|| !satelliteService.caricaSingoloElemento(idSatelliteDaEliminare).getStato()
				.equals(satelliteService.caricaSingoloElemento(idSatelliteDaEliminare).getStato().DISATTIVATO)) {
			model.addAttribute("messageError",errorCode);
			return "satellite/delete";
		}
		
		satelliteService.rimuovi(idSatelliteDaEliminare);
		
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/edit/{idSatellite}")
	public String edit(@PathVariable(required=true) Long idSatellite,Model model) {
		model.addAttribute("edit_satellite_attr",satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/edit";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("edit_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs,Model model) {

		String errorCodeData = "Impossibile che la data di rientro sia prima della data di lancio";
		String errorCodeStato = "Impossibile che un satellite in orbita abbia una data di rientro";
		
		if(satellite.getDataRientro() != null)
			if(satellite.getDataRientro().before(satellite.getDataLancio())) {
				model.addAttribute("messageData", errorCodeData);
				return "satellite/edit";
				
			}
		
		if(satellite.getDataRientro() != null) 
				if(satellite.getStato().equals(satellite.getStato().FISSO) 
				|| satellite.getStato().equals(satellite.getStato().IN_MOVIMENTO)) {
					model.addAttribute("messageStato", errorCodeStato);
					return "satellite/edit";
				}
		
		if (result.hasErrors())
			return "satellite/edit";

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";

	}
	
	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}
	
	@GetMapping("/lancia/{idSatellite}")
	public String lancia(@PathVariable(required = true) Long idSatellite,Model model,RedirectAttributes redirectAttrs) {
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		 Date today = calendar.getTime();
		
		Satellite satelliteDaLanciare = satelliteService.caricaSingoloElemento(idSatellite);
		satelliteDaLanciare.setStato(StatoSatellite.IN_MOVIMENTO);
		satelliteDaLanciare.setDataLancio(today);
		if(satelliteDaLanciare.getDataRientro() != null) {
			satelliteDaLanciare.setDataRientro(null);
		}
		
		satelliteService.aggiorna(satelliteDaLanciare);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
		
	}
	
	@GetMapping("/rientra/{idSatellite}")
	public String rientra(@PathVariable(required = true) Long idSatellite,Model model,RedirectAttributes redirectAttrs) {
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		 Date today = calendar.getTime();
		 
		 
		 Satellite satelliteDaFarRientrare = satelliteService.caricaSingoloElemento(idSatellite);
		 
		 if(!satelliteDaFarRientrare.getStato().equals(satelliteDaFarRientrare.getStato().DISATTIVATO)) {
			 satelliteDaFarRientrare.setStato(StatoSatellite.DISATTIVATO);
			 satelliteDaFarRientrare.setDataRientro(today);
		 }
		 
		 satelliteService.aggiorna(satelliteDaFarRientrare);
		 
		 redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
			return "redirect:/satellite";
	}
	
	@GetMapping("/listLanciatiNonDisattivatiServlet")
	public String listLanciatiNonDisattivati(Model model) throws ParseException {
		List<Satellite> results = satelliteService.FindAllByDataLancioBeforeAndStatoNotLike(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-11"), StatoSatellite.DISATTIVATO);
		model.addAttribute("satellite_listLanciatiNonDisattivati_attribute", results);
		return "satellite/listLanciatiNonDisattivati";
	}
	
	@GetMapping("/listDisattivatiNonRientratiServlet")
	public String listDisattivatiNonRientrati(Model model) throws ParseException {
		List<Satellite> results = satelliteService.FindAllByDataRientroIsNullAndStatoLike(StatoSatellite.DISATTIVATO);
		model.addAttribute("satellite_listDisattivatiNonRientrati_attribute", results);
		return "satellite/listDisattivatiNonRientrati";
	}
	
	@GetMapping("/listOrbitaFissiServlet")
	public String listOrbitaFissi(Model model) throws ParseException {
		List<Satellite> results = satelliteService.FindAllByDataLancioBeforeAndStatoLike(new SimpleDateFormat("yyyy-MM-dd").parse("2012-06-11"),StatoSatellite.FISSO);
		model.addAttribute("satellite_listOrbitaFissi_attribute", results);
		return "satellite/listOrbitaFissi";
	}
	
}

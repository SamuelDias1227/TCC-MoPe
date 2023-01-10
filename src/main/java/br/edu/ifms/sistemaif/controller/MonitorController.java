package br.edu.ifms.sistemaif.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Disciplina;
import br.edu.ifms.sistemaif.modelo.Estudante;
import br.edu.ifms.sistemaif.modelo.Monitor;
import br.edu.ifms.sistemaif.monitor.dto.MonitorDTO;
import br.edu.ifms.sistemaif.repository.CargoRepository;
import br.edu.ifms.sistemaif.repository.DisciplinaRepository;
import br.edu.ifms.sistemaif.repository.EstudanteRepository;
import br.edu.ifms.sistemaif.repository.MonitorRepository;

@Controller
@RequestMapping("/monitor")
public class MonitorController {
	
	@Autowired
	MonitorRepository monitorRepository;
	
	@Autowired
	EstudanteRepository estudanteRepository;
	
	@Autowired
	private DisciplinaRepository disciplinaRepository;
	
	@Autowired
	CargoRepository cargoRepository;
	
	@GetMapping("/novo")
	public String adicionarMonitor(Model model) {
		model.addAttribute("monitorDTO", new MonitorDTO());
		model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
		return "/publica-criar-monitor";
	}
	
	@PostMapping("/salvar")
	public String salvarMonitor(MonitorDTO monitorDTO, Model model, RedirectAttributes attributes, @RequestParam(value = "dds", required = false) int[] dds) {
		
		
		model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
		Disciplina materia = null;
		
		if (dds == null) {
			model.addAttribute("mensagemDisciplina", "Pelo menos uma disciplina deve ser informada");
			return "/publica-criar-monitor";
		} else {
			for (int i = 0; i < dds.length; i++) {
				long idDisciplina = dds[i];
				Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(idDisciplina);
				if (disciplinaOptional.isPresent()) {
					materia = disciplinaOptional.get();
				}
			}
		}
		System.out.println("Aqui em cima!");
		if (monitorDTO.getRa() == null){
			model.addAttribute("raNaoEncontrado", "Esse Ra não está cadastrado no sistema");
			return "/publica-criar-monitor";
		}
		Optional<Estudante> estudanteOpt = estudanteRepository.findByRa(monitorDTO.getRa());
		System.out.println("Aqui em baixa!");
		Estudante estudante = null;
		if (estudanteOpt.isPresent()) {
			estudante =  estudanteOpt.get();
			if (estudante != null && materia != null) {
				Monitor monitor = new Monitor();
				monitor.setDisciplina(materia);
				// Busca o cargo de MONITOR
				Cargo cargo = cargoRepository.findByNome("MONITOR");
				Cargo cargo2 = cargoRepository.findByNome("ESTUDANTE");
				List<Cargo> cargos = new ArrayList<Cargo>();
				cargos.add(cargo);
				cargos.add(cargo2);
				estudante.setCargos(cargos); // associa os cargos de MONITOR e ESTUDANTE ao estudante
				monitor.setEstudante(estudante);
				monitor.setAtivo(true);

				LocalDate localDate = LocalDate.now();
				int ano = localDate.getYear();
				int mes = localDate.getMonthValue();
				String semestre = "";
				if (mes >= 1 && mes < 7){
					semestre = "1";
				}else if (mes >= 6 && mes <= 12){
					semestre = "2";
				}

				monitor.setSemestre(""+ano+"."+semestre+"");
				monitorRepository.save(monitor);
				attributes.addFlashAttribute("mensagem", "Monitor salvo com sucesso!");
			}
		}else {
			model.addAttribute("raNaoEncontrado", "Esse Ra não está cadastrado no sistema");
			return "/publica-criar-monitor";
		}
		return "redirect:/monitor/novo";
	}
	
	// funcão apenas pode ser usanda pelo admin
	@RequestMapping("/admin/listar")
	public String listarMonitor(Model model) {
		model.addAttribute("monitores", monitorRepository.findAll());
		return "/auth/admin/admin-listar-monitor";
	}
	
	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/admin/apagar/{id}")
	public String deleteMonitor(@PathVariable("id") long id, Model model) {
		Monitor monitor = monitorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		monitor.setAtivo(false);
		
		Estudante estudante = monitor.getEstudante();
		
		// Busca o cargo de ESTUDANTE
		Cargo cargo = cargoRepository.findByNome("ESTUDANTE");
		List<Cargo> cargos = new ArrayList<Cargo>();
		cargos.add(cargo);
		estudante.setCargos(cargos); // associa o cargo de ESTUDANTE ao estudante
		estudanteRepository.save(estudante);
		monitorRepository.save(monitor);
		return "redirect:/monitor/admin/listar";
	}
}
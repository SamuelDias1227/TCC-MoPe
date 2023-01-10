package br.edu.ifms.sistemaif.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Estudante;
import br.edu.ifms.sistemaif.modelo.Horario;
import br.edu.ifms.sistemaif.modelo.Monitor;
import br.edu.ifms.sistemaif.modelo.Presenca;
import br.edu.ifms.sistemaif.modelo.Professor;
import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.EstudanteRepository;
import br.edu.ifms.sistemaif.repository.HorarioRepository;
import br.edu.ifms.sistemaif.repository.MonitorRepository;
import br.edu.ifms.sistemaif.repository.PresencaRepository;
import br.edu.ifms.sistemaif.repository.ProfessorRepository;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Controller
@RequestMapping("/presenca")
public class PresencaController {
	
	@Autowired
	private PresencaRepository presencaRepository;
	
	@Autowired
	private HorarioRepository horarioRepository;
	
	@Autowired
	private EstudanteRepository estudanteRepository;
	
	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private MonitorRepository monitorRepository;

	@GetMapping("/salvar/{idHorario}/{idLog}")
	public String salvarPresenca(@PathVariable("idHorario") long idHorario, @PathVariable("idLog") long idLog, RedirectAttributes attributes) {
		
		Horario horario = horarioRepository.findById(idHorario).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idHorario));
		Estudante estudnate = estudanteRepository.findById(idLog).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		Professor pofessor = horario.getProfessor();
		
		Presenca presenca = new Presenca();
		presenca.setProfessor(pofessor);
		presenca.setEstudante(estudnate);
		presenca.setHorario(horario);
		presenca.setData(new Date());
		
		presencaRepository.save(presenca);
		
		return "redirect:/disciplina/student/listar";
    }

	@GetMapping("/salvar/presenca/monitor/{idHorario}/{idLog}")
	public String salvarPresencaMonitor(@PathVariable("idHorario") long idHorario, @PathVariable("idLog") long idLog, RedirectAttributes attributes) {
		
		Horario horario = horarioRepository.findById(idHorario).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idHorario));
		Estudante estudnate = estudanteRepository.findById(idLog).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		Monitor monitor = horario.getMonitor();
		
		Presenca presenca = new Presenca();
		presenca.setMonitor(monitor);
		presenca.setEstudante(estudnate);
		presenca.setHorario(horario);
		presenca.setData(new Date());
		
		presencaRepository.save(presenca);
		
		return "redirect:/disciplina/student/listar";
    }
	
	// funcao que lista apenas as disciplinas daquele professor ou daquele monitor
	@RequestMapping("/teacher/listar/{idLog}")
	public String teacherlistarPresenca(@PathVariable("idLog") long idLog, Model model) {
		Professor professor;
		Estudante estudante;
		Usuario usuario = usuarioRepository.findById(idLog).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		for (Cargo cargo: usuario.getCargos()){
			if (cargo.getNome().equals("PROFESSOR")){
				Optional<Professor> prof = professorRepository.findById(idLog);
				professor = prof.get();
				ArrayList<Presenca> presencas = new ArrayList<>();
				presencas.addAll(professor.getPresencas());
				model.addAttribute("presencas", presencas);
			} else if (cargo.getNome().equals("MONITOR")){
				Optional<Estudante> estudanteOpt = estudanteRepository.findById(idLog);
				estudante = estudanteOpt.get();
				ArrayList<Presenca> presencas = new ArrayList<>();
				List<Monitor> monitores = estudante.getMonitorias();
				for (Monitor monitor: monitores){
					if (monitor.isAtivo()){
						presencas.addAll(monitor.getPresencas());
						model.addAttribute("presencas", presencas);
					}
				}
			}
		}
		return "/auth/teacher/teacher-presencas";
	}
	
	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/teacher/confirmarPresenca/{idPresenca}/{idLog}")
	public String confirmarPresenca(@PathVariable("idPresenca") long idPresenca, @PathVariable("idLog") long idLog, Model model) {
		Presenca presenca = presencaRepository.findById(idPresenca)
				.orElseThrow(() -> new IllegalArgumentException("id inválido:" + idPresenca));

		if (presenca.getConfirmacao() == false){
			System.out.println("Aqui dentro do if felse");
			presenca.setConfirmacao(true);
		}else if (presenca.getConfirmacao() == true){
			System.out.println("Aqui dentro do if true");
			presenca.setConfirmacao(false);
		}

		presencaRepository.save(presenca);
		return "redirect:/presenca/teacher/listar/" + idLog; //esse id tem que ser do professor
	}
}

package br.edu.ifms.sistemaif.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Disciplina;
import br.edu.ifms.sistemaif.modelo.Estudante;
import br.edu.ifms.sistemaif.modelo.Horario;
import br.edu.ifms.sistemaif.modelo.Monitor;
import br.edu.ifms.sistemaif.modelo.Professor;
import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.DisciplinaRepository;
import br.edu.ifms.sistemaif.repository.EstudanteRepository;
import br.edu.ifms.sistemaif.repository.HorarioRepository;
import br.edu.ifms.sistemaif.repository.MonitorRepository;
import br.edu.ifms.sistemaif.repository.ProfessorRepository;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Controller
@RequestMapping("/horario")
public class HorarioController {

    @Autowired
	private HorarioRepository horarioRepository;
    
    @Autowired
	private DisciplinaRepository disciplinaRepository;
    
    @Autowired
	private ProfessorRepository professorRepository;
    
    @Autowired
	private UsuarioRepository usuarioRepository;
    
    @Autowired
	private MonitorRepository monitorRepository;
    
    @Autowired
	private EstudanteRepository estudanteRepository;
	
	@GetMapping("/novo")
	public String adicionarHorario(Model model) {
		model.addAttribute("horario", new Horario());
		return "/auth/teacher/teacher-criar-horario";
	}
	
	// salva um horario vinculando-o ao professor ou monitor
	@PostMapping("/salvar/{id}")
	public String salvarHorario(@PathVariable("id") long id, @Valid Horario horario, BindingResult result, 
				RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "/auth/teacher-criar-horario";
		}
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		for(Cargo cargo: usuario.getCargos()) {
			if (cargo.getNome().equals("PROFESSOR")) {
				Professor professor = professorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
				horario.setProfessor(professor);
				horario.setStatus(true);
				horarioRepository.save(horario);
				attributes.addFlashAttribute("mensagem", "Horario salvo com sucesso, Professor!");
				break;
			}else if(cargo.getNome().equals("MONITOR")){
				Estudante estudante = estudanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
				Monitor monitorias = null;
				for (Monitor monitor: estudante.getMonitorias()) {
					if (monitor.isAtivo()) {
						monitorias = monitor;
					}
				}
				horario.setMonitor(monitorias);
				horario.setStatus(true);
				horarioRepository.save(horario);
				attributes.addFlashAttribute("mensagem", "Horario salvo com sucesso, Monitor!");
			}
		}	
		return "redirect:/horario/novo";
    }

	// funcao lista todos os horarios cadastrados para o admin apagar ou editar
	@RequestMapping("/admin/listar")
	public String listarHorario(Model model) {
		model.addAttribute("horarios", horarioRepository.findAll());
		return "/auth/admin/admin-listar-horario";
	}
	
	// funcao lista todos os horarios cadastrado de um determinado professor de uma determinada disciplina para ele confirmar presença
	@RequestMapping("/student/listar/{id}")
	public String studentListarHorario(@PathVariable("id") long id, Model model) {
		Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		List<Professor> professores = disciplina.getProfessores();
		ArrayList<Horario> horarios = new ArrayList<Horario>(); 
		for (Professor professor: professores) {
			for (Horario horario: professor.getHorarios()) {
				if(horario.getStatus() == true) {
					horarios.add(horario);
				}
			}
		}
		List<Monitor> monitores = disciplina.getMonitorias();
		ArrayList<Horario> hors = new ArrayList<Horario>(); 
		for (Monitor monitor: monitores) {
			if (monitor.isAtivo()) {
				for (Horario horario: monitor.getHorarios()) {
					if(horario.getStatus() == true) {
						hors.add(horario);
					}
				}
			}
		}
		model.addAttribute("horarios", horarios);
		model.addAttribute("hors", hors);
		return "/auth/student/student-listar-horario";
	}

	// funcao lista somente os horarios daquele professor para ele apagar ou editar
	@RequestMapping("/teacher/listar/{id}")
	public String teacherListarHorario(@PathVariable("id") long id, Model model) {
		Professor professor = professorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		ArrayList<Horario> horarios = new ArrayList<Horario>();
		for (Horario horario: professor.getHorarios()) {
			if(horario.getStatus() == true) {
				horarios.add(horario);
			}
		}
		model.addAttribute("horarios", horarios);

		return "/auth/teacher/teacher-listar-horario";
	}
	
	// funcao lista somente os horarios daquele monitor para ele apagar ou editar
	@RequestMapping("/monitor/listar/{id}")
	public String monitorListarHorario(@PathVariable("id") long id, Model model) {
		Estudante estudante = estudanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		Monitor monitorias = null;
		for (Monitor monitor: estudante.getMonitorias()) {
			if (monitor.isAtivo()) {
				monitorias = monitor;
			}
		}
		
		ArrayList<Horario> horarios = new ArrayList<Horario>();
		for (Horario horario: monitorias.getHorarios()) {
			if(horario.getStatus() == true) {
				horarios.add(horario);
			}
		}
		model.addAttribute("horarios", horarios);
		model.addAttribute("todosHorarios", horarioRepository.findAll());

		return "/auth/teacher/teacher-listar-horario";
	}
	
	// funcao deve poder ser usada pelo admin
	@GetMapping("/admin/apagar/{id}")
	public String deleteHorario(@PathVariable("id") long id, Model model) {
		Horario horario = horarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		horario.setStatus(false);
		horarioRepository.save(horario);
	    return "redirect:/horario/admin/listar";
	}
	
	// funcao pode ser usada pelo professor e monitor
	@GetMapping("/teacher/apagar/{idHorario}/{idLog}")
	public String deleteHorarioProfessor(@PathVariable("idHorario") long idHorario, @PathVariable("idLog") long idLog, Model model) {
		Horario horario = horarioRepository.findById(idHorario).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		horario.setStatus(false);
		horarioRepository.save(horario);

		Usuario usuario = usuarioRepository.findById(idLog).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		for(Cargo cargo: usuario.getCargos()) {
			if (cargo.getNome().equals("PROFESSOR")) {
				return "redirect:/horario/teacher/listar/" + idLog;
			}else if(cargo.getNome().equals("MONITOR")){
				return "redirect:/horario/monitor/listar/" + idLog;
			}
		}
	    return "redirect:/horario/teacher/listar/" + idLog;
	}

	// funcao deve poder ser usada tanto pelo professor, monitor e admin
	@GetMapping("/editar/{id}")
	public String editarHorario(@PathVariable("id") long id, Model model) {
		Optional<Horario> horarioVelho = horarioRepository.findById(id);
		if (!horarioVelho.isPresent()) {
			throw new IllegalArgumentException("horario inválido:" + id);
		}
		Horario horario = horarioVelho.get();
		model.addAttribute("horario", horario);
		return "/auth/teacher/teacher-alterar-horario";
	}

	// funcao deve poder ser usada tanto pelo professor, monitor e admin
	@PostMapping("/editar/{idHorario}/{idLog}")
	public String editarHorario(@PathVariable("idHorario") long idHorario, @PathVariable("idLog") long idLog, @Valid Horario horario, BindingResult result) {
		if (result.hasErrors()) {
			horario.setId(idHorario);
			return "/auth/teacher/teacher-alterar-horario";
		}
		Horario hour = horarioRepository.findById(idHorario).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idHorario));
		hour.setDia(horario.getDia());
		hour.setHorarioInicio(horario.getHorarioInicio());
		hour.setHorarioFinal(horario.getHorarioFinal());
		hour.setLocal(horario.getLocal());
		horarioRepository.save(hour);
		Usuario user = usuarioRepository.findById(idLog).orElseThrow(() -> new IllegalArgumentException("id inválido:" + idLog));
		for (Cargo cargo: user.getCargos()) {
			if (cargo.getNome().equals("PROFESSOR")) {
				return "redirect:/horario/teacher/listar/" + idLog;
			}else if(cargo.getNome().equals("MONITOR")) {
				return "redirect:/horario/monitor/listar/" + idLog;
			}else if(cargo.getNome().equals("ADMIN")){
				return "redirect:/horario/admin/listar";
			}
		}
		return "redirect:/horario/admin/listar";
	}

}
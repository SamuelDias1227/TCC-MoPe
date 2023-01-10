package br.edu.ifms.sistemaif.controller;

import java.util.ArrayList;
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

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Disciplina;
import br.edu.ifms.sistemaif.modelo.Estudante;
import br.edu.ifms.sistemaif.modelo.Monitor;
import br.edu.ifms.sistemaif.modelo.Professor;
import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.DisciplinaRepository;
import br.edu.ifms.sistemaif.repository.EstudanteRepository;
import br.edu.ifms.sistemaif.repository.MonitorRepository;
import br.edu.ifms.sistemaif.repository.ProfessorRepository;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
	private DisciplinaRepository disciplinaRepository;
    
    @Autowired
	private UsuarioRepository usuarioRepository;
    
    @Autowired
	private ProfessorRepository professorRepository;
    
    @Autowired
	private MonitorRepository monitorRepository;
    
    @Autowired
	private EstudanteRepository estudanteRepository;

    // funcao que lista todas as disciplinas para o admin apagar ou editar
	@RequestMapping("/admin/listar")
	public String listarDisciplina(Model model) {
		model.addAttribute("disciplinas", disciplinaRepository.findAll());
		return "/auth/admin/admin-listar-disciplina";
	}
	
	// funcao que lista todas as disciplinas para o estudante consutar seus horarios
	@RequestMapping("/student/listar")
	public String studentlistarDisciplina(Model model) {
		model.addAttribute("disciplinas", disciplinaRepository.findAll());
		return "/auth/student/student-index";
	}
	
	// funcao que lista apenas as disciplinas daquele professor ou daquele monitor
	@RequestMapping("/teacher/listar/{id}")
	public String teacherlistarDisciplina(@PathVariable("id") long id, Model model) {
		Professor professor;
		Optional<Usuario> user = usuarioRepository.findById(id);
		Usuario usuario = user.get();
		for(Cargo cargo: usuario.getCargos()) {
			if (cargo.getNome().equals("PROFESSOR")) {
				Optional<Professor> prof = professorRepository.findById(id);
				professor = prof.get();
				model.addAttribute("disciplinas", professor.getDisciplinas());
				break;
			}else if(cargo.getNome().equals("MONITOR")){
				Optional<Estudante> estudanteOpt = estudanteRepository.findById(id);
				ArrayList<Monitor> monitorias = new ArrayList<Monitor>();
				for (Monitor monitor: estudanteOpt.get().getMonitorias()) {
					if (monitor.isAtivo()) {
						monitorias.add(monitor);
					}
				}
				model.addAttribute("disciplinas", monitorias.get(monitorias.size()-1).getDisciplina());
				model.addAttribute("todasDisciplinas", disciplinaRepository.findAll());
			}
		}
		return "/auth/teacher/teacher-index";
	}

	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/admin/apagar/{id}")
	public String deleteDisciplina(@PathVariable("id") long id, Model model) {
		Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		disciplinaRepository.delete(disciplina);
	    return "redirect:/disciplina/admin/listar";
	}

	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/admin/editar/{id}")
	public String editarDisciplina(@PathVariable("id") long id, Model model) {
		Optional<Disciplina> disciplinaVelha = disciplinaRepository.findById(id);
		if (!disciplinaVelha.isPresent()) {
			throw new IllegalArgumentException("disciplina inválida:" + id);
		}
		Disciplina disciplina = disciplinaVelha.get();
		model.addAttribute("disciplina", disciplina);
		return "/auth/admin/admin-alterar-disciplina";
	}

	// funcão apenas pode ser usanda pelo admin
	@PostMapping("/admin/editar/{id}")
	public String editarDisciplina(@PathVariable("id") long id, @Valid Disciplina disciplina, BindingResult result) {
		if (result.hasErrors()) {
			disciplina.setId(id);
			return "/auth/admin/admin-alterar-disciplina";
		}
		Optional<Disciplina> disciplinaVelha = disciplinaRepository.findById(id);
		Disciplina disciplinaa = disciplinaVelha.get();
		disciplinaa.setNome(disciplina.getNome());
		disciplinaa.setDescricao(disciplina.getDescricao());
		disciplinaa.setEmenta(disciplina.getEmenta());
		disciplinaRepository.save(disciplinaa);
		return "redirect:/disciplina/admin/listar";
	}

}

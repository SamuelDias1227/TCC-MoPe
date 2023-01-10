package br.edu.ifms.sistemaif.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.sistemaif.modelo.Professor;
import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Disciplina;
import br.edu.ifms.sistemaif.repository.CargoRepository;
import br.edu.ifms.sistemaif.repository.DisciplinaRepository;
import br.edu.ifms.sistemaif.repository.ProfessorRepository;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private DisciplinaRepository disciplinaRepository;

	@Autowired
	private CargoRepository cargoRepository;

	@Autowired
	private BCryptPasswordEncoder criptografia;

	// inicio do metodo para criar um novo professor
	@GetMapping("/novo")
	public String adicionarProfessor(Model model) {
		model.addAttribute("professor", new Professor());
		model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
		return "/publica-criar-professor";
	}
	// fim do metodo para criar um novo professor

	// inicio do metodo para salvar um novo professor
	@PostMapping("/salvar")
	public String salvarProfessor(@Valid Professor professor, BindingResult result, Model model,
			RedirectAttributes attributes, @RequestParam(value = "dds", required = false) int[] dds) {

		model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();

		if (result.hasErrors()) {
			return "/publica-criar-professor";
		}

		if (dds == null) {
			model.addAttribute("mensagemDisciplina", "Pelo menos uma disciplina deve ser informada");
			return "/publica-criar-professor";
		} else {
			for (int i = 0; i < dds.length; i++) {
				long idDisciplina = dds[i];
				Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(idDisciplina);
				if (disciplinaOptional.isPresent()) {
					Disciplina disciplina = disciplinaOptional.get();
					disciplinas.add(disciplina);
				}
			}
		}

		List<Professor> prof = professorRepository.findByStatusTrueOrderBySiapeAsc();
		List<Professor> teacher = new ArrayList<Professor>();
		for (int i = 0; i < prof.size(); i++) {
			if (professor.getEmail().equals(prof.get(i).getEmail())) {
				teacher.add(prof.get(i));
			}
		}
		if (!teacher.isEmpty()) {
			model.addAttribute("emailExiste", "O E-mail informado já existe cadastrado no sistema");
			return "/publica-criar-professor";
		}
		professor.setDisciplinas(disciplinas); // relaciona disciplinas ao professor

		// Busca o cargo de PROFESSOR
		Cargo cargo = cargoRepository.findByNome("PROFESSOR");
		List<Cargo> cargos = new ArrayList<Cargo>();
		cargos.add(cargo);
		professor.setCargos(cargos); // associa o cargo de PROFESSOR ao usuário

		professor.setStatus(true);

		String senhaCriptografia = criptografia.encode(professor.getSenha());
		professor.setSenha(senhaCriptografia); // criptografando a senha no cadastro do professor

		professorRepository.save(professor);
		attributes.addFlashAttribute("mensagem", "Professor salvo com sucesso!");
		return "redirect:/professor/novo";
	}
	// fim do metodo para salvar um novo professor

	// funcão apenas pode ser usanda pelo admin
	@RequestMapping("/admin/listar")
	public String listarProfessor(Model model) {
		model.addAttribute("professores", professorRepository.findByStatusTrueOrderBySiapeAsc());
		return "/auth/admin/admin-listar-professor";
	}

	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/admin/apagar/{id}")
	public String deleteProfessor(@PathVariable("id") long id, Model model) {
		Professor professor = professorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("siape inválido:" + id));
		professor.setStatus(false);
		professorRepository.save(professor);
		return "redirect:/professor/admin/listar";
	}

	// funcão pode ser usanda tanto pelo admin quanto pelo professor
	@GetMapping("/editar/{id}")
	public String editarProfessor(@PathVariable("id") long id, Model model) {
		Optional<Professor> professorVelho = professorRepository.findById(id);
		if (!professorVelho.isPresent()) {
			throw new IllegalArgumentException("professor inválido:" + id);
		}
		Professor professor = professorVelho.get();
		professor.setSenha("");
		model.addAttribute("professor", professor);
		model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
		return "/auth/teacher/teacher-alterar-professor";
	}

	// funcão pode ser usanda tanto pelo admin quanto pelo estudante
	@PostMapping("/editar/{id}") // 
	public String editarProfessor(@PathVariable("id") long id, Model model, @Valid Professor professor,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(value = "dds", required = false) int[] dds) {
		
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();

		if (result.hasErrors()) {
			if (dds == null) {
				model.addAttribute("mensagemDisciplina", "Pelo menos uma disciplina deve ser informada");
				professor.setId(id);
				professor.setDisciplinas(disciplinas);
				model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
				return "/auth/teacher/teacher-alterar-professor";
			}
			for (int i = 0; i < dds.length; i++) {
				long idDisciplina = dds[i];
				Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(idDisciplina);
				if (disciplinaOptional.isPresent()) {
					Disciplina disciplina = disciplinaOptional.get();
					disciplinas.add(disciplina);
				}
			}
			professor.setId(id);
			professor.setDisciplinas(disciplinas);
			model.addAttribute("listaDisciplinas", disciplinaRepository.findAll());
			return "/auth/teacher/teacher-alterar-professor";
		}

		if (dds == null) {
			professor.setId(id);
			attributes.addFlashAttribute("mensagemDisciplina", "Pelo menos uma disciplina deve ser informada");
			return "redirect:/professor/editar/" + id;
		} else {
			for (int i = 0; i < dds.length; i++) {
				long idDisciplina = dds[i];
				Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(idDisciplina);
				if (disciplinaOptional.isPresent()) {
					Disciplina disciplina = disciplinaOptional.get();
					disciplinas.add(disciplina);
				}
			}
		}

		Professor prof = professorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));

		prof.setDisciplinas(disciplinas);
		prof.setNome(professor.getNome());
		prof.setEmail(professor.getEmail());
		String senhaCriptografia = criptografia.encode(professor.getSenha());
		prof.setSenha(senhaCriptografia); //criptografando a senha no cadastro do estudante

		professorRepository.save(prof);
		
		return "redirect:/disciplina/teacher/listar/"+prof.getId();
	}

}
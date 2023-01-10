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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Estudante;
import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.CargoRepository;
import br.edu.ifms.sistemaif.repository.EstudanteRepository;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Controller
@RequestMapping("/estudante")
public class EstudanteController {

	@Autowired
	private EstudanteRepository estudanteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CargoRepository cargoRepository;
	
	@Autowired
	private BCryptPasswordEncoder criptografia;

	@GetMapping("/novo")
	public String adicionarEstudante(Model model) {
		model.addAttribute("estudante", new Estudante());
		return "/publica-criar-estudante";
	}

	@PostMapping("/salvar")
	public String salvarEstudante(@Valid Estudante estudante, BindingResult result, Model model,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "/publica-criar-estudante";
		}

		List<Estudante> aluno = estudanteRepository.findByStatusTrueOrderByRaAsc();
		List<Estudante> studant = new ArrayList<Estudante>();

		for (int i = 0; i < aluno.size(); i++) {
			if (estudante.getEmail().equals(aluno.get(i).getEmail())) {
				studant.add(aluno.get(i));
			}
		}
		if (!studant.isEmpty()) {
			model.addAttribute("emailExiste", "O E-mail informado já existe cadastrado no sistema");
			return "/publica-criar-estudante";
		}

		// Busca o cargo de ESTUDANTE
		Cargo cargo = cargoRepository.findByNome("ESTUDANTE");
		List<Cargo> cargos = new ArrayList<Cargo>();
		cargos.add(cargo);
		estudante.setCargos(cargos); // associa o cargo de ESTUDANTE ao usuário

		estudante.setStatus(true);
		
		String senhaCriptografia = criptografia.encode(estudante.getSenha());
		estudante.setSenha(senhaCriptografia); //criptografando a senha no cadastro do estudante
		
		estudanteRepository.save(estudante);
		attributes.addFlashAttribute("mensagem", "Estudante salvo com sucesso!");
		return "redirect:/estudante/novo";
	}

	// funcão apenas pode ser usanda pelo admin
	@RequestMapping("/admin/listar")
	public String listarEstudante(Model model) {
		model.addAttribute("estudantes", estudanteRepository.findByStatusTrueOrderByRaAsc());
		return "/auth/admin/admin-listar-estudante";
	}
	
	// funcão apenas pode ser usanda pelo admin
	@GetMapping("/admin/apagar/{id}")
	public String deleteEstudante(@PathVariable("id") long id, Model model) {
		Estudante estudante = estudanteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));

		estudante.setStatus(false);
		estudanteRepository.save(estudante);
		return "redirect:/estudante/admin/listar";
	}
	
	// funcão pode ser usanda tanto pelo admin quanto pelo estudante
	@GetMapping("/editar/{id}")
	public String editarEstudante(@PathVariable("id") long id, Model model) {
		Optional<Estudante> estudanteVelho = estudanteRepository.findById(id);
		if (!estudanteVelho.isPresent()) {
			throw new IllegalArgumentException("Estudante inválido:" + id);
		}
		Estudante estudante = estudanteVelho.get();
		estudante.setSenha("");
		model.addAttribute("estudante", estudante);
		return "/auth/student/student-alterar-estudante";
	}

	// funcão pode ser usanda tanto pelo admin quanto pelo estudante
	@PostMapping("/editar/{id}")
	public String editarEstudante(@PathVariable("id") long id, @Valid Estudante estudante, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println(result);
			estudante.setId(id);
			return "/auth/student/student-alterar-estudante";
		}

		/*// Busca o cargo básico de usuário
		Cargo cargo = cargoRepository.findByNome("ESTUDANTE");
		List<Cargo> cargos = new ArrayList<Cargo>();
		cargos.add(cargo);
		Optional<Estudante> estudanteOpicional = estudanteRepository.findById(id);
		Estudante studant = estudanteOpicional.get();*/

		Optional<Estudante> estudanteOpicional = estudanteRepository.findById(id);
		Estudante studant = estudanteOpicional.get();

		//studant.setCargos(cargos);
		studant.setNome(estudante.getNome());
		studant.setTurma(estudante.getTurma());
		studant.setEmail(estudante.getEmail());

		String senhaCriptografia = criptografia.encode(estudante.getSenha());
		studant.setSenha(senhaCriptografia); 

		estudanteRepository.save(studant);

		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id inválido:" + id));
		for(Cargo cargo: usuario.getCargos()) {
			if (cargo.getNome().equals("MONITOR")) {
				return "redirect:/disciplina/teacher/listar/"+usuario.getId();
			}else if(cargo.getNome().equals("ESTUDANTE")){
				return "redirect:/disciplina/student/listar";
			}
		}
		return "redirect:/disciplina/student/listar";
	}
}

package br.edu.ifms.sistemaif.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método que verifica qual papel o usuário tem na aplicação
     */
    private boolean temAutorizacao(Usuario usuario, String nome) {
        List<Cargo> cargos = usuario.getCargos();

        for (Cargo cg : cargos) {
            if (cg.getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/index")
    public String index(@CurrentSecurityContext(expression = "authentication.name") String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        String redirectURL = "";
        if (temAutorizacao(usuario, "ADMIN")) {
            redirectURL = "/auth/admin/admin-index";
        } else if (temAutorizacao(usuario, "PROFESSOR")) {
            redirectURL = "redirect:/disciplina/teacher/listar/"+usuario.getId();
        } else if (temAutorizacao(usuario, "MONITOR")) {
            redirectURL = "redirect:/disciplina/teacher/listar/"+usuario.getId();
        } else if (temAutorizacao(usuario, "ESTUDANTE")) {
            redirectURL = "redirect:/disciplina/student/listar";
        }
        return redirectURL;
    }

}
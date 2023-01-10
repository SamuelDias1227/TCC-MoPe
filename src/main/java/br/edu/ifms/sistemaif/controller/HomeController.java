package br.edu.ifms.sistemaif.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("msnBemVindo", "Bem-vindo ao sistema de gerenciamento de permanências e monitorias");
		return "publica-index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}

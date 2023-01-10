package br.edu.ifms.sistemaif.security;


import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import br.edu.ifms.sistemaif.modelo.Usuario;
import br.edu.ifms.sistemaif.repository.UsuarioRepository;

@Service
@Transactional
public class DetalheUsuarioServico implements UserDetailsService {

	UsuarioRepository usuarioRepository;
	
	public DetalheUsuarioServico(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("cheguei aqui!");
		Usuario usuario = usuarioRepository.findByEmail(username);
		
		if(usuario != null && usuario.getStatus()) {
			DetalheUsuario detalheUsuario = new DetalheUsuario(usuario);
			return detalheUsuario;
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
	}

}

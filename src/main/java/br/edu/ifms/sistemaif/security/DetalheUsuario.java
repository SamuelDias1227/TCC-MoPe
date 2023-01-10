package br.edu.ifms.sistemaif.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.modelo.Usuario;

public class DetalheUsuario implements UserDetails {
	
	private Usuario usuario;

	public DetalheUsuario(Usuario usuario) {
		super();
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Cargo> cargos = usuario.getCargos();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(Cargo cargo: cargos) {
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority(cargo.getNome());
			authorities.add(sga);
		}
		return authorities;

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return usuario.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return usuario.getStatus();
	}
	
	public String getNome() {
		return usuario.getNome();
	}
	
	public Long getId() {
		return usuario.getId();
	}

}

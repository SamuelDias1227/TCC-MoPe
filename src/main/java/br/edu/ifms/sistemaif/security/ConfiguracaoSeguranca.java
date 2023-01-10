package br.edu.ifms.sistemaif.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.edu.ifms.sistemaif.repository.UsuarioRepository;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private LoginSucesso loginSucesso;
	
	@Bean
	public BCryptPasswordEncoder gerarCriptografia() {
		BCryptPasswordEncoder criptografia = new BCryptPasswordEncoder();
		return criptografia;
	}
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		DetalheUsuarioServico detalheDoUsuario = new DetalheUsuarioServico(usuarioRepository);
		return detalheDoUsuario;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/auth/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/auth/teacher/*").hasAnyAuthority("ADMIN", "PROFESSOR", "MONITOR")
		.antMatchers("/auth/student/*").hasAnyAuthority("ADMIN", "ESTUDANTE", "MONITOR")
		.antMatchers("/professor/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/professor/editar/*").hasAnyAuthority("PROFESSOR")
		.antMatchers("/estudante/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/estudante/editar/*").hasAnyAuthority("ESTUDANTE")
		.antMatchers("/disciplina/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/disciplina/student/*").hasAnyAuthority("ESTUDANTE")
		.antMatchers("/disciplina/teacher/*").hasAnyAuthority("PROFESSOR", "MONITOR")
		.antMatchers("/horario/novo*").hasAnyAuthority("PROFESSOR", "MONITOR")
		.antMatchers("/horario/salvar*").hasAnyAuthority("PROFESSOR", "MONITOR")
		.antMatchers("/horario/editar*").hasAnyAuthority("ADMIN", "PROFESSOR", "MONITOR")
		.antMatchers("/horario/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/horario/student/*").hasAnyAuthority("ESTUDANTE")
		.antMatchers("/horario/teacher/*").hasAnyAuthority("PROFESSOR")
		.antMatchers("/horario/monitor/*").hasAnyAuthority("MONITOR")
		.antMatchers("/usuario/*").hasAnyAuthority("ADMIN", "PROFESSOR", "MONITOR", "ESTUDANTE")
		.and()
		.exceptionHandling().accessDeniedPage("/auth/auth-acesso-negado")
		.and()
		.formLogin().successHandler(loginSucesso)
		.loginPage("/login").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// O objeto que vai obter os detalhes do usuário
		UserDetailsService detalheDoUsuario = userDetailsServiceBean();
		// Objeto para criptografia
		BCryptPasswordEncoder criptografia = gerarCriptografia();
		
		auth.userDetailsService(detalheDoUsuario).passwordEncoder(criptografia);
	}
	
}

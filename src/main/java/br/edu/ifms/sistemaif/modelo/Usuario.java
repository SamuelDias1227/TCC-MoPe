package br.edu.ifms.sistemaif.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    @NotEmpty(message = "A senha deve ser informada")
    @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
    private String senha;

    private Boolean status;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="usuario_cargo", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "cargo_id"))
    private List<Cargo> cargos;

    //metodos de acesso
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
	public List<Cargo> getCargos() {
		return cargos;
	}
	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}
	
}

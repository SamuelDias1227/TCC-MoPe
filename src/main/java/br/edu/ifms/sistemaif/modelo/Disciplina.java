package br.edu.ifms.sistemaif.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;

    private String descricao;

    @NotEmpty(message = "A ementa deve ser informada")
    @Size(min = 3, message = "A ementa deve ter no mínimo 3 caracteres")
    private String ementa;

    @ManyToMany(mappedBy = "disciplinas", fetch = FetchType.LAZY)
    private List<Professor> professores;
    
    @OneToMany(mappedBy = "disciplina")
    private List<Monitor> monitorias;

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
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getEmenta() {
        return ementa;
    }
    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }
    public List<Professor> getProfessores() {
        return professores;
    }
    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }
	public List<Monitor> getMonitorias() {
		return monitorias;
	}
	public void setMonitorias(List<Monitor> monitorias) {
		this.monitorias = monitorias;
	}
    
}
package br.edu.ifms.sistemaif.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity 
public class Monitor{ // monitoria
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
			
	@ManyToOne
	private Disciplina disciplina;
	
	@ManyToOne
	private Estudante estudante;
	
	@OneToMany
    @JoinColumn(name = "monitor_id")
    private List<Horario> horarios;

	@OneToMany(mappedBy = "monitor")
    private List<Presenca> presencas;
	
	private boolean ativo;
	
	private String semestre;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Estudante getEstudante() {
		return estudante;
	}
	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}
	public String getSemestre() {
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public Disciplina getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public List<Horario> getHorarios() {
		return horarios;
	}
	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}
	
	public List<Presenca> getPresencas() {
		return presencas;
	}
	public void setPresencas(List<Presenca> presencas) {
		this.presencas = presencas;
	}
}

package br.edu.ifms.sistemaif.modelo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Presenca {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Boolean confirmacao = false;
	
	@ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
	
	@ManyToOne
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;
	
	@ManyToOne
    @JoinColumn(name = "professor_id", nullable=true)
    private Professor professor;

	@ManyToOne
    @JoinColumn(name = "monitor_id", nullable=true)
    private Monitor monitor;
	
	@Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date data;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getConfirmacao() {
		return confirmacao;
	}
	public void setConfirmacao(Boolean confirmacao) {
		this.confirmacao = confirmacao;
	}

	public Horario getHorario() {
		return horario;
	}
	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Estudante getEstudante() {
		return estudante;
	}
	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public Monitor getMonitor() {
		return monitor;
	}
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}
}

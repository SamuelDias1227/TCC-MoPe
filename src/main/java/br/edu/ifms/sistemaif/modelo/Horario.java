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
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String horarioInicio;

    private String horarioFinal;

    private String dia;
    
    private String local;
    
    private Boolean status;
    
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable=true)
    private Professor professor;
    
    @ManyToOne
    @JoinColumn(name = "monitor_id", nullable=true)
    private Monitor monitor;
    
    @OneToMany(mappedBy = "horario")
    private List<Presenca> presencas;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHorarioInicio() {
        return horarioInicio;
    }
    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }
    public String getHorarioFinal() {
        return horarioFinal;
    }
    public void setHorarioFinal(String horarioFinal) {
        this.horarioFinal = horarioFinal;
    }
    public String getDia() {
        return dia;
    }
    public void setDia(String dia) {
        this.dia = dia;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Monitor getMonitor() {
		return monitor;
	}
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}
	public List<Presenca> getPresencas() {
		return presencas;
	}
	public void setPresencas(List<Presenca> presencas) {
		this.presencas = presencas;
	}
}
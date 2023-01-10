package br.edu.ifms.sistemaif.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="idPessoa")
public class Professor extends Usuario{
	
    private Long siape;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="professor_disciplina", joinColumns = @JoinColumn(name = "professor_siape"), inverseJoinColumns = @JoinColumn(name = "disciplina_id"))
    private List<Disciplina> disciplinas;
    
    @OneToMany
    @JoinColumn(name = "professor_id")
    private List<Horario> horarios;
    
    @OneToMany(mappedBy = "professor")
    private List<Presenca> presencas;

    //metodos de acesso
    public Long getSiape() {
        return siape;
    }
    public void setSiape(Long siape) {
        this.siape = siape;
    }
    
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
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
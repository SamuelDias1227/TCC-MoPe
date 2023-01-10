package br.edu.ifms.sistemaif.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name="idPessoa")
public class Estudante extends Usuario{
	
    private Long ra;

	@NotNull 
    private int turma;
    
    @OneToMany(mappedBy = "estudante")
    private List<Monitor> monitorias;
    
    @OneToMany(mappedBy = "estudante")
    private List<Presenca> presencas;
    
    //metodos de acesso
	public Long getRa() {
		return ra;
	}
	public void setRa(Long ra) {
		this.ra = ra;
	}

	public int getTurma() {
		return turma;
	}
	public void setTurma(int turma) {
		this.turma = turma;
	}
	
	public List<Monitor> getMonitorias() {
		return monitorias;	
	}
	public void setMonitorias(List<Monitor> monitorias) {
		this.monitorias = monitorias;
	}
	
	public List<Presenca> getPresencas() {
		return presencas;
	}
	public void setPresencas(List<Presenca> presencas) {
		this.presencas = presencas;
	}
}

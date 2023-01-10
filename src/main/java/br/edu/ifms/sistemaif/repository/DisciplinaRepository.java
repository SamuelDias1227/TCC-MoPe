package br.edu.ifms.sistemaif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>{
    
}

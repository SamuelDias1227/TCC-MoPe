package br.edu.ifms.sistemaif.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long>{
    Professor findByEmail(String email);
    List<Professor> findByStatusTrueOrderBySiapeAsc();
    //List<Professor> findByStatusTrueAndEmail(String email);
}

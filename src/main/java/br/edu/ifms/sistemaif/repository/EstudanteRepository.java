package br.edu.ifms.sistemaif.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Estudante;

public interface EstudanteRepository extends JpaRepository<Estudante, Long>{
    Estudante findByEmail(String email);
    Optional<Estudante> findByRa(long ra);
    List<Estudante> findByStatusTrueOrderByRaAsc();
}

package br.edu.ifms.sistemaif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
}

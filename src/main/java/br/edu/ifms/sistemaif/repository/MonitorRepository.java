package br.edu.ifms.sistemaif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Monitor;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {

}

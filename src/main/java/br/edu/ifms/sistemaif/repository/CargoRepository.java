package br.edu.ifms.sistemaif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.sistemaif.modelo.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
	Cargo findByNome(String nome);

}

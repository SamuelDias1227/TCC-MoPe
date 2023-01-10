package br.edu.ifms.sistemaif.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.ifms.sistemaif.modelo.Cargo;
import br.edu.ifms.sistemaif.repository.CargoRepository;

@Component
public class CaregadoraDados implements CommandLineRunner {
	
	@Autowired
	private CargoRepository cargoRepository;


	@Override
	public void run(String... args) throws Exception {
		
		String[] nomes = {"ADMIN", "ESTUDANTE", "PROFESSOR", "MONITOR"};
		
		for (String nomeString: nomes) {
			Cargo cargo = cargoRepository.findByNome(nomeString);
			if (cargo == null) {
				cargo = new Cargo(nomeString);
				cargoRepository.save(cargo);
			}
		}

	}

}

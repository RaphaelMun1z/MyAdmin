package com.rm.myadmin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rm.myadmin.entities.Residence;
import com.rm.myadmin.repositories.ResidenceRepository;
import com.rm.myadmin.services.exceptions.ResourceNotFoundException;

@Service
public class ResidenceService {
	@Autowired
	private ResidenceRepository repository;

	public List<Residence> findAll() {
		return repository.findAll();
	}

	public Residence findById(Long id) {
		Optional<Residence> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Residence create(Residence obj) {
		return repository.save(obj);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}

package com.rm.myadmin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.rm.myadmin.entities.BillingAddress;
import com.rm.myadmin.repositories.BillingAddressRepository;
import com.rm.myadmin.services.exceptions.DatabaseException;
import com.rm.myadmin.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BillingAddressService {
	@Autowired
	private BillingAddressRepository repository;

	public List<BillingAddress> findAll() {
		return repository.findAll();
	}

	public BillingAddress findById(Long id) {
		Optional<BillingAddress> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public BillingAddress create(BillingAddress obj) {
		return repository.save(obj);
	}

	public void delete(Long id) {
		try {
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw new ResourceNotFoundException(id);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public BillingAddress update(Long id, BillingAddress obj) {
		try {
			BillingAddress entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(BillingAddress entity, BillingAddress obj) {
		entity.setNumber(obj.getNumber());
		entity.setStreet(obj.getStreet());
		entity.setDistrict(obj.getDistrict());
		entity.setCity(obj.getCity());
		entity.setState(obj.getState());
		entity.setCountry(obj.getCountry());
		entity.setCep(obj.getCep());
		entity.setComplement(obj.getComplement());
	}
}

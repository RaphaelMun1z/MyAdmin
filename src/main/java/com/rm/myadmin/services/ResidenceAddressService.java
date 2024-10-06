package com.rm.myadmin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.myadmin.entities.ResidenceAddress;
import com.rm.myadmin.repositories.ResidenceAddressRepository;
import com.rm.myadmin.services.exceptions.DataViolationException;
import com.rm.myadmin.services.exceptions.DatabaseException;
import com.rm.myadmin.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ResidenceAddressService {
	@Autowired
	private ResidenceAddressRepository repository;

	@Autowired
	private CacheService cacheService;

	@Cacheable("findAllResidenceAddress")
	public List<ResidenceAddress> findAllCached() {
		return findAll();
	}

	public List<ResidenceAddress> findAll() {
		return repository.findAll();
	}

	public ResidenceAddress findById(String id) {
		Optional<ResidenceAddress> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException("Residence Address", id));
	}

	@Transactional
	public ResidenceAddress create(ResidenceAddress obj) {
		try {
			ResidenceAddress ra = repository.save(obj);
			cacheService.putResidenceAddressCache();
			return ra;
		} catch (DataIntegrityViolationException e) {
			throw new DataViolationException();
		}
	}

	@Transactional
	public void delete(String id) {
		try {
			if (repository.existsById(id)) {
				repository.deleteById(id);
				cacheService.evictAllCacheValues("findAllResidenceAddress");
			} else {
				throw new ResourceNotFoundException(id);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public ResidenceAddress update(String id, ResidenceAddress obj) {
		try {
			ResidenceAddress entity = repository.getReferenceById(id);
			updateData(entity, obj);
			ResidenceAddress ra = repository.save(entity);
			cacheService.putResidenceAddressCache();
			return ra;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(ResidenceAddress entity, ResidenceAddress obj) {
		entity.setStreet(obj.getStreet());
		entity.setDistrict(obj.getDistrict());
		entity.setCity(obj.getCity());
		entity.setState(obj.getState());
		entity.setCountry(obj.getCountry());
		entity.setCep(obj.getCep());
		entity.setComplement(obj.getComplement());
	}
}

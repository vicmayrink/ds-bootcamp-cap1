package com.devsuperior.dsbootcampcap1.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsbootcampcap1.dto.ClientDTO;
import com.devsuperior.dsbootcampcap1.entities.Client;
import com.devsuperior.dsbootcampcap1.repositories.ClientRepository;
import com.devsuperior.dsbootcampcap1.services.exceptions.DatabaseException;
import com.devsuperior.dsbootcampcap1.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}

	@Transactional()
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new  Client();
		//https://www.oodlestechnologies.com/dev-blog/dto-to-entity-and-entity-to-dto-conversion./
		BeanUtils.copyProperties(dto, entity, "id");
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional()
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			//https://docs.spring.io/spring-data/jpa/docs/current/api/deprecated-list.html
			Client entity = repository.getReferenceById(id);
			//https://www.oodlestechnologies.com/dev-blog/dto-to-entity-and-entity-to-dto-conversion./
			BeanUtils.copyProperties(dto, entity, "id");
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found " + id); 
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

}

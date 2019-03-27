package com.acme.hibernate.orm;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "fruits")
@RestController
public class FruitResource {

	@PersistenceContext
	EntityManager entityManager;

	@GetMapping
	public Fruit[] get() {
		return entityManager.createNamedQuery("Fruits.findAll", Fruit.class)
				.getResultList().toArray(new Fruit[0]);
	}

	@GetMapping("{id}")
	public Fruit getSingle(@PathVariable Integer id) {
		Fruit entity = entityManager.find(Fruit.class, id);
		if (entity == null) {
			throw new NoFruitException("Fruit with id of " + id + " does not exist.");
		}
		return entity;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Fruit> create(Fruit fruit) {
		if (fruit.getId() != null) {
			throw new BadFruitException("Id was invalidly set on request.");
		}

		entityManager.persist(fruit);
		return ResponseEntity.status(201).body(fruit);
	}

	@PutMapping("{id}")
	@Transactional
	public Fruit update(@PathVariable Integer id, Fruit fruit) {
		if (fruit.getName() == null) {
			throw new BadFruitException("Fruit Name was not set on request.");
		}

		Fruit entity = entityManager.find(Fruit.class, id);

		if (entity == null) {
			throw new NoFruitException("Fruit with id of " + id + " does not exist.");
		}

		entity.setName(fruit.getName());

		return entity;
	}

	@DeleteMapping("{id}")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		Fruit entity = entityManager.getReference(Fruit.class, id);
		if (entity == null) {
			throw new NoFruitException("Fruit with id of " + id + " does not exist.");
		}
		entityManager.remove(entity);
	}

}

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
class NoFruitException extends RuntimeException {

	public NoFruitException(String msg) {
		super(msg);
	}

}

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class BadFruitException extends RuntimeException {

	public BadFruitException(String msg) {
		super(msg);
	}

}

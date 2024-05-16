package com.desofs.backend.database.springRepositories;

import com.desofs.backend.database.models.RentalPropertyDB;
import org.springframework.data.repository.CrudRepository;

public interface RentalPropertyRepositoryJPA extends CrudRepository<RentalPropertyDB, String> {
}

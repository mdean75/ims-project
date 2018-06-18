package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ImsDao extends CrudRepository<Ticket, Integer> {
}

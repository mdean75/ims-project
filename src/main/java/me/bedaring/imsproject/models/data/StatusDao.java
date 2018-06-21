package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface StatusDao extends CrudRepository<Status, Integer> {
}

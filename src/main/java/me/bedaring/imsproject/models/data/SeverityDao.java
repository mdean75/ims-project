package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Severity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SeverityDao extends CrudRepository<Severity, Integer> {

}

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

    @Modifying
    @Query(value = "update severity set severity_name = ?1 where id = ?2", nativeQuery = true)
    int update(String severityName, int id);

}

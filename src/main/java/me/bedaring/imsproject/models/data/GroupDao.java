package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.AssignedGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GroupDao extends CrudRepository<AssignedGroup, Integer> {

    // get all groups sorted by name
    List<AssignedGroup> findAllByOrderByGroupName();
}

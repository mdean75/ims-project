package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ImsDao extends CrudRepository<Ticket, Integer> {

    int countTicketBySeverity(Severity severity);

    @Query(value = "select count(id) from ticket where category_sub_id = ?1 or category_main_id = ?1 or " +
            "category_detail_id = ?1", nativeQuery = true)
    int countTicketByCategory(Category category);

    @Query(value = "select count(id) from ticket where assigned_group_id = ?1", nativeQuery = true)
    int countTicketByAssignedGroup(AssignedGroup assignedGroup);

    int countTicketsByAssignedGroupEquals(AssignedGroup assignedGroup);

    List<Ticket> findAllByAssignedPersonId(int id);

    List<Ticket> findAllByAssignedGroupId(int id);

    int countTicketByAssignedPerson(User user);
}

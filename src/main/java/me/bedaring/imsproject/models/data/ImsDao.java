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

    // get a count of all tickets with the specified severity
    int countTicketBySeverity(Severity severity);

    // get a count of all tickets where the given category is found with a type of main, sub, or detail
    @Query(value = "select count(id) from ticket where category_sub_id = ?1 or category_main_id = ?1 or " +
            "category_detail_id = ?1", nativeQuery = true)
    int countTicketByCategory(Category category);

    // get a list of all tickets assigned to the specified group
    int countTicketsByAssignedGroupEquals(AssignedGroup assignedGroup);

    // get a list of all tickets assigned to the specified user
    List<Ticket> findAllByAssignedPersonId(int id);

    // get a list off all tickets assigned to the specified group id
    List<Ticket> findAllByAssignedGroupId(int id);

    // get a count of all tickets assigned to the specified user
    int countTicketByAssignedPerson(User user);
}

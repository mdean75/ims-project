package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryDao extends CrudRepository<Category, Integer> {
    // get all categories of the specified category type
    List<Category> findCategoryByCategoryTypeEquals(String categoryType);

    // get all categories sorted by type then name
    List<Category> findAllByOrderByCategoryTypeAscCategoryNameAsc();

}

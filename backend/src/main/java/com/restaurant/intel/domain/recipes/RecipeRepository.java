package com.restaurant.intel.domain.recipes;

import com.restaurant.intel.domain.menu.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
  List<Recipe> findByMenuItem(MenuItem menuItem);
}

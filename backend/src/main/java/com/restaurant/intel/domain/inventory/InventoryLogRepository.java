package com.restaurant.intel.domain.inventory;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
  Optional<InventoryLog> findTopByLocationIdAndIngredientIdOrderByLoggedAtDesc(Long locationId, Long ingredientId);
}

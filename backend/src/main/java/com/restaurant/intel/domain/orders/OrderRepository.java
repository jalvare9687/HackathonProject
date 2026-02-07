package com.restaurant.intel.domain.orders;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByLocationIdAndOrderedAtBetween(Long locationId, LocalDateTime start, LocalDateTime end);
}

package com.restaurant.intel.domain.inventory;

import com.restaurant.intel.domain.ingredient.Ingredient;
import com.restaurant.intel.domain.location.Location;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory_logs")
@Getter
@Setter
@NoArgsConstructor
public class InventoryLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ingredient_id")
  private Ingredient ingredient;

  private Double onHandAfter;
  private LocalDateTime loggedAt;
}

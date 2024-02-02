package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository<T extends Event> extends JpaRepository<T, Long> {
}

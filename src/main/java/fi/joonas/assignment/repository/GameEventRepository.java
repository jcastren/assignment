package fi.joonas.assignment.repository;

import fi.joonas.assignment.jpaentity.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameEventRepository extends JpaRepository<GameEvent, Long> {
}

package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.EventWinner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventWinnerRepository extends JpaRepository<EventWinner, Long> {
    boolean existsByEventId(Long eventId);
    EventWinner findByUserIdAndEventId(Long userId, Long eventId);
    List<EventWinner> findAllByEventId(Long eventId);
}

package com.matome.casino.repository;


import com.matome.casino.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {


    Optional<Player> findByName(String name);
}

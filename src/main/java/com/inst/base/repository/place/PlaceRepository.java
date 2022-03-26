package com.inst.base.repository.place;

import com.inst.base.entity.place.Place;
import com.inst.base.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {
    Optional<Place> findByName(String name);
}

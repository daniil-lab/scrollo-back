package com.inst.base.repository.story;

import com.inst.base.entity.story.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoryRepository extends JpaRepository<Story, UUID> {
}

package com.inst.base.repository.story;

import com.inst.base.entity.story.Story;
import com.inst.base.entity.story.StoryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoryFileRepository extends JpaRepository<StoryFile, UUID> {
}

package com.inst.base.service.story;

import com.inst.base.entity.story.Story;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoryService {
    Story createStory();

    PageResponse<Story> getMyStories();

    Story checkStory();

    Story removeStory();

    List<List<Story>> getStoriesForMainScreen();
}

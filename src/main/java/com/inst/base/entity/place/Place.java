package com.inst.base.entity.place;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Place extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private Set<Post> posts;
}

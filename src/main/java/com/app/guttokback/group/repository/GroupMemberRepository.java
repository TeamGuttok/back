package com.app.guttokback.group.repository;

import com.app.guttokback.group.domain.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {
    
}

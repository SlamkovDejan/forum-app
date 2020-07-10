package mk.ukim.finki.emt.forum.forummanagement.domain.repository;

import mk.ukim.finki.emt.forum.forummanagement.domain.model.Discussion;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.DiscussionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, DiscussionId> {
}

package mk.ukim.finki.emt.forum.forummanagement.domain.repository;

import mk.ukim.finki.emt.forum.forummanagement.domain.model.Forum;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.ForumId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, ForumId> {
}

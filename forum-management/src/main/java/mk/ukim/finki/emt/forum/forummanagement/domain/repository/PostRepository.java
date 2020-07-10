package mk.ukim.finki.emt.forum.forummanagement.domain.repository;

import mk.ukim.finki.emt.forum.forummanagement.domain.model.Post;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.PostId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, PostId> {
}

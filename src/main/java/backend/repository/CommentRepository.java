package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
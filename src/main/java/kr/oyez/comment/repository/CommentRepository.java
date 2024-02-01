package kr.oyez.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.comment.domain.Comment;
import kr.oyez.comment.repository.custom.CommentRepositoryCustom;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}

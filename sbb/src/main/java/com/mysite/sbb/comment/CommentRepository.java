package com.mysite.sbb.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.sbb.question.Question;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	List<Comment> findByQuestion(Question question);
	
	// 프로필 구성에 필요한 쿼리
	@Query("select q "
		+ "from Comment q "
		+ "join SiteUser u on q.author=u "
		+ "where u.username = :username "
		+ "order by q.createDate desc ")
		List<Comment> findCurrentComment(@Param("username") String username, Pageable pageable);

}

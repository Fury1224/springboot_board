package com.mysite.sbb.answer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.sbb.question.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	Page<Answer> findAllByQuestion(Question question, Pageable pageable);
	
	// 프로필 구성을 위한 쿼리
	@Query("select q "
		+ "from Answer q "
		+ "join SiteUser u on q.author=u "
		+ "where u.username = :username "
		+ "order by q.createDate desc ")
		List<Answer> findCurrentAnswer(@Param("username") String username, Pageable pageable);

}

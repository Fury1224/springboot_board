package com.mysite.sbb.question;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.category.Category;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	Question findBySubject(String subject); // 메서드 이름을 자동으로 분석하여 실행
	Question findBySubjectAndContent(String subject, String Content);
	List<Question> findBySubjectLike(String subject);
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable); // 검색 기능
	Page<Question> findByCategory(Category category, Pageable pageable);	// 카테고리
}

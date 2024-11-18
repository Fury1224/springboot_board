package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

@SpringBootTest
class SbbApplicationTests {

	@Autowired // 외부 클래스를 자동 생성, 등록
	private QuestionService questionService;
	
	@Test
	void testJpa() {
		for(int i = 1; i<=100; i++) {
			String subject = String.format("테스트 데이터입니다 : [%03d]", i);
			String content = "내용 없음 ";
			this.questionService.create(subject, content);
		}
	}
}

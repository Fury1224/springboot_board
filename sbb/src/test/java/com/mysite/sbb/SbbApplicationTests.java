package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
	
	
	@Test
	@Transactional
	void testJpa() {
		
		/*
		// 질문으로 답변 조회
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		List<Answer> answerList = q.getAnswerList();
		
		assertEquals(1, answerList.size());
		assertEquals("답변입니다", answerList.get(0).getContent());
		
		System.out.println("선택한 질문 제목 : " + q.getSubject());
		System.out.println("조회된 답변 : " + answerList.get(0).getContent());
		
		// 답변 조회
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(1, a.getQuestion().getId());
		
		System.out.println(a.getContent());
		System.out.println(a.getQuestion().getId());
		
		// 답변 작성
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		Answer a = new Answer();
		a.setContent("답변입니다");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
				
		// 테이블 행 삭제
		assertEquals(2, this.questionRepository.count()); // 테이블 행 개수가 2 인지 확인
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q); // 해당 테이블 행 삭제
		assertEquals(1, this.questionRepository.count()); // 테이블 행 개수가 1 인지 확인
		
		// 데이터 수정
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
		
		// 해당 문자를 포함한 검색
		List<Question> qList = this.questionRepository.findBySubjectLike("2019%");
		Question q = qList.get(0);
		assertEquals("20194085", q.getSubject());
				
		// 두가지 요소로 찾기
		Question q = this.questionRepository.findBySubjectAndContent("20194085", "한승헌 과제 수행중입니다");
		assertEquals(1, q.getId());
		
		// 한가지 요소로 찾기
		Question q = this.questionRepository.findBySubject("20194085");
		assertEquals(1, q.getId());
		
		// id값으로 데이터 찾기
		Optional<Question> oq = this.questionRepository.findById(1); 
		if(oq.isPresent()) { // 맞는 데이터가 있으면
			Question q = oq.get();
			assertEquals("20194085", q.getSubject());
		}
		
		
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size()); // 전체 데이터가 2개 인지 확인
		
		Question q = all.get(0); // 첫번째 데이터 가져오기
		assertEquals("20194085", q.getSubject()); // 일치하는지 확인
		
		// 데이터 삽입
		Question q1 = new Question();
		q1.setSubject("20194085");
		q1.setContent("한승헌 과제 수행중입니다");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);
		
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문");
		q2.setContent("id는 자동생성?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);
		
		*/
				
	}

}

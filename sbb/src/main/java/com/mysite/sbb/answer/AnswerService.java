package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

	private final AnswerRepository answerRepository;
	
	public Answer create(Question question, String content, SiteUser user) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(user);
		this.answerRepository.save(answer);
		return answer;
	}
	
	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = this.answerRepository.findById(id);
		if(answer.isPresent()) {
			return answer.get();
		}
		else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		this.answerRepository.save(answer);
	}

	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}
	
	public Page<Answer> getList(Question question, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("voter")); 
	        	sorts.add(Sort.Order.asc("createDate"));
	    	Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
	    	return this.answerRepository.findAllByQuestion(question, pageable);
	   }

	public List<Answer> getCurrentListByUser(String username, int num) {
		Pageable pageable = PageRequest.of(0, num);
		return answerRepository.findCurrentAnswer(username, pageable);
	}
	
	public List<Answer> getRecentAnswers(int limit) {
		return this.answerRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createDate"))).getContent();
	}


}

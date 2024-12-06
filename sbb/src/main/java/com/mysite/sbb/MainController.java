package com.mysite.sbb;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {

	private final QuestionService questionService;
	private final AnswerService answerService;
	
	@GetMapping("/")
	public String showRecentPosts(Model model) {
		// 최신 게시글 5개
		List<Question> questions = this.questionService.getRecentQuestions(5);
	
		// 최신 답변 5개
		List<Answer> answers = this.answerService.getRecentAnswers(5);
	
		model.addAttribute("questions", questions);
		model.addAttribute("answers", answers);
	
		return "recent_posts";
	}

}

package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/question") // 이 컨트롤러에서는 question 으로 시작하는 url로 매핑한다 (프리픽스)
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
	private final UserService userService;
	
	@GetMapping("/list")	// 질문 목록
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
		Page<Question> paging = this.questionService.getList(page);
		model.addAttribute("paging", paging); // questionList 라는 이름의 모델 객체에 값 questionList 저장
		return "question_list";
	}
	
	@GetMapping("/detail/{id}")	// 질문 상세보기
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create") // 질문 등록
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	
	@PostMapping("/create") // 질문 저장
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) { //principal = 로그인한 사용자의 정보 클래스
		if (bindingResult.hasErrors()) {
			return "question_form";
		}

		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
}

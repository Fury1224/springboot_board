package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentForm;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;


@RequestMapping("/question") // 이 컨트롤러에서는 question 으로 시작하는 url로 매핑한다 (프리픽스)
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final CategoryService categoryService;
	private final QuestionService questionService;
	private final UserService userService;
	private final AnswerService answerService;
	private final CommentService commentService;
	
	@GetMapping("/list")	// 질문 목록
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value = "kw", defaultValue = "") String kw) { // 검색 기능 추가
		Page<Question> paging = this.questionService.getList(page, kw);
		List<Category> categoryList = this.categoryService.getAll();
		model.addAttribute("paging", paging); // questionList 라는 이름의 모델 객체에 값 questionList 저장
		model.addAttribute("kw", kw); 
		model.addAttribute("category_list", categoryList);	// 카테고리 추가
		return "question_list";
	}
	
	/*
	@GetMapping("/detail/{id}")	// 질문 상세보기
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	*/
	
	// 질문 상세보기 + 페이징 기능으로 넘어온 요청 처리
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, 
			 @RequestParam(value = "answerPage", defaultValue = "0") int answerPage,
			 @RequestParam(value = "sort", defaultValue = "createDate") String sort) {
		Question question = this.questionService.getQuestion(id);
		Page<Answer> answerPaging =  this.answerService.getList(question, answerPage);
		List<Category> categoryList = this.categoryService.getAll();
		List<Comment> commentList = this.commentService.getCommentList(question);
		questionService.increaseViewCountAndUpdatePopular(id);	// 조회수 증가, 인기글 갱신
		model.addAttribute("question", question);
		model.addAttribute("answerPaging", answerPaging);
		model.addAttribute("category_list", categoryList);	// 카테고리 추가
		model.addAttribute("commentList", commentList);	// 댓글 추가
		model.addAttribute("commentForm", new CommentForm());	// 댓글 작성 폼
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm, Model model) {
		List<Category> category_list = this.categoryService.getAll();
		model.addAttribute("category_list", category_list);	// 카테고리 목록 보내기
		return "question_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal
			) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		Category category = this.categoryService.getCategoryByName(questionForm.getCategory());	// 카테고리 추가
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, category);
		return "redirect:/question/list";
	}
	
	
	@PreAuthorize("isAuthenticated()")	// 인증된 사용자만
	@GetMapping("/vote/{id}")	// 추천 기능
	public String questionVote(Principal principal, @PathVariable("id") Integer id, Model model) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		List<Category> category_list = this.categoryService.getAll();	// 기존의 카테고리 넘겨주기
		model.addAttribute("category_list", category_list);
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")	// 수정 버튼을 눌렀을 때
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal, Model model) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		List<Category> categoryList = this.categoryService.getAll();	// 카테고리 전달
        model.addAttribute("category_list", categoryList);
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")	// form 에서 수정 요청을 보냄
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		Question question = this.questionService.getQuestion(id);
		Category category = this.categoryService.getCategoryByName(questionForm.getCategory());	// 카테고리 수정
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), category);
		return String.format("redirect:/question/detail/%s", id);
	}
}

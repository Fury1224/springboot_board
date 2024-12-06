package com.mysite.sbb.user;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.category.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final CommentService commentService;
	private final CategoryService categoryService;

	
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "signup_form";
		}
		
		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
		}
		catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		}
		catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
			
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	
	// 프로필 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		String username = principal.getName();
		List<Category> categoryList = this.categoryService.getAll();
		model.addAttribute("username", username);
		model.addAttribute("questionList", questionService.getCurrentListByUser(username, 5));
		model.addAttribute("answerList", answerService.getCurrentListByUser(username, 5));
		model.addAttribute("commentList", commentService.getCurrentListByUser(username, 5));
		model.addAttribute("category_list", categoryList);
		return "profile";
	}


}

package com.mysite.sbb.user;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.s3.S3Service;
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
	private final S3Service s3Service;

	
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
	public String profile(Model model, Principal principal, UserUpdateForm userUpdateForm) {
		String username = principal.getName();
		SiteUser user = userService.getUser(username); // UserService에서 사용자 정보 가져오기
	    String profileImageUrl = user.getProfileImageUrl(); // 프로필 이미지 URL 가져오기
	    Long userId = user.getId();
	    
		List<Category> categoryList = this.categoryService.getAll();
		
		model.addAttribute("username", username);
		model.addAttribute("questionList", questionService.getCurrentListByUser(username, 5));
		model.addAttribute("answerList", answerService.getCurrentListByUser(username, 5));
		model.addAttribute("commentList", commentService.getCurrentListByUser(username, 5));
		model.addAttribute("category_list", categoryList);
		model.addAttribute("profileImageUrl", profileImageUrl); // 프로필 이미지 URL 추가
		model.addAttribute("userId", userId);	// userId 추가
		return "profile";
	}


	@PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String update(@Valid UserUpdateForm userUpdateForm, BindingResult bindingResult,
                         Model model, Principal principal) {
        SiteUser siteUser = this.userService.getUser(principal.getName());

        model.addAttribute("username", siteUser.getUsername());
        model.addAttribute("userEmail", siteUser.getEmail());
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        if(!this.userService.isMatch(userUpdateForm.getOriginPassword(), siteUser.getPassword())) {
            bindingResult.rejectValue("originPassword", "passwordInCorrect",
                    "기존 패스워드가 일치하지 않습니다.");
            return "profile";
        }
        if(!userUpdateForm.getPassword1().equals(userUpdateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "확인 패스워드가 일치하지 않습니다.");
            return "profile";
        }

        try {
            userService.update(siteUser, userUpdateForm.getPassword1());
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("updateFailed", e.getMessage());
        }
        return "profile";
    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/{userId}/uploadProfileImage")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@PathVariable("userId") Long userId, 
    		@RequestParam("file") MultipartFile file) {
		
        try {
            // S3에 이미지 업로드 후 URL 받기
            String s3Url = s3Service.uploadImage(file);

            // 해당 유저의 프로필 이미지 URL 업데이트
            SiteUser user = userService.getUserById(userId); // 유저 조회
            user.setProfileImageUrl(s3Url); // 프로필 이미지 URL 설정
            userService.updateProfile(user); // 유저 정보 업데이트
            // 성공적으로 업로드된 이미지 URL 반환
            return ResponseEntity.ok(Map.of("imageUrl", s3Url));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Upload failed"));
        }
    }

}

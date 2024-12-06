package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Question {
	@Id 	// 기본키 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
	private Integer id;
	
	@Column(length = 200) // 열의 길이
	private String subject;
	
	@Column(columnDefinition = "TEXT") // 글자수 제한 X
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	// Answer 에서 question 으로 연결되고 질문을 삭제하면 답변들도 삭제된다(cascade)
	private List<Answer> answerList; // 답변 리스트
	
	@ManyToOne
	private SiteUser author;	// 작성자
	
	@ManyToMany
	Set<SiteUser> voter;	// 추천 기능
	
	private LocalDateTime modifyDate;	// 수정날짜정보
	
	@ManyToOne
	private Category category;	// 카테고리 기능
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;	// 댓글 기능
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int viewCount;	// 조회수
	
	@Column(name = "is_popular", nullable = false, columnDefinition = "boolean default false")
	private boolean isPopular = false;	// 조회수에 따른 인기 게시물
	public boolean isPopular() {
		return isPopular;
	}
	public void setPopular(boolean popular) {
		isPopular = popular;
	}


}

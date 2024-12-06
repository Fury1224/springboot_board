package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.question.Question;
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

public class Answer {
	@Id 	// 기본키 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
	private Integer id;
	
	@Column(columnDefinition = "TEXT") // 글자수 제한 X
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private Question question;

	@ManyToOne
	private SiteUser author;
	
	@ManyToMany
	Set<SiteUser> voter;
	
	private LocalDateTime modifyDate;
	
	@OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;	// 댓글 기능
}

package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HelloLombok {

	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		HelloLombok h1 = new HelloLombok("Hi", 5);
		//h1.setHello("헬로");
		//h1.setLombok(5);
		
		System.out.println(h1.getHello());
		System.out.println(h1.getLombok());
	}
}

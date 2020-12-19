package br.br.helbert.word.replace.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
	
	private String name;
	private String firstName;
	private List<String> questions;
	
	
	public Client() {
		this.questions = new ArrayList<>();
	}


	public Client(String name, String firstName) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.questions = new ArrayList<>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public List<String> getQuestions() {
		return questions;
	}


	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
	
	

}

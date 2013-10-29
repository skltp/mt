/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */

package se.skltp.mb.fitnesse;

import java.util.ArrayList;


/**
 * Fitness script fixture to support verifying the flow of questions and answers.
 * 
 * Stateful; stores result of latest findAllXxx and allows searching in it.
 * 
 * @author mats.olsson@callistaenterprise.se
 */
public class VerifyQuestionAndAnswerFlow {
    
	private String patientId;
    private String careUnitId;
	private String question;
	private ArrayList<String> questions;
	private String answer;
	private ArrayList<String> answers;


    public VerifyQuestionAndAnswerFlow() {
	}
    
    public void setCareUnitIdAndPatient(String careUnitId, String patientId) {
		this.careUnitId = careUnitId;
		this.patientId = patientId;
    }


    public void askQuestion(String question) {
		this.question = question;
    }
    
    public void fetchQuestions() {
    	questions = new ArrayList<String>();
    	questions.add(question);
    }
    
    public int numberOfQuestions() {
    	return questions.size();
    }
    
    public String theQuestion() {
    	return questions.get(0);
    }
    
    public void answerQuestion(String answer) {
		this.answer = answer;
    }
    
    public void fetchAnswers() {
    	answers = new ArrayList<String>();
    	answers.add(answer);
    }
    
    public String theAnswer() {
    	return answers.get(0);
    }
    
    public int numberOfAnswers() {
    	return answers.size();
    }
        
}

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

package se.inera.ifv.casebox.core.service;

import java.util.List;

import se.inera.ifv.casebox.core.entity.Answer;

/**
 * @author Pär Wenåker
 *
 */
public class AnswersValue {
    private List<Answer> answers;

    private int answersLeft;

    /**
      * @param questions
      * @param questionsLeft
      */
    public AnswersValue(List<Answer> answers, int answersLeft) {
        this.answers = answers;
        this.answersLeft = answersLeft;
    }

    /**
     * @return the answers
     */
    public List<Answer> getAnswers() {
        return answers;
    }
    
    /**
     * @return the answersLeft
     */
    public int getAnswersLeft() {
        return answersLeft;
    }
}

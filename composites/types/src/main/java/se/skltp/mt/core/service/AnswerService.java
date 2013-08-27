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
package se.skltp.mt.core.service;

import java.util.List;
import java.util.Set;

import se.skltp.mt.core.entity.Answer;

/**
 * Service for handling the entity answer. 
 */
public interface AnswerService {

    /**
     * Returns all answers for a specific care unit.
     * 
     * @param careUnit the care unit
     * @return {@link List} {@link Answer}
     */
    List<Answer> getAllAnswersForCareUnit(String careUnit);

    /**
    * Returns the answers for a specific care unit. 
    * @param careUnit	the care unit
    * @return {@link AnswerValue}
    */
    AnswersValue getAnswersForCareUnit(String careUnit);

    /**
     * Save a new answer. 
     * @param answer	the answer to be saved
     * @return the new id for the answer
     */
    Long saveAnswer(Answer answer);

    /**
     * Deletes answers for the given answer ids in the list. 
     * @param id	{@link List} of answer ids
     */
    void deleteAnswersForCareUnit(String careUnit, Set<Long> ids);

}

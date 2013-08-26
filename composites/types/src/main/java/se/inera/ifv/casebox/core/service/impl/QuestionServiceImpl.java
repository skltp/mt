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
package se.inera.ifv.casebox.core.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.inera.ifv.casebox.core.entity.MessageType;
import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.core.repository.QuestionRepository;
import se.inera.ifv.casebox.core.service.QuestionService;
import se.inera.ifv.casebox.core.service.QuestionsValue;
import se.inera.ifv.casebox.core.service.StatisticService;

@Service
@Transactional(rollbackFor = IllegalStateException.class)
public class QuestionServiceImpl implements QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Value("${max.fetch.results}")
    int maxResults;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StatisticService statisticService;

    public QuestionsValue getQuestionsForCareUnit(String careUnit) {
        List<Question> questions = questionRepository.findForCareUnit(careUnit, maxResults);
        Long totalNumOfQuestions = questionRepository.getNumOfQuestionsForCareUnit(careUnit);

        int questionsLeft = (int) Math.max(0, totalNumOfQuestions - questions.size());

        for (Question q : questions) {
            q.setStatusRetrieved();
        }

        return new QuestionsValue(questions, questionsLeft);
    }

    public Long saveQuestion(Question question) {
        question = questionRepository.store(question);
        return question.getId();
    }

    public void deleteQuestionsForCareUnit(String careUnit, Set<Long> ids) {
        int deletedQuestions = questionRepository.delete(careUnit, ids);

        log.debug("Deleted {} questions", deletedQuestions);

        // FIXME: Handle better and provide better error description
        if (deletedQuestions != ids.size()) {
            throw new IllegalStateException("Cannot delete questions. Illegal ids or state");
        }

        statisticService.storeStatistics(careUnit, deletedQuestions, MessageType.Question);
    }

    /* (non-Javadoc)
     * @see se.inera.ifv.casebox.core.service.QuestionService#getAllQuestionsForCareUnit(java.lang.String)
     */
    public List<Question> getAllQuestionsForCareUnit(String careUnit) {
        return questionRepository.findAllForCareUnit(careUnit);
    }
}

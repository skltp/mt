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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.inera.ifv.casebox.core.entity.Answer;
import se.inera.ifv.casebox.core.entity.MessageType;
import se.inera.ifv.casebox.core.repository.AnswerRepository;
import se.inera.ifv.casebox.core.service.AnswerService;
import se.inera.ifv.casebox.core.service.AnswersValue;
import se.inera.ifv.casebox.core.service.StatisticService;

@Service
@Transactional(rollbackFor = IllegalStateException.class)
public class AnswerServiceImpl implements AnswerService {

    @Value("${max.fetch.results}")
    int maxResults;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private StatisticService statisticService;

    public List<Answer> getAllAnswersForCareUnit(String careUnit) {
        return answerRepository.findForCareUnit(careUnit, maxResults);
    }

    public AnswersValue getAnswersForCareUnit(String careUnit) {
        List<Answer> answers = answerRepository.findForCareUnit(careUnit, maxResults);
        Long totalNumOfAnswers = answerRepository.getNumOfQuestionsForCareUnit(careUnit);

        int questionsLeft = (int) Math.max(0, totalNumOfAnswers - answers.size());

        for (Answer a : answers) {
            a.setStatusRetrieved();
        }

        return new AnswersValue(answers, questionsLeft);
    }

    public Long saveAnswer(Answer answer) {
        Answer result = answerRepository.persist(answer);
        return result.getId();
    }

    public void deleteAnswersForCareUnit(String careUnit, Set<Long> ids) {
        int deletedAnswers = answerRepository.delete(careUnit, ids);

        // FIXME: Handle differently to provide better error message.
        if (deletedAnswers != ids.size()) {
            throw new IllegalStateException("Cannot delete answers. Illegal ids or state");
        }

        statisticService.storeStatistics(careUnit, deletedAnswers, MessageType.Answer);
    }
}

package se.skltp.mt.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import se.skltp.mt.core.entity.Answer;
import se.skltp.mt.core.service.AnswerService;

@Controller
@RequestMapping("/answers")
public class AnswersController {

    @Autowired
    private AnswerService answerService;

    private static final Logger log = LoggerFactory.getLogger(AnswersController.class);

    /**
     * Shows all questions
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView index(@RequestParam(required = false) String careUnit) {
        ModelAndView mav = new ModelAndView("answers/index");

        List<Answer> answers = answerService.getAllAnswersForCareUnit(careUnit);
        log.debug("Collection questions for careunit: {}, number of questions: {}.",
                new Object[] { careUnit, answers.size() });
        mav.addObject("careunit", careUnit);
        mav.addObject("answers", answers);

        return mav;
    }
}

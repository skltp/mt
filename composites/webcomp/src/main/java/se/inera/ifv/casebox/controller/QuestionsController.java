package se.inera.ifv.casebox.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.core.service.QuestionService;

@Controller
@RequestMapping("/questions")
public class QuestionsController {

    private QuestionService questionService;
    private static final Logger log = LoggerFactory.getLogger(QuestionsController.class);
    
    /**
     * Shows all questions
     * @return
     */
    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView index(@RequestParam(required=false) String careUnit) {
        ModelAndView mav = new ModelAndView("questions/index");
        
        List<Question> questions = questionService.getAllQuestionsForCareUnit(careUnit);
        log.debug("Collection questions for careunit: {}, number of questions: {}.", new Object[]{careUnit, questions.size()});
        mav.addObject("careunit", careUnit);
        mav.addObject("questions", questions);
        
        return mav;
    }
    
    public QuestionService getQuestionService() {
        return questionService;
    }

    @Resource
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
}

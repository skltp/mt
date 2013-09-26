package se.skltp.messagebox.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.service.StatisticService;

@Controller
@RequestMapping("/answers")
public class StatisticsController {

    @Autowired
    private StatisticService statisticService;

    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    /**
     * Shows all questions
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView index(@RequestParam(required = false) String careUnit) {
        ModelAndView mav = new ModelAndView("statistics/index");

        List<Statistic> statistics = statisticService.getStatisticsForDay(System.currentTimeMillis());
        mav.addObject("statistics", statistics);

        return mav;
    }
}

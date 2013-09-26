package se.skltp.messagebox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.service.StatisticService;

/**
 * Main controller to open the first page with some overview information and statistic about
 * the current state in the messagebox. 
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView start() {
        ModelAndView mav = new ModelAndView("start");
        List<Statistic> statistics = statisticService.getStatisticsForDay(System.currentTimeMillis());
        mav.addObject("statistics", statistics);
        return mav;
    }
}

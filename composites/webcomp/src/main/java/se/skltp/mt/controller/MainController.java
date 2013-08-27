package se.skltp.mt.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.skltp.mt.core.service.CareUnitInfo;
import se.skltp.mt.core.service.OverviewService;
import se.skltp.mt.core.service.StatisticService;
import se.skltp.mt.core.service.impl.StatisticInfo;

/**
 * Main controller to open the first page with some overview information and statistic about
 * the current state in the casebox. 
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private OverviewService overviewService;
    @Autowired
    private StatisticService statisticService;
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView start() {
        ModelAndView mav = new ModelAndView("start");
        List<CareUnitInfo> careUnits = overviewService.getCareUnitInfos(5);
        mav.addObject("careUnits", careUnits);
        
        // get statistic
        Collection<StatisticInfo> statistics = statisticService.findStatisticLastMonth();
        mav.addObject("statistics", statistics);
        return mav;
    }
}

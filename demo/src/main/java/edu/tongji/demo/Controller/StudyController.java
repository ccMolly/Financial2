package edu.tongji.demo.Controller;

import edu.tongji.demo.Model.IntroductionFile;
import edu.tongji.demo.Service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study")
public class StudyController {

    @Autowired
    private StudyService studyService;

    @GetMapping("/introduction")
    public Object getIntroduction(@RequestParam(value = "name")String name){
        IntroductionFile introductionFile= studyService.getFileByName(name);
        if (introductionFile == null){
            return "Not found";
        }else{
            return introductionFile.getContent();
        }
    }
}
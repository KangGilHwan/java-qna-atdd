package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

@Controller
public class QuestionController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("/questions")
    public String create(@LoginUser User loginUser, QuestionDto questionDto){
        qnaService.create(loginUser, questionDto.toQuestion());
        return "redirect:/";
    }
}

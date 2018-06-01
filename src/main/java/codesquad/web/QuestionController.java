package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, QuestionDto questionDto) {
        qnaService.create(loginUser, questionDto.toQuestion());
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @LoginUser User loginUser, QuestionDto updateQuestion) {
        qnaService.update(loginUser, id, updateQuestion.toQuestion());
        return String.format("redirect:/questions/%d", id);
    }
}

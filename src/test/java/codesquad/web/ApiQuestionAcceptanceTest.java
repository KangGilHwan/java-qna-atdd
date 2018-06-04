package codesquad.web;

import codesquad.domain.Question;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest{

    private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    @Test
    public void create_no_login(){
        QuestionDto questionDto = createDto();
        ResponseEntity<String> response = template().postForEntity("/api/questions", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void create_login(){
        QuestionDto newQuestion = createDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.debug("response : {}", response);

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto dbQuestion = template().getForObject(location, QuestionDto.class);
        assertThat(newQuestion, is(dbQuestion));
    }

    @Test
    public void update_login(){
        QuestionDto newQuestion = createDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.debug("response : {}", response);

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto updateQuestion = new QuestionDto("titleTest2", "contentsTest2");
        basicAuthTemplate().put(location, updateQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(updateQuestion, is(dbQuestion));
    }

    @Test
    public void update_다른_사용자(){
        QuestionDto newQuestion = createDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.debug("response : {}", response);

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto updateQuestion = new QuestionDto("titleTest2", "contentsTest2");
        basicAuthTemplate(findByUserId("riverway")).put(location, updateQuestion);

        QuestionDto dbQuestion = basicAuthTemplate(findByUserId("riverway")).getForObject(location, QuestionDto.class);
        assertThat(newQuestion, is(dbQuestion));
    }

    @Test
    public void delete_login(){
        QuestionDto newQuestion = createDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.debug("response : {}", response);

        String location = response.getHeaders().getLocation().getPath();
        basicAuthTemplate().delete(location);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        log.debug("dbQuestion : {}", dbQuestion);
        assertNull(dbQuestion);
    }

    @Test
    public void delete_다른_사용자(){
        QuestionDto newQuestion = createDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.debug("response : {}", response);

        String location = response.getHeaders().getLocation().getPath();
        basicAuthTemplate(findByUserId("riverway")).delete(location);

        QuestionDto dbQuestion = basicAuthTemplate(findByUserId("riverway")).getForObject(location, QuestionDto.class);
        assertThat(dbQuestion, is(newQuestion));
    }

    private QuestionDto createDto(){
        return new QuestionDto("titleTest", "contentsTest");
    }
}

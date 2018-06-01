package codesquad.web;

import codesquad.domain.QuestionRepository;
import com.sun.deploy.net.HttpResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Autowired
    private QuestionRepository questionRepository;
    private final Long testQuestionNum = Long.valueOf(1);

    @Test
    public void form(){
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void show(){
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d", testQuestionNum), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().contains(questionRepository.findById(testQuestionNum).get().getTitle()), is(true));
    }

    @Test
    public void create() {
        HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();

        String title = "test";
        builder.addParameter("title", title);
        builder.addParameter("contents", "test");

        HttpEntity<MultiValueMap<String, Object>> request = builder.build();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/questions", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertNotNull(questionRepository.findByTitle(title));
        assertTrue(response.getHeaders().getLocation().getPath().startsWith("/"));
    }

    @Test
    public void create_no_login() {
        HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();

        String title = "test";
        builder.addParameter("title", title);
        builder.addParameter("contents", "test");

        HttpEntity<MultiValueMap<String, Object>> request = builder.build();
        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void updateForm(){
        ResponseEntity<String> respose = template().getForEntity(String.format("/questions/%d/form", testQuestionNum), String.class);
        assertThat(respose.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void update_login() {
        HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();

        String title = "test";
        builder.addParameter("title", title);
        builder.addParameter("contents", "test");
        builder.addParameter("_method", "put");

        HttpEntity<MultiValueMap<String, Object>> request = builder.build();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/questions/%d", testQuestionNum), request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    @Test
    public void update_no_login() {
        HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();

        String title = "test";
        builder.addParameter("title", title);
        builder.addParameter("contents", "test");
        builder.addParameter("_method", "put");

        HttpEntity<MultiValueMap<String, Object>> request = builder.build();
        ResponseEntity<String> response = template().postForEntity(String.format("/questions/%d", testQuestionNum), request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}

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
}

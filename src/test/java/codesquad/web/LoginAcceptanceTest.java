package codesquad.web;

import codesquad.security.HttpSessionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import support.test.AcceptanceTest;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Test
    public void login() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userId", "riverway");
        params.add("password", "test");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, header);

        ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }
}

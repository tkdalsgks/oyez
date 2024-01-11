package kr.oyez.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.oyez.security.dto.ResponseDataCode;
import kr.oyez.security.dto.ResponseDataDTO;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();

        ResponseDataDTO responseDataDTO = new ResponseDataDTO();
        responseDataDTO.setCode(ResponseDataCode.ERROR);
        responseDataDTO.setStatus(ResponseDataCode.ERROR);
        responseDataDTO.setMessage(exception.getMessage());

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(mapper.writeValueAsString(responseDataDTO));
        response.getWriter().flush();
    }
}

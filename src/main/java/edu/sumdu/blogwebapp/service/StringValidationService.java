package edu.sumdu.blogwebapp.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class StringValidationService {

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    public String escapeHtml(String unsafeText) {
        return StringEscapeUtils.escapeHtml4(unsafeText);
    }
    public boolean isValidEmailAddress(String emailAddress) {
        return EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
    }
}

package egovframework.example.board.vo.validator;

import egovframework.example.board.vo.BoardVO;

import static egovframework.example.board.util.ExceptionMessage.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;


@Component
public class BoardValidator {
	private static final int TITLE_MAX_LENGTH = 100;
	private static final int CONTENT_MAX_LENGTH = 1000;
	private static final int MEMBER_MAX_LENGTH = 6;
	private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
	
	
	public void validate(BoardVO boardVO) {
		checkTitle(boardVO.getTitle());
		checkContent(boardVO.getContent());
		checkMember(boardVO.getMember());
		checkPassword(boardVO.getPassword());
	}
	
	private void checkTitle(String title) {
		if(isEmpty(title)) {
			throw new IllegalArgumentException(NOT_EMPTY_TITLE.getMessage());
		}
		if(title.length() > TITLE_MAX_LENGTH) {
			throw new IllegalArgumentException(LENGTH_OVER_TITLE.getMessage());
		}
	}
	
	private void checkContent(String content) {
		if(isEmpty(content)) {
			throw new IllegalArgumentException(NOT_EMPTY_CONTENT.getMessage());
		}
		if(content.length() > CONTENT_MAX_LENGTH) {
			throw new IllegalArgumentException(LENGTH_OVER_CONTENT.getMessage());
		}
	}
	
	private void checkMember(String member) {
		if(isEmpty(member)) {
			throw new IllegalArgumentException(NOT_EMPTY_MEMBER.getMessage());
		}
		if(member.length() > MEMBER_MAX_LENGTH) {
			throw new IllegalArgumentException(LENGTH_OVER_MEMBER.getMessage());
		}
	}
	
	private void checkPassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        
        if(!matcher.matches()) {
			throw new IllegalArgumentException(INVALID_PASSWORD.getMessage());
        }
	}

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}
}

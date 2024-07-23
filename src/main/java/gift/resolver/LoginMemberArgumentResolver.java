package gift.resolver;

import gift.annotation.LoginMember;
import gift.auth.JwtTokenUtil;
import gift.model.Member;
import gift.service.MemberService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(LoginMemberArgumentResolver.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginMember.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.error("Authorization header is missing or does not start with Bearer");
            throw new IllegalArgumentException("Authorization header must be provided and start with Bearer");
        }

        String token = authorizationHeader.substring(7);
        logger.debug("JWT Token: {}", token);

        Claims claims = jwtTokenUtil.getClaims(token);
        if (claims == null) {
            logger.error("Invalid JWT token");
            throw new IllegalArgumentException("Invalid JWT token");
        }

        Long memberId = Long.parseLong(claims.getSubject());
        Member member = memberService.findById(memberId);
        if (member == null) {
            logger.error("Member not found for id: {}", memberId);
            throw new IllegalArgumentException("Member not found");
        }

        return member;
    }
}

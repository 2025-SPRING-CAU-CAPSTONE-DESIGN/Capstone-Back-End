package com.capstone.storyforest.global.jwt;

import com.capstone.storyforest.user.dto.CustomUserDetails;
import com.capstone.storyforest.user.entity.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 1) shouldNotFilter 로 공개 경로는 아예 필터를 타지 않습니다.
 * 2) doFilterInternal 에서는 JwtException 을 잡아서
 *    “잘못된 토큰”일 땐 인증 시도도, 에러 전파도 하지 않고 조용히 넘어갑니다.
 */
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 인증이 필요 없는 경로라면 필터를 건너뜁니다.
        return "/login".equals(path)
                || "/join".equals(path)
                || path.startsWith("/users/") && path.endsWith("/check")
                || path.startsWith("/api/friends/notifications/stream")
                || path.startsWith("/api/sentence/");  // <-- 여기에 추가
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            // 헤더가 없으면 그냥 다음 필터로
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();

        try {
            // 만료 검사도 isExpired 내부에서 parseClaimsJws 를 쓰도록 수정해 두셨다면
            // 여기서 JwtException 이 던져질 수 있습니다.
            if (jwtUtil.isExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 유효한 토큰이면 사용자 정보 뽑아서 SecurityContext 에 세팅
            String username = jwtUtil.getUsername(token);
            String role     = jwtUtil.getRole(token);

            User user = new User();
            user.setUsername(username);
            user.setPassword("temppassword");
            user.setRole(role);

            CustomUserDetails userDetails =
                    new CustomUserDetails(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext()
                    .setAuthentication(auth);

        } catch (JwtException ex) {
            // 토큰 파싱·검증 실패 시에도 에러를 그대로 뿜지 않고
            // “인증 없이” 다음 필터로 넘어가게 합니다.
        }

        filterChain.doFilter(request, response);
    }
}

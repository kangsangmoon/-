package com.codeforcode.ui.solution;

import com.codeforcode.auth.jwt.TokenProvider;
import com.codeforcode.redis.token.TokenService;
import com.codeforcode.solution.dto.request.SolutionFindRequest;
import com.codeforcode.solution.dto.response.SolutionResponse;
import com.codeforcode.solution.repository.SolutionRepository;
import com.codeforcode.user.dto.UserResponse;
import com.codeforcode.user.repository.UserRepository;
import com.codeforcode.user.service.UserAuthService;
import com.codeforcode.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/solution")
public class SolutionUiController {

    private final SolutionRepository solutionRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @RequestMapping
    public String solutionList(Model model, HttpServletRequest request,@RequestBody SolutionFindRequest solutionFindRequest) {
        List<SolutionResponse> solutionList = solutionRepository.findAll(solutionFindRequest);
        model.addAttribute("solutions", solutionList);

        String token = HeaderUtil.resolveToken(request);
        if(tokenProvider.validateToken(token)){
            String name = tokenProvider.getAuthentication(token).getName();
            var userResponse = userRepository.findByUserId(name);
            model.addAttribute("user", userResponse);
            return "/auth/solution";
        }
        return "/non-auth/solution";
    }

    @RequestMapping("/{id}")
    public String solutionSolvePage(Model model, HttpServletRequest request, @PathVariable(value = "id") Long id) {
        SolutionResponse result = solutionRepository.findById(id);
        model.addAttribute("solution", result);

        String token = HeaderUtil.resolveToken(request);
        if(tokenProvider.validateToken(token)){
            String name = tokenProvider.getAuthentication(token).getName();
            var userResponse = userRepository.findByUserId(name);
            model.addAttribute("user", userResponse);
            return "/auth/solution";
        }
        return "/non-auth/solution";
    }
    //TODO Gemini 문제 받아오면 화면에 렌더링 해줘야해.
    //TODO 문제 검색 페이지도 만들어야해.
}

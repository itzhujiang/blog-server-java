package com.blog.blogserverjava.controller.blog;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.aboutme.AboutMeInfoResponse;
import com.blog.blogserverjava.dto.aboutme.AboutMeInfoWrapper;
import com.blog.blogserverjava.dto.aboutme.UpdateAboutMeRequest;
import com.blog.blogserverjava.service.blog.AboutMeService;
import com.blog.blogserverjava.utils.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog/about-me")
@RequiredArgsConstructor
public class AboutMeController {

    private final AboutMeService aboutMeService;

    @GetMapping("/info")
    public Result<AboutMeInfoWrapper> info() {
        AboutMeInfoResponse info = aboutMeService.getAboutMeInfo();
        return Result.success(new AboutMeInfoWrapper(
                List.of(info),
                new AboutMeInfoWrapper.Pagination(1, 1, 1L)
        ));
    }

    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody UpdateAboutMeRequest request, HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        aboutMeService.updateAboutMeInfo(request, user != null ? user.name() : null);
        return Result.success(null);
    }
}

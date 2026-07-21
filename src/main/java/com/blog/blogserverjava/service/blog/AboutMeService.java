package com.blog.blogserverjava.service.blog;

import com.blog.blogserverjava.dto.aboutme.AboutMeInfoResponse;
import com.blog.blogserverjava.dto.aboutme.UpdateAboutMeRequest;

public interface AboutMeService {
    AboutMeInfoResponse getAboutMeInfo();

    void updateAboutMeInfo(UpdateAboutMeRequest request, String operatorName);
}

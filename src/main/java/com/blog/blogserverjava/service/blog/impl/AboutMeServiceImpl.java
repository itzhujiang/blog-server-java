package com.blog.blogserverjava.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.aboutme.AboutMeInfoResponse;
import com.blog.blogserverjava.dto.aboutme.UpdateAboutMeRequest;
import com.blog.blogserverjava.dto.tool.ConfirmMediaResult;
import com.blog.blogserverjava.entity.AboutPage;
import com.blog.blogserverjava.entity.AboutPageMedia;
import com.blog.blogserverjava.entity.MediaFile;
import com.blog.blogserverjava.enums.AboutPageMediaUsage;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.AboutPageMapper;
import com.blog.blogserverjava.mapper.AboutPageMediaMapper;
import com.blog.blogserverjava.mapper.MediaFileMapper;
import com.blog.blogserverjava.service.blog.AboutMeService;
import com.blog.blogserverjava.service.tool.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AboutMeServiceImpl implements AboutMeService {

    private final AboutPageMapper aboutPageMapper;
    private final AboutPageMediaMapper aboutPageMediaMapper;
    private final MediaFileMapper mediaFileMapper;
    private final MediaFileService mediaFileService;

    @Override
    public AboutMeInfoResponse getAboutMeInfo() {
        List<AboutPage> pages = aboutPageMapper.selectList(null);
        if (pages.isEmpty()) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "关于我信息不存在");
        }
        AboutPage aboutPage = pages.get(0);

        List<AboutPageMedia> medias = aboutPageMediaMapper.selectList(
                new LambdaQueryWrapper<AboutPageMedia>().eq(AboutPageMedia::getAboutPageId, aboutPage.getId())
        );

        String avatarUrl = null;
        String contentUrl = null;
        if (!medias.isEmpty()) {
            List<Integer> mediaIds = medias.stream().map(AboutPageMedia::getMediaId).toList();
            Map<Integer, String> mediaUrlMap = mediaFileMapper.selectList(
                    new LambdaQueryWrapper<MediaFile>().in(MediaFile::getId, mediaIds)
            ).stream().collect(java.util.stream.Collectors.toMap(MediaFile::getId, MediaFile::getFileUrl));

            for (AboutPageMedia media : medias) {
                String url = mediaUrlMap.get(media.getMediaId());
                if (media.getUsageType() == AboutPageMediaUsage.AVATAR) {
                    avatarUrl = url;
                } else if (media.getUsageType() == AboutPageMediaUsage.CONTENT) {
                    contentUrl = url;
                }
            }
        }

        return new AboutMeInfoResponse(
                aboutPage.getId(),
                aboutPage.getTitle(),
                aboutPage.getNickname(),
                aboutPage.getJobTitle(),
                aboutPage.getPersonalTags(),
                aboutPage.getContactInfo(),
                aboutPage.getSocialLinks(),
                aboutPage.getSkills(),
                aboutPage.getTimeline(),
                aboutPage.getUpdatedAt(),
                avatarUrl,
                contentUrl
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAboutMeInfo(UpdateAboutMeRequest request, String operatorName) {
        AboutPage aboutPage = aboutPageMapper.selectById(request.getId());
        if (aboutPage == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "关于我信息不存在");
        }

        List<String> fileCodesToConfirm = new java.util.ArrayList<>();
        List<AboutPageMediaUsage> usagesToUpdate = new java.util.ArrayList<>();

        if (request.isUpdateAvatar() && StringUtils.hasText(request.getAvatarCode())) {
            fileCodesToConfirm.add(request.getAvatarCode());
            usagesToUpdate.add(AboutPageMediaUsage.AVATAR);
        }
        if (request.isUpdateContent() && StringUtils.hasText(request.getContentCode())) {
            fileCodesToConfirm.add(request.getContentCode());
            usagesToUpdate.add(AboutPageMediaUsage.CONTENT);
        }

        if (!fileCodesToConfirm.isEmpty()) {
            List<ConfirmMediaResult> confirmResults = mediaFileService.confirmTempMedia(fileCodesToConfirm);
            for (int i = 0; i < fileCodesToConfirm.size(); i++) {
                String code = fileCodesToConfirm.get(i);
                AboutPageMediaUsage usage = usagesToUpdate.get(i);
                ConfirmMediaResult result = confirmResults.stream()
                        .filter(r -> r.fileCode().equals(code))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(ResultCode.INTERNAL_ERROR,
                                usage == AboutPageMediaUsage.AVATAR ? "未找到头像文件" : "未找到内容文件"));

                // 删除该类型的旧关联
                aboutPageMediaMapper.delete(
                        new LambdaQueryWrapper<AboutPageMedia>()
                                .eq(AboutPageMedia::getAboutPageId, request.getId())
                                .eq(AboutPageMedia::getUsageType, usage)
                );

                // 创建新关联
                AboutPageMedia media = new AboutPageMedia();
                media.setAboutPageId(request.getId());
                media.setMediaId(result.mediaId());
                media.setUsageType(usage);
                aboutPageMediaMapper.insert(media);
            }
        }

        aboutPage.setNickname(operatorName);
        aboutPage.setJobTitle(request.getJobTitle());
        aboutPage.setPersonalTags(request.getPersonalTags());
        aboutPage.setContactInfo(request.getContactInfo());
        aboutPage.setSocialLinks(request.getSocialLinks());
        aboutPage.setSkills(request.getSkills());
        aboutPage.setTimeline(request.getTimeline());
        aboutPageMapper.updateById(aboutPage);
    }
}

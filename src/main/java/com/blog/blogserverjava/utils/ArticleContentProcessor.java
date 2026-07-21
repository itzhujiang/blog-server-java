package com.blog.blogserverjava.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理文章 Markdown 内容中的 img/video 标签：
 * 1. <img> 标签转换为标准 Markdown 语法 ![alt](url)
 * 2. <video> 标签保留结构，只替换 src 为永久路径
 * 3. 标准 Markdown 图片语法的 url 替换为永久路径
 */
public class ArticleContentProcessor {

    private static final Pattern IMG_TAG = Pattern.compile("<img\\s+([^>]*?)>", Pattern.CASE_INSENSITIVE);
    private static final Pattern VIDEO_TAG = Pattern.compile("<video\\s+([^>]*?)>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SRC_ATTR = Pattern.compile("src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALT_ATTR = Pattern.compile("alt=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
    private static final Pattern MD_IMAGE = Pattern.compile("!\\[([^\\]]*)]\\(([^)]+)\\)");

    public static String process(String content, Map<String, String> tempToPermanentMapping) {
        content = replaceImgTags(content, tempToPermanentMapping);
        content = replaceVideoTags(content, tempToPermanentMapping);
        content = replaceMarkdownImages(content, tempToPermanentMapping);
        return content;
    }

    private static String applyMapping(String src, Map<String, String> mapping) {
        String result = src;
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static String replaceImgTags(String content, Map<String, String> mapping) {
        Matcher matcher = IMG_TAG.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String attributes = matcher.group(1);
            Matcher srcMatcher = SRC_ATTR.matcher(attributes);
            Matcher altMatcher = ALT_ATTR.matcher(attributes);
            String src = srcMatcher.find() ? srcMatcher.group(1) : "";
            String alt = altMatcher.find() ? altMatcher.group(1) : "image";
            String newSrc = applyMapping(src, mapping);
            matcher.appendReplacement(sb, Matcher.quoteReplacement("![" + alt + "](" + newSrc + ")"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String replaceVideoTags(String content, Map<String, String> mapping) {
        Matcher matcher = VIDEO_TAG.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String attributes = matcher.group(1);
            Matcher srcMatcher = SRC_ATTR.matcher(attributes);
            String src = srcMatcher.find() ? srcMatcher.group(1) : "";
            String newSrc = applyMapping(src, mapping);
            String newAttributes = SRC_ATTR.matcher(attributes).replaceFirst(Matcher.quoteReplacement("src=\"" + newSrc + "\""));
            matcher.appendReplacement(sb, Matcher.quoteReplacement("<video " + newAttributes + ">"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String replaceMarkdownImages(String content, Map<String, String> mapping) {
        Matcher matcher = MD_IMAGE.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String alt = matcher.group(1);
            String url = matcher.group(2);
            String newUrl = applyMapping(url, mapping);
            matcher.appendReplacement(sb, Matcher.quoteReplacement("![" + alt + "](" + newUrl + ")"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

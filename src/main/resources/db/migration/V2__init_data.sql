insert into categories (name, slug, created_at, updated_at, deleted_at)
values
    ('技术学习', 'tech',(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('生活故事', 'life',(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('AI创作', 'ai',(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint);


insert into site_settings (setting_key, setting_value, setting_type, description, updated_at, created_at)
values
    ('site_title', '暖木博客', 'string', '网站标题', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('site_description', '一个探索科技、设计、生活及AI创作潜能的个人博客', 'string', '网站描述', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('site_keywords', '博客,技术,生活,AI,设计', 'string', '网站关键词', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('posts_per_page',12, 'number', '每页文章数量',(EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint ),
    ('enable_comments', true, 'boolean', '是否开启评论', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('comment_moderation', true, 'boolean', '评论是否需要审核', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint),
    ('footer_copyright', '© 2025 暖木博客. 版权所有.', 'string', '页脚版权信息', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint, (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint);

insert into about_page (
    title,
    nickname,
    job_title,
    personal_tags,
    contact_info,
    social_links,
    skills,
    timeline,
    updated_at
)
values (
           '关于我',
           '木心',
           '前端开发者 & UI设计师',
           '["热爱学习的技术人", "AI创作探索者"]',
           '{"email":"hello@mouxin.blog","github":"@mouxin-dev","wechat":"mouxin_chat"}',
           '{"twitter":"#","dribbble":"#","instagram":"#"}',
           '[
             {
               "category": "前端开发",
               "items": [
                 {"name": "React & Next.js", "level": 90},
                 {"name": "Vue & Nuxt.js", "level": 85},
                 {"name": "Tailwind CSS", "level": 95}
               ]
             },
             {
               "category": "UI/UX 设计",
               "items": [
                 {"name": "Figma", "level": 90},
                 {"name": "用户研究", "level": 75},
                 {"name": "原型设计", "level": 85}
               ]
             },
             {
               "category": "其他技能",
               "items": [
                 {"name": "Node.js", "level": 70},
                 {"name": "摄影", "level": 80},
                 {"name": "写作", "level": 88}
               ]
             }
           ]',
           '[
             {"timestamp": 1704067200000, "title": "开启博客之旅", "description": "创建\"暖木博客\"，记录技术与生活。"},
             {"timestamp": 1640995200000, "title": "首次参与开源项目", "description": "为一个小众UI库贡献了代码。"},
             {"timestamp": 1577808000000, "title": "毕业 & 第一份工作", "description":
             "作为前端开发工程师，正式踏入职场。"},
             {"timestamp": 1467302400000, "title": "写下第一行代码", "description": "Hello World! 开启了对编程世界的探索。"}
             ]',
           (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
       );


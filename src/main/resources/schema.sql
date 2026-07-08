-- ========================================
-- 重置（本地开发用）
-- ========================================

drop table if exists big_file_chunks cascade;
drop table if exists big_file_records cascade;
drop table if exists search_logs cascade;
drop table if exists temp_media cascade;
drop table if exists ai_session_memories cascade;
drop table if exists ai_global_chat_memories cascade;
drop table if exists ai_chat_messages cascade;
drop table if exists ai_chat_sessions cascade;
drop table if exists ai_chat_users cascade;
drop table if exists about_page_media cascade;
drop table if exists about_page cascade;
drop table if exists artwork_media cascade;
drop table if exists ai_artworks cascade;
drop table if exists article_media cascade;
drop table if exists article_categories cascade;
drop table if exists comments cascade;
drop table if exists articles cascade;
drop table if exists categories cascade;
drop table if exists media_files cascade;
drop table if exists admin_users cascade;
drop table if exists site_settings cascade;

-- ========================================
-- 枚举类型定义
-- ========================================

drop type if exists article_status cascade;
drop type if exists comment_status cascade;
drop type if exists article_media_usage cascade;
drop type if exists artwork_media_usage cascade;
drop type if exists artwork_status cascade;
drop type if exists setting_type cascade;
drop type if exists admin_status cascade;
drop type if exists about_page_media_usage cascade;
drop type if exists ai_chat_user_status cascade;
drop type if exists ai_chat_message_role cascade;
drop type if exists ai_chat_message_type cascade;
drop type if exists ai_global_chat_memory_category cascade;
drop type if exists big_file_status cascade;
drop type if exists big_file_chunk_status cascade;

create type article_status as enum ('draft', 'published', 'archived');
create type comment_status as enum ('pending', 'approved', 'spam', 'trash');
create type article_media_usage as enum ('thumbnail', 'attachment', 'content');
create type artwork_media_usage as enum ('main', 'thumbnail', 'process', 'variant');
create type artwork_status as enum ('draft', 'published');
create type setting_type as enum ('string', 'number', 'boolean', 'json');
create type admin_status as enum ('active', 'inactive', 'locked');
create type about_page_media_usage as enum ('avatar', 'content');
create type ai_chat_user_status as enum ('active', 'blocked');
create type ai_chat_message_role as enum ('user', 'assistant', 'system');
create type ai_chat_message_type as enum ('text', 'system');
create type ai_global_chat_memory_category as enum ('user', 'feedback', 'reference');
create type big_file_status as enum ('uploading', 'completed', 'failed');
create type big_file_chunk_status as enum ('pending', 'uploaded');

create table if not exists articles (
    id          serial primary key,
    title       varchar(255)   not null,
    slug        varchar(255)   not null,
    content     text           not null default '',
    excerpt     text,
    author_name varchar(100)   not null default '木心',
    reading_time integer       not null default 0,
    view_count  integer        not null default 0,
    status      article_status not null default 'published',
    published_at bigint,
    created_at  bigint         not null default 0,
    updated_at  bigint         not null default 0,
    deleted_at  bigint         not null default 0
);

create unique index if not exists idx_articles_slug on articles (slug);
create index if not exists idx_articles_status_published on articles (status, published_at);
create index if not exists idx_articles_author_name on articles (author_name);
create index if not exists idx_articles_created_at on articles (created_at);

create table if not exists categories (
    id         serial       primary key,
    name       varchar(100) not null,
    slug       varchar(100) not null,
    created_at bigint       not null default 0,
    updated_at bigint       not null default 0,
    deleted_at bigint       not null default 0
);

create unique index if not exists idx_categories_slug on categories (slug);

create table if not exists article_categories (
    article_id  integer not null references articles (id) on delete cascade,
    category_id integer not null references categories (id) on delete cascade,
    created_at  bigint  not null default 0,
    primary key (article_id, category_id)
);

create index if not exists idx_article_categories_article_id on article_categories (article_id);
create index if not exists idx_article_categories_category_id on article_categories (category_id);

create table if not exists comments (
    id           serial         primary key,
    article_id   integer        not null references articles (id) on delete cascade,
    parent_id    integer        references comments (id) on delete set null,
    author_name  varchar(100)   not null,
    is_author    boolean        not null default false,
    author_email varchar(255),
    author_phone varchar(255),
    author_ip    varchar(45),
    content      text           not null,
    status       comment_status not null default 'pending',
    like_count   integer        not null default 0,
    created_at   bigint         not null default 0,
    updated_at   bigint         not null default 0
);

create index if not exists idx_comments_article_status on comments (article_id, status);
create index if not exists idx_comments_parent_id on comments (parent_id);
create index if not exists idx_comments_created_at on comments (created_at);
create index if not exists idx_comments_author_ip on comments (author_ip);

create table if not exists media_files (
    id            serial       primary key,
    original_name varchar(255) not null,
    stored_name   varchar(255) not null,
    file_path     varchar(500) not null,
    file_url      varchar(500) not null,
    file_size     bigint,
    mime_type     varchar(100),
    width         integer,
    height        integer,
    alt_text      text,
    uploader_name varchar(100),
    file_hash     varchar(32)  not null default '',
    created_at    bigint       not null default 0,
    updated_at    bigint       not null default 0
);

create index if not exists idx_media_files_mime_type on media_files (mime_type);
create index if not exists idx_media_files_created_at on media_files (created_at);
create index if not exists idx_media_files_uploader_name on media_files (uploader_name);
create index if not exists idx_media_files_file_hash on media_files (file_hash);

create table if not exists article_media (
    id         serial              primary key,
    article_id integer             not null references articles (id) on delete cascade,
    media_id   integer             not null references media_files (id) on delete cascade,
    usage_type article_media_usage not null default 'attachment',
    sort_order integer             not null default 0,
    created_at bigint              not null default 0
);

create unique index if not exists idx_article_media_unique on article_media (article_id, media_id, usage_type);
create index if not exists idx_article_media_article_id on article_media (article_id);
create index if not exists idx_article_media_media_id on article_media (media_id);
create index if not exists idx_article_media_usage_type on article_media (usage_type);

create table if not exists temp_media (
    code          varchar(50)  primary key,
    original_name varchar(255) not null,
    stored_name   varchar(255) not null,
    file_path     varchar(500) not null,
    file_size     bigint       not null,
    mime_type     varchar(100) not null,
    expires_at    bigint       not null,
    is_used       boolean      not null default false,
    created_at    bigint       not null default 0
);

create index if not exists idx_temp_media_expires_at on temp_media (expires_at);
create index if not exists idx_temp_media_is_used on temp_media (is_used);

create table if not exists admin_users (
    id             serial       primary key,
    username       varchar(100) not null,
    email          varchar(255) not null,
    display_name   varchar(100),
    avatar_url     varchar(500),
    phone          varchar(20),
    password_hash  varchar(255) not null,
    password_salt  varchar(255) not null,
    status         admin_status not null default 'active',
    last_login_at  bigint,
    last_login_ip  varchar(45),
    login_attempts integer      not null default 0,
    locked_until   bigint,
    created_at     bigint       not null default 0,
    updated_at     bigint       not null default 0
);

create unique index if not exists idx_admin_users_username on admin_users (username);
create unique index if not exists idx_admin_users_email on admin_users (email);
create index if not exists idx_admin_users_status on admin_users (status);
create index if not exists idx_admin_users_last_login_ip on admin_users (last_login_ip);

create table if not exists site_settings (
    id            serial       primary key,
    setting_key   varchar(100) not null,
    setting_value text,
    setting_type  setting_type not null default 'string',
    description   text,
    created_at    bigint       not null default 0,
    updated_at    bigint       not null default 0
);

create unique index if not exists idx_site_settings_key on site_settings (setting_key);

create table if not exists about_page (
    id            serial       primary key,
    title         varchar(255) not null default '关于我',
    nickname      varchar(100),
    job_title     varchar(100),
    personal_tags jsonb,
    contact_info  jsonb,
    social_links  jsonb,
    skills        jsonb,
    timeline      jsonb,
    updated_at    bigint       not null default 0
);

create table if not exists about_page_media (
    id            serial                 primary key,
    about_page_id integer                not null references about_page (id) on delete cascade,
    media_id      integer                not null references media_files (id) on delete cascade,
    usage_type    about_page_media_usage not null default 'avatar',
    created_at    bigint                 not null default 0
);

create index if not exists idx_about_page_media_page_id on about_page_media (about_page_id);
create index if not exists idx_about_page_media_media_id on about_page_media (media_id);
create index if not exists idx_about_page_media_usage_type on about_page_media (usage_type);

create table if not exists ai_artworks (
    id              serial         primary key,
    title           varchar(255)   not null,
    slug            varchar(255)   not null,
    description     text,
    category        varchar(50),
    creation_prompt text,
    ai_model        varchar(100),
    view_count      integer        not null default 0,
    like_count      integer        not null default 0,
    is_featured     boolean        not null default false,
    sort_order      integer        not null default 0,
    status          artwork_status not null default 'published',
    created_at      bigint         not null default 0,
    updated_at      bigint         not null default 0
);

create unique index if not exists idx_ai_artworks_slug on ai_artworks (slug);
create index if not exists idx_ai_artworks_status_featured on ai_artworks (status, is_featured, sort_order);
create index if not exists idx_ai_artworks_category on ai_artworks (category);
create index if not exists idx_ai_artworks_created_at on ai_artworks (created_at);

create table if not exists artwork_media (
    id         serial              primary key,
    artwork_id integer             not null references ai_artworks (id) on delete cascade,
    media_id   integer             not null references media_files (id) on delete cascade,
    usage_type artwork_media_usage not null default 'main',
    sort_order integer             not null default 0,
    created_at bigint              not null default 0
);

create index if not exists idx_artwork_media_artwork_id on artwork_media (artwork_id);
create index if not exists idx_artwork_media_media_id on artwork_media (media_id);
create index if not exists idx_artwork_media_usage_type on artwork_media (usage_type);

create table if not exists ai_chat_users (
    id               serial              primary key,
    phone            varchar(20),
    status           ai_chat_user_status not null default 'active',
    last_verified_at bigint,
    last_login_ip    varchar(45),
    created_at       bigint              not null default 0,
    updated_at       bigint              not null default 0
);

create unique index if not exists idx_ai_chat_users_phone on ai_chat_users (phone);
create index if not exists idx_ai_chat_users_status on ai_chat_users (status);
create index if not exists idx_ai_chat_users_last_login_ip on ai_chat_users (last_login_ip);

create table if not exists ai_chat_sessions (
    id                   bigserial    primary key,
    session_id           varchar(255) not null,
    user_id              integer      not null references ai_chat_users (id) on delete restrict,
    title                varchar(255) not null,
    last_message_preview text,
    last_message_at      bigint       not null default 0,
    created_at           bigint       not null default 0,
    updated_at           bigint       not null default 0,
    deleted_at           bigint       not null default 0
);

create unique index if not exists idx_ai_chat_sessions_session_id on ai_chat_sessions (session_id);
create index if not exists idx_ai_chat_sessions_user_deleted_last_message on ai_chat_sessions (user_id, deleted_at, last_message_at desc);
create index if not exists idx_ai_chat_sessions_user_created_at on ai_chat_sessions (user_id, created_at desc);

create table if not exists ai_chat_messages (
    id           bigserial            primary key,
    message_id   varchar(255)         not null,
    session_id   bigint               not null references ai_chat_sessions (id) on delete restrict,
    role         ai_chat_message_role not null,
    message_type ai_chat_message_type not null default 'text',
    content      text                 not null,
    created_at   bigint               not null default 0,
    updated_at   bigint               not null default 0
);

create unique index if not exists idx_ai_chat_messages_message_id on ai_chat_messages (message_id);
create index if not exists idx_ai_chat_messages_session_created_at on ai_chat_messages (session_id, created_at asc);

create table if not exists ai_global_chat_memories (
    id               serial                         primary key,
    user_id          integer                        not null,
    category         ai_global_chat_memory_category,
    content          text,
    access_count     integer                        not null default 0,
    last_accessed_at bigint,
    skip_index       boolean                        not null default false,
    created_at       bigint                         not null default 0,
    updated_at       bigint                         not null default 0,
    deleted_at       bigint                         not null default 0
);

create index if not exists idx_ai_global_chat_memories_user_category on ai_global_chat_memories (user_id, category);
create index if not exists idx_ai_global_chat_memories_user_last_accessed on ai_global_chat_memories (user_id, last_accessed_at);

create table if not exists ai_session_memories (
    id              serial       primary key,
    user_id         integer      not null,
    thread_id       varchar(255) not null,
    content         text,
    last_message_id varchar(255) not null,
    created_at      bigint       not null default 0,
    updated_at      bigint       not null default 0
);

create unique index if not exists idx_ai_session_memories_thread_id on ai_session_memories (thread_id);
create index if not exists idx_ai_session_memories_user_id on ai_session_memories (user_id);

create table if not exists big_file_records (
    id            serial          primary key,
    identifier    varchar(36)     not null,
    original_name varchar(255)    not null,
    total_size    bigint          not null,
    chunk_size    integer         not null,
    total_chunks  integer         not null,
    mime_type     varchar(100)    not null,
    status        big_file_status not null default 'uploading',
    file_hash     varchar(32),
    stored_name   varchar(255),
    file_path     varchar(500),
    file_url      varchar(500),
    created_at    bigint          not null default 0,
    completed_at  bigint
);

create unique index if not exists idx_big_file_records_identifier on big_file_records (identifier);
create index if not exists idx_big_file_records_status on big_file_records (status);
create index if not exists idx_big_file_records_created_at on big_file_records (created_at);

create table if not exists big_file_chunks (
    id              serial                primary key,
    file_identifier varchar(36)           not null,
    chunk_number    integer               not null,
    chunk_size      integer               not null,
    chunk_hash      varchar(32),
    chunk_path      varchar(500)          not null,
    status          big_file_chunk_status not null default 'pending',
    uploaded_at     bigint
);

create unique index if not exists idx_big_file_chunks_identifier_number on big_file_chunks (file_identifier, chunk_number);
create index if not exists idx_big_file_chunks_file_identifier on big_file_chunks (file_identifier);

create table if not exists search_logs (
    id            serial       primary key,
    query         varchar(255) not null,
    results_count integer      not null default 0,
    ip_address    varchar(45),
    user_agent    text,
    created_at    bigint       not null default 0
);

create index if not exists idx_search_logs_query on search_logs (query);
create index if not exists idx_search_logs_created_at on search_logs (created_at);
create index if not exists idx_search_logs_ip_address on search_logs (ip_address);
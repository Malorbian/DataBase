-- V1__init.sql
-- Target: PostgreSQL (UUID, DATE, DOUBLE PRECISION)

-- =========================
-- Lookup / master tables
-- =========================

CREATE TABLE organisation (
                              id   UUID PRIMARY KEY,
                              name TEXT NOT NULL,
                              CONSTRAINT uk_organisation_name UNIQUE (name)
);

CREATE TABLE person (
                        id         UUID PRIMARY KEY,
                        name       TEXT NOT NULL,
                        discipline TEXT NOT NULL,
                        CONSTRAINT uk_person_name_discipline UNIQUE (name, discipline)
);

CREATE TABLE genre (
                       id   UUID PRIMARY KEY,
                       name TEXT NOT NULL,
                       CONSTRAINT uk_genre_name UNIQUE (name)
);

CREATE TABLE tag (
                     id   UUID PRIMARY KEY,
                     name TEXT NOT NULL,
                     CONSTRAINT uk_tag_name UNIQUE (name)
);

CREATE TABLE franchise (
                           id   UUID PRIMARY KEY,
                           name TEXT,
                           CONSTRAINT uk_franchise_name UNIQUE (name)
);

CREATE TABLE rating_platform (
                                 id         UUID PRIMARY KEY,
                                 name       TEXT NOT NULL,
                                 discipline TEXT NOT NULL,
                                 CONSTRAINT uk_rating_platform_name_discipline UNIQUE (name, discipline)
);

CREATE TABLE series (
                        id            UUID PRIMARY KEY,
                        name          TEXT NOT NULL,
                        discipline    TEXT NOT NULL,
                        season_number INT  NOT NULL DEFAULT 0,
                        episode_count INT  NOT NULL DEFAULT 0,
                        current_episode INT NOT NULL DEFAULT 0,
                        current_season  INT NOT NULL DEFAULT 0,
                        state         TEXT NOT NULL DEFAULT 'UNKNOWN',
                        CONSTRAINT uk_series_name_discipline UNIQUE (name, discipline)
);

-- =========================
-- Media base (JOINED inheritance root)
-- =========================

CREATE TABLE media (
                       media_id        UUID PRIMARY KEY,
                       title           TEXT NOT NULL,
                       organisation_id UUID NULL,
                       person_id       UUID NULL,
                       discipline      TEXT NOT NULL,
                       state           TEXT NOT NULL DEFAULT 'UNKNOWN',
                       last_activity   DATE NULL,
                       link            TEXT NULL,

    -- Either a primary person OR a primary organization must be set (exactly one non-null)
                       CONSTRAINT ck_media_primary_owner_xor
                           CHECK (
                               (organisation_id IS NULL AND person_id IS NOT NULL)
                                   OR
                               (organisation_id IS NOT NULL AND person_id IS NULL)
                               ),

                       CONSTRAINT fk_media_organisation
                           FOREIGN KEY (organisation_id) REFERENCES organisation(id),

                       CONSTRAINT fk_media_person
                           FOREIGN KEY (person_id) REFERENCES person(id),

                       CONSTRAINT uk_media_title_org_discipline UNIQUE (title, organisation_id, discipline),
                       CONSTRAINT uk_media_title_person_discipline UNIQUE (title, person_id, discipline)
);

-- Helpful indexes for FK columns (Postgres does not auto-index FKs)
CREATE INDEX ix_media_organisation_id ON media(organisation_id);
CREATE INDEX ix_media_person_id       ON media(person_id);

-- =========================
-- Media subtypes (JOINED)
-- PK = FK to media(media_id)
-- =========================

CREATE TABLE game (
                      media_id UUID PRIMARY KEY,
                      version  TEXT NULL,
                      CONSTRAINT fk_game_media
                          FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE
);

CREATE TABLE art (
                     media_id UUID PRIMARY KEY,
                     medium   TEXT NULL,
                     CONSTRAINT fk_art_media
                         FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE
);

CREATE TABLE music (
                       media_id UUID PRIMARY KEY,
                       album    TEXT NULL,
                       CONSTRAINT fk_music_media
                           FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE
);

CREATE TABLE story (
                       media_id    UUID PRIMARY KEY,
                       story_type  TEXT NULL,
                       CONSTRAINT fk_story_media
                           FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE
);

CREATE TABLE video (
                       media_id           UUID PRIMARY KEY,
                       length_in_minutes  DOUBLE PRECISION NULL,
                       video_type         TEXT NULL,
                       series_id          UUID NULL,

                       CONSTRAINT fk_video_media
                           FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE,

                       CONSTRAINT fk_video_series
                           FOREIGN KEY (series_id) REFERENCES series(id)
);

CREATE INDEX ix_video_series_id ON video(series_id);

-- =========================
-- Link tables / relations (id-only in JPA, FK enforced in DB)
-- =========================

CREATE TABLE media_genre (
                             id       UUID PRIMARY KEY,
                             media_id UUID NOT NULL,
                             genre_id UUID NOT NULL,

                             CONSTRAINT fk_media_genre_media
                                 FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE,

                             CONSTRAINT fk_media_genre_genre
                                 FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE RESTRICT,

                             CONSTRAINT uk_media_genre_media_genre UNIQUE (media_id, genre_id)
);

CREATE INDEX ix_media_genre_media_id ON media_genre(media_id);
CREATE INDEX ix_media_genre_genre_id ON media_genre(genre_id);

CREATE TABLE media_tag (
                           id       UUID PRIMARY KEY,
                           media_id UUID NOT NULL,
                           tag_id   UUID NOT NULL,

                           CONSTRAINT fk_media_tag_media
                               FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE,

                           CONSTRAINT fk_media_tag_tag
                               FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE RESTRICT,

                           CONSTRAINT uk_media_tag_media_tag UNIQUE (media_id, tag_id)
);

CREATE INDEX ix_media_tag_media_id ON media_tag(media_id);
CREATE INDEX ix_media_tag_tag_id   ON media_tag(tag_id);

CREATE TABLE credit (
                        id       UUID PRIMARY KEY,
                        media_id UUID NOT NULL,
                        person_id UUID NOT NULL,
                        role     TEXT NOT NULL,

                        CONSTRAINT fk_credit_media
                            FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE,

                        CONSTRAINT fk_credit_person
                            FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE RESTRICT,

                        CONSTRAINT uk_credit_media_person_role UNIQUE (media_id, person_id, role)
);

CREATE INDEX ix_credit_media_id  ON credit(media_id);
CREATE INDEX ix_credit_person_id ON credit(person_id);

CREATE TABLE rating (
                        id          UUID PRIMARY KEY,
                        platform_id UUID NOT NULL,
                        media_id    UUID NOT NULL,
                        rating      DOUBLE PRECISION NOT NULL DEFAULT 0,

                        CONSTRAINT fk_rating_platform
                            FOREIGN KEY (platform_id) REFERENCES rating_platform(id) ON DELETE RESTRICT,

                        CONSTRAINT fk_rating_media
                            FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE CASCADE,

                        CONSTRAINT uk_rating_platform_media UNIQUE (platform_id, media_id)
);

CREATE INDEX ix_rating_platform_id ON rating(platform_id);
CREATE INDEX ix_rating_media_id    ON rating(media_id);

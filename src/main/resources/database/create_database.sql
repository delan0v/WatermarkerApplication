CREATE TABLE W_USER (
  user_id       INTEGER                NOT NULL,
  user_name     CHARACTER VARYING(255) NOT NULL,
  user_surename CHARACTER VARYING(255) NOT NULL,
  user_login    CHARACTER VARYING(255) NOT NULL,
  user_email    CHARACTER VARYING(255) NOT NULL,
  user_password CHARACTER VARYING(255) NOT NULL
);
CREATE TABLE W_LOGO (
  logo_id       INTEGER       NOT NULL,
  user_id       INTEGER       NOT NULL,
  logo_content  BYTEA         NOT NULL,
  logo_name     VARCHAR(255)  NOT NULL
);

CREATE TABLE W_DOCUMENT (
  document_id              INTEGER                  NOT NULL,
  user_id                  INTEGER                  NOT NULL,
  document_name            CHARACTER VARYING(255)   NOT NULL,
  document_document        CHARACTER VARYING(255)   NOT NULL,
  document_hash_code       CHARACTER VARYING(255)   NOT NULL
);

CREATE TABLE W_WATERMARK (
  watermark_id        INTEGER NOT NULL,
  document_id         INTEGER NOT NULL,
  watermark_watermark BYTEA   NOT NULL,
  watermark_text      CHARACTER VARYING(255) NOT NULL,
  watermark_dct       DOUBLE PRECISION
);

ALTER TABLE W_USER
  OWNER TO postgres;
ALTER TABLE W_DOCUMENT
  OWNER TO postgres;
ALTER TABLE w_watermark
  OWNER TO postgres;

CREATE SEQUENCE id_user_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
CREATE SEQUENCE id_document_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
CREATE SEQUENCE id_watermark_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
CREATE SEQUENCE id_logo_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE id_user_seq
  OWNER TO postgres;
ALTER SEQUENCE id_user_seq OWNED BY W_USER.user_id;
ALTER TABLE ONLY W_USER
  ALTER COLUMN user_id SET DEFAULT nextval('id_user_seq' :: REGCLASS);

ALTER TABLE id_document_seq
  OWNER TO postgres;
ALTER SEQUENCE id_document_seq OWNED BY W_DOCUMENT.document_id;
ALTER TABLE ONLY W_DOCUMENT
  ALTER COLUMN document_id SET DEFAULT nextval('id_document_seq' :: REGCLASS);

ALTER TABLE id_watermark_seq
  OWNER TO postgres;
ALTER SEQUENCE id_watermark_seq OWNED BY w_watermark.watermark_id;
ALTER TABLE ONLY w_watermark
  ALTER COLUMN watermark_id SET DEFAULT nextval('id_watermark_seq' :: REGCLASS);

ALTER TABLE id_logo_seq
  OWNER TO postgres;
ALTER SEQUENCE id_logo_seq OWNED BY w_logo.logo_id;
ALTER TABLE ONLY w_logo
  ALTER COLUMN logo_id SET DEFAULT nextval('id_logo_seq' :: REGCLASS);


ALTER TABLE ONLY W_USER
  ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);
ALTER TABLE ONLY W_DOCUMENT
  ADD CONSTRAINT document_pkey PRIMARY KEY (document_id);
ALTER TABLE ONLY w_watermark
  ADD CONSTRAINT watermark_pkey PRIMARY KEY (watermark_id);

ALTER TABLE ONLY W_DOCUMENT
  ADD CONSTRAINT w_document_user_id_fkey FOREIGN KEY (user_id) REFERENCES W_USER (user_id);
ALTER TABLE ONLY w_watermark
  ADD CONSTRAINT w_watermark_document_id_fkey FOREIGN KEY (document_id) REFERENCES W_DOCUMENT (document_id);

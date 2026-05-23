--
-- PostgreSQL database dump
--

\restrict 4Aiya4Rs06y0PpuaBSiUoFP8WM6XYYPh9HvnQxMvIjK01244pSH6sYaNKfPZH3k

-- Dumped from database version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: citext; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;


--
-- Name: EXTENSION citext; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

--
-- Name: auditoria_eventos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
)
PARTITION BY RANGE (criado_em);


SET default_table_access_method = heap;

--
-- Name: auditoria_eventos_2025_01; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_01 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_02; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_02 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_03; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_03 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_04; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_04 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_05; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_05 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_06; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_06 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_07; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_07 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_08; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_08 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_09; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_09 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_10; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_10 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_11; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_11 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2025_12; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2025_12 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_01; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_01 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_02; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_02 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_03; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_03 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_04; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_04 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_05; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_05 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: auditoria_eventos_2026_06; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria_eventos_2026_06 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    correlation_id uuid NOT NULL,
    usuario_id uuid,
    entidade character varying(50) NOT NULL,
    entidade_id uuid NOT NULL,
    acao character varying(30) NOT NULL,
    dados_anteriores jsonb,
    dados_novos jsonb,
    ip_origem inet,
    user_agent text,
    origem character varying(20),
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT auditoria_eventos_acao_check CHECK (((acao)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'LOGIN'::character varying, 'LOGOUT'::character varying, 'UPLOAD'::character varying, 'CLASSIFY'::character varying, 'CONFIRM'::character varying, 'REJECT'::character varying, 'EXPORT'::character varying, 'REPROCESS'::character varying, 'API_KEY_GEN'::character varying, 'PASSWORD_CHANGE'::character varying])::text[]))),
    CONSTRAINT auditoria_eventos_origem_check CHECK (((origem)::text = ANY ((ARRAY['web'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying, 'api'::character varying, 'sistema'::character varying])::text[])))
);


--
-- Name: categoria_thresholds; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categoria_thresholds (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    categoria_id uuid NOT NULL,
    threshold_auto smallint DEFAULT 70 NOT NULL,
    threshold_alerta smallint DEFAULT 50 NOT NULL,
    ambiguidade_alta boolean DEFAULT false NOT NULL,
    CONSTRAINT categoria_thresholds_threshold_alerta_check CHECK (((threshold_alerta >= 0) AND (threshold_alerta <= 100))),
    CONSTRAINT categoria_thresholds_threshold_auto_check CHECK (((threshold_auto >= 0) AND (threshold_auto <= 100)))
);


--
-- Name: categorias; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categorias (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nome character varying(100) NOT NULL,
    categoria_pai_id uuid,
    tipo character varying(10) NOT NULL,
    icone character varying(50),
    cor_hex character(7),
    padrao boolean DEFAULT false,
    usuario_id uuid,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT categorias_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['receita'::character varying, 'gasto'::character varying, 'ambos'::character varying])::text[])))
);


--
-- Name: classificacao_logs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.classificacao_logs (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    transacao_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    estrategia character varying(20) NOT NULL,
    modelo_ia_versao character varying(50),
    prompt_versao character varying(20),
    prompt_hash character varying(64),
    resposta_raw text,
    categoria_sugerida uuid,
    confianca smallint NOT NULL,
    tokens_usados integer,
    latencia_ms integer,
    categoria_final uuid,
    corrigida boolean DEFAULT false NOT NULL,
    corrigida_em timestamp with time zone,
    motivo_correcao text,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT classificacao_logs_confianca_check CHECK (((confianca >= 0) AND (confianca <= 100))),
    CONSTRAINT classificacao_logs_estrategia_check CHECK (((estrategia)::text = ANY ((ARRAY['regra_usuario'::character varying, 'dicionario'::character varying, 'ia_api'::character varying, 'fallback'::character varying])::text[])))
);


--
-- Name: consentimentos_lgpd; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.consentimentos_lgpd (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    tipo character varying(50) NOT NULL,
    versao_politica character varying(20) NOT NULL,
    consentido boolean NOT NULL,
    ip_origem inet,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    revogado_em timestamp with time zone,
    revogado_motivo text,
    CONSTRAINT consentimentos_lgpd_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['tratamento_dados_financeiros'::character varying, 'uso_ia'::character varying, 'armazenamento_extrato'::character varying, 'bot_whatsapp'::character varying, 'bot_telegram'::character varying])::text[])))
);


--
-- Name: contas_financeiras; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contas_financeiras (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    nome character varying(100) NOT NULL,
    tipo character varying(20) NOT NULL,
    banco character varying(100),
    saldo_inicial numeric(15,2) DEFAULT 0 NOT NULL,
    padrao boolean DEFAULT false,
    ativa boolean DEFAULT true,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    atualizado_em timestamp with time zone,
    CONSTRAINT contas_financeiras_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['corrente'::character varying, 'poupanca'::character varying, 'cartao'::character varying, 'dinheiro'::character varying, 'investimento'::character varying])::text[])))
);


--
-- Name: extratos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.extratos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    conta_id uuid NOT NULL,
    arquivo_nome character varying(255),
    arquivo_uuid character varying(255) NOT NULL,
    hash_arquivo character varying(64) NOT NULL,
    banco_detectado character varying(100),
    parser_versao_id uuid,
    score_extracao numeric(4,3),
    periodo_inicio date,
    periodo_fim date,
    status character varying(30) DEFAULT 'upload_recebido'::character varying NOT NULL,
    total_lancamentos integer DEFAULT 0 NOT NULL,
    lancamentos_confirmados integer DEFAULT 0 NOT NULL,
    lancamentos_pendentes integer DEFAULT 0 NOT NULL,
    lancamentos_ignorados integer DEFAULT 0 NOT NULL,
    versao integer DEFAULT 1 NOT NULL,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    atualizado_em timestamp with time zone,
    CONSTRAINT extratos_status_check CHECK (((status)::text = ANY ((ARRAY['upload_recebido'::character varying, 'validando'::character varying, 'na_fila'::character varying, 'extraindo'::character varying, 'classificando'::character varying, 'aguardando_ia'::character varying, 'pendente_revisao'::character varying, 'parcialmente_revisado'::character varying, 'concluido'::character varying, 'erro_formato'::character varying, 'erro_extracao'::character varying, 'erro_classificacao'::character varying, 'erro_timeout'::character varying, 'cancelado'::character varying, 'reprocessando'::character varying])::text[])))
);


--
-- Name: notificacoes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notificacoes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    canal character varying(20) NOT NULL,
    tipo character varying(50) NOT NULL,
    titulo character varying(255),
    mensagem text,
    enviada boolean DEFAULT false NOT NULL,
    enviada_em timestamp with time zone,
    erro text,
    tentativas smallint DEFAULT 0 NOT NULL,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT notificacoes_canal_check CHECK (((canal)::text = ANY ((ARRAY['whatsapp'::character varying, 'telegram'::character varying, 'email'::character varying])::text[])))
);


--
-- Name: parser_versoes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.parser_versoes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    banco character varying(100) NOT NULL,
    versao character varying(20) NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    score_qualidade numeric(4,3),
    total_usos integer DEFAULT 0 NOT NULL,
    total_erros integer DEFAULT 0 NOT NULL,
    descricao text,
    depreciado_em timestamp with time zone
);


--
-- Name: processamento_jobs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.processamento_jobs (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    extrato_id uuid,
    tipo character varying(30) NOT NULL,
    status character varying(30) DEFAULT 'enfileirado'::character varying NOT NULL,
    tentativas smallint DEFAULT 0 NOT NULL,
    max_tentativas smallint DEFAULT 3 NOT NULL,
    payload jsonb,
    erro_mensagem text,
    worker_id character varying(100),
    lock_expires_at timestamp with time zone,
    correlation_id uuid,
    enfileirado_em timestamp with time zone DEFAULT now() NOT NULL,
    iniciado_em timestamp with time zone,
    concluido_em timestamp with time zone,
    proximo_retry timestamp with time zone,
    CONSTRAINT processamento_jobs_status_check CHECK (((status)::text = ANY ((ARRAY['enfileirado'::character varying, 'iniciando'::character varying, 'processando'::character varying, 'aguardando_ia'::character varying, 'concluido'::character varying, 'falha_ia'::character varying, 'falha_parser'::character varying, 'timeout'::character varying, 'retry_1'::character varying, 'retry_2'::character varying, 'retry_3'::character varying, 'dead_letter'::character varying, 'cancelado'::character varying])::text[]))),
    CONSTRAINT processamento_jobs_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['extracao_pdf'::character varying, 'classificacao_ia'::character varying, 'notificacao'::character varying, 'geracao_pdf'::character varying, 'snapshot'::character varying, 'anonimizacao'::character varying])::text[])))
);


--
-- Name: regras_classificacao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.regras_classificacao (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    termo character varying(255) NOT NULL,
    categoria_id uuid NOT NULL,
    subcategoria character varying(100),
    score_confianca numeric(4,3) DEFAULT 1.000 NOT NULL,
    vezes_aplicada integer DEFAULT 0 NOT NULL,
    vezes_corretas integer DEFAULT 0 NOT NULL,
    vezes_incorretas integer DEFAULT 0 NOT NULL,
    correcoes_consecutivas smallint DEFAULT 0 NOT NULL,
    congelada boolean DEFAULT false NOT NULL,
    congelada_motivo text,
    congelada_ate date,
    criada_por character varying(20) DEFAULT 'usuario'::character varying NOT NULL,
    ultima_aplicacao timestamp with time zone,
    ultima_correcao timestamp with time zone,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT regras_classificacao_criada_por_check CHECK (((criada_por)::text = ANY ((ARRAY['usuario'::character varying, 'ia_auto'::character varying, 'admin'::character varying])::text[])))
);


--
-- Name: snapshots_financeiros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.snapshots_financeiros (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    conta_id uuid,
    ano smallint NOT NULL,
    mes smallint NOT NULL,
    saldo_inicial numeric(15,2) NOT NULL,
    total_receitas numeric(15,2) DEFAULT 0 NOT NULL,
    total_gastos numeric(15,2) DEFAULT 0 NOT NULL,
    saldo_final numeric(15,2) NOT NULL,
    fechado boolean DEFAULT false NOT NULL,
    fechado_em timestamp with time zone,
    CONSTRAINT snapshots_financeiros_mes_check CHECK (((mes >= 1) AND (mes <= 12)))
);


--
-- Name: transacoes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.transacoes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    conta_id uuid NOT NULL,
    extrato_id uuid,
    transacao_estorno_id uuid,
    tipo character varying(10) NOT NULL,
    descricao_original text,
    descricao_usuario character varying(255),
    descricao_normalizada character varying(255),
    valor numeric(15,2) NOT NULL,
    data_transacao date NOT NULL,
    data_lancamento timestamp with time zone DEFAULT now() NOT NULL,
    categoria_id uuid,
    subcategoria character varying(100),
    estabelecimento character varying(255),
    origem character varying(20) DEFAULT 'manual'::character varying NOT NULL,
    status_revisao character varying(30) DEFAULT 'extraida'::character varying NOT NULL,
    confianca_ia smallint,
    recorrente boolean DEFAULT false,
    periodo_recorrencia character varying(20),
    observacao text,
    deleted_at timestamp with time zone,
    versao integer DEFAULT 1 NOT NULL,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    atualizado_em timestamp with time zone,
    CONSTRAINT transacoes_confianca_ia_check CHECK (((confianca_ia >= 0) AND (confianca_ia <= 100))),
    CONSTRAINT transacoes_origem_check CHECK (((origem)::text = ANY ((ARRAY['manual'::character varying, 'pdf'::character varying, 'api'::character varying])::text[]))),
    CONSTRAINT transacoes_status_revisao_check CHECK (((status_revisao)::text = ANY ((ARRAY['extraida'::character varying, 'classificada'::character varying, 'pendente_revisao'::character varying, 'confirmada'::character varying, 'ignorada'::character varying, 'arquivada'::character varying])::text[]))),
    CONSTRAINT transacoes_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['receita'::character varying, 'gasto'::character varying])::text[]))),
    CONSTRAINT transacoes_valor_check CHECK ((valor > (0)::numeric))
);


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    codigo character varying(12) NOT NULL,
    cpf character varying(255) NOT NULL,
    cpf_hash character varying(64),
    nome_completo character varying(150) NOT NULL,
    email character varying(255) NOT NULL,
    senha_hash character varying(255) NOT NULL,
    telefone character varying(20),
    telegram_chat_id bigint,
    whatsapp_id character varying(100),
    saldo_inicial_definido boolean DEFAULT false NOT NULL,
    moeda character(3) DEFAULT 'BRL'::bpchar NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    email_verificado boolean DEFAULT false NOT NULL,
    token_verificacao character varying(100),
    token_verificacao_expira timestamp with time zone,
    ultimo_login timestamp with time zone,
    tentativas_login smallint DEFAULT 0 NOT NULL,
    bloqueado_ate timestamp with time zone,
    refresh_token_hash character varying(255),
    api_key_hash character varying(255),
    deleted_at timestamp with time zone,
    versao integer DEFAULT 1 NOT NULL,
    criado_em timestamp with time zone DEFAULT now() NOT NULL,
    atualizado_em timestamp with time zone
);


--
-- Name: auditoria_eventos_2025_01; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_01 FOR VALUES FROM ('2025-01-01 00:00:00+00') TO ('2025-02-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_02; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_02 FOR VALUES FROM ('2025-02-01 00:00:00+00') TO ('2025-03-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_03; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_03 FOR VALUES FROM ('2025-03-01 00:00:00+00') TO ('2025-04-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_04; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_04 FOR VALUES FROM ('2025-04-01 00:00:00+00') TO ('2025-05-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_05; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_05 FOR VALUES FROM ('2025-05-01 00:00:00+00') TO ('2025-06-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_06; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_06 FOR VALUES FROM ('2025-06-01 00:00:00+00') TO ('2025-07-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_07; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_07 FOR VALUES FROM ('2025-07-01 00:00:00+00') TO ('2025-08-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_08; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_08 FOR VALUES FROM ('2025-08-01 00:00:00+00') TO ('2025-09-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_09; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_09 FOR VALUES FROM ('2025-09-01 00:00:00+00') TO ('2025-10-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_10; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_10 FOR VALUES FROM ('2025-10-01 00:00:00+00') TO ('2025-11-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_11; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_11 FOR VALUES FROM ('2025-11-01 00:00:00+00') TO ('2025-12-01 00:00:00+00');


--
-- Name: auditoria_eventos_2025_12; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2025_12 FOR VALUES FROM ('2025-12-01 00:00:00+00') TO ('2026-01-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_01; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_01 FOR VALUES FROM ('2026-01-01 00:00:00+00') TO ('2026-02-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_02; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_02 FOR VALUES FROM ('2026-02-01 00:00:00+00') TO ('2026-03-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_03; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_03 FOR VALUES FROM ('2026-03-01 00:00:00+00') TO ('2026-04-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_04; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_04 FOR VALUES FROM ('2026-04-01 00:00:00+00') TO ('2026-05-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_05; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_05 FOR VALUES FROM ('2026-05-01 00:00:00+00') TO ('2026-06-01 00:00:00+00');


--
-- Name: auditoria_eventos_2026_06; Type: TABLE ATTACH; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria_eventos ATTACH PARTITION public.auditoria_eventos_2026_06 FOR VALUES FROM ('2026-06-01 00:00:00+00') TO ('2026-07-01 00:00:00+00');


--
-- Data for Name: auditoria_eventos_2025_01; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_01 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_02; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_02 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_03; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_03 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_04; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_04 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_05; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_05 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_06; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_06 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_07; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_07 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_08; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_08 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_09; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_09 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_10; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_10 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_11; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_11 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2025_12; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2025_12 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2026_01; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_01 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2026_02; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_02 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2026_03; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_03 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2026_04; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_04 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: auditoria_eventos_2026_05; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_05 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
18a7143b-9ff7-43a6-ad58-305612baa38c	d79353c0-f2c0-4627-a132-a885923bcdac	11111111-1111-1111-1111-111111111111	usuario	11111111-1111-1111-1111-111111111111	CREATE	\N	{"email": "joao.demo@finapp.com.br", "moeda": "BRL", "nome_completo": "João Silva Demonstração"}	192.0.2.1	\N	web	2026-05-23 02:37:42.351468+00
\.


--
-- Data for Name: auditoria_eventos_2026_06; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.auditoria_eventos_2026_06 (id, correlation_id, usuario_id, entidade, entidade_id, acao, dados_anteriores, dados_novos, ip_origem, user_agent, origem, criado_em) FROM stdin;
\.


--
-- Data for Name: categoria_thresholds; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.categoria_thresholds (id, categoria_id, threshold_auto, threshold_alerta, ambiguidade_alta) FROM stdin;
c1e90dda-05e7-4967-b814-2a826462e31f	00000000-0000-0000-0000-000000000019	90	50	f
599fb40f-829a-4e5e-bd58-d52f26e74a0c	00000000-0000-0000-0000-000000000103	70	50	f
e1770bef-bb0b-4173-b407-7c8ada128d08	00000000-0000-0000-0000-000000000010	70	50	f
e379e38f-68c5-4365-9b01-b793008f92ac	00000000-0000-0000-0000-000000000020	100	50	t
\.


--
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao, usuario_id, criado_em) FROM stdin;
00000000-0000-0000-0000-000000000001	Salário	\N	receita	💰	#16A34A	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000002	Freelance	\N	receita	💻	#0EA5E9	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000003	Investimentos	\N	receita	📈	#7C3AED	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000004	Outras Receitas	\N	receita	➕	#64748B	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000010	Alimentação	\N	gasto	🍽️	#EF4444	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000011	Transporte	\N	gasto	🚗	#F97316	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000012	Moradia	\N	gasto	🏠	#FBBF24	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000013	Saúde	\N	gasto	🏥	#EC4899	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000014	Educação	\N	gasto	📚	#8B5CF6	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000015	Lazer	\N	gasto	🎮	#06B6D4	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000016	Vestuário	\N	gasto	👗	#F43F5E	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000017	Assinaturas	\N	gasto	📱	#6366F1	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000018	Financeiro	\N	gasto	🏦	#059669	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000019	Streaming	\N	gasto	🎬	#DC2626	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000020	Outros	\N	ambos	📦	#94A3B8	t	\N	2026-05-23 02:37:42.337476+00
00000000-0000-0000-0000-000000000101	Supermercado	00000000-0000-0000-0000-000000000010	gasto	🛒	#EF4444	t	\N	2026-05-23 02:37:42.339355+00
00000000-0000-0000-0000-000000000102	Restaurante	00000000-0000-0000-0000-000000000010	gasto	🍴	#EF4444	t	\N	2026-05-23 02:37:42.339355+00
00000000-0000-0000-0000-000000000103	Delivery	00000000-0000-0000-0000-000000000010	gasto	🛵	#EF4444	t	\N	2026-05-23 02:37:42.339355+00
00000000-0000-0000-0000-000000000104	Padaria/Café	00000000-0000-0000-0000-000000000010	gasto	☕	#EF4444	t	\N	2026-05-23 02:37:42.339355+00
00000000-0000-0000-0000-000000000105	Lanchonete	00000000-0000-0000-0000-000000000010	gasto	🍔	#EF4444	t	\N	2026-05-23 02:37:42.339355+00
00000000-0000-0000-0000-000000000111	Combustível	00000000-0000-0000-0000-000000000011	gasto	⛽	#F97316	t	\N	2026-05-23 02:37:42.340919+00
00000000-0000-0000-0000-000000000112	Uber/99	00000000-0000-0000-0000-000000000011	gasto	🚕	#F97316	t	\N	2026-05-23 02:37:42.340919+00
00000000-0000-0000-0000-000000000113	Transporte Público	00000000-0000-0000-0000-000000000011	gasto	🚌	#F97316	t	\N	2026-05-23 02:37:42.340919+00
00000000-0000-0000-0000-000000000114	Estacionamento	00000000-0000-0000-0000-000000000011	gasto	🅿️	#F97316	t	\N	2026-05-23 02:37:42.340919+00
00000000-0000-0000-0000-000000000115	Manutenção	00000000-0000-0000-0000-000000000011	gasto	🔧	#F97316	t	\N	2026-05-23 02:37:42.340919+00
00000000-0000-0000-0000-000000000121	Aluguel	00000000-0000-0000-0000-000000000012	gasto	🔑	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000122	Condomínio	00000000-0000-0000-0000-000000000012	gasto	🏢	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000123	Energia	00000000-0000-0000-0000-000000000012	gasto	💡	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000124	Água	00000000-0000-0000-0000-000000000012	gasto	💧	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000125	Internet	00000000-0000-0000-0000-000000000012	gasto	📶	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000126	Gás	00000000-0000-0000-0000-000000000012	gasto	🔥	#FBBF24	t	\N	2026-05-23 02:37:42.34209+00
00000000-0000-0000-0000-000000000131	Plano de Saúde	00000000-0000-0000-0000-000000000013	gasto	🏥	#EC4899	t	\N	2026-05-23 02:37:42.343344+00
00000000-0000-0000-0000-000000000132	Farmácia	00000000-0000-0000-0000-000000000013	gasto	💊	#EC4899	t	\N	2026-05-23 02:37:42.343344+00
00000000-0000-0000-0000-000000000133	Consultas	00000000-0000-0000-0000-000000000013	gasto	👨‍⚕️	#EC4899	t	\N	2026-05-23 02:37:42.343344+00
00000000-0000-0000-0000-000000000134	Academia	00000000-0000-0000-0000-000000000013	gasto	🏋️	#EC4899	t	\N	2026-05-23 02:37:42.343344+00
00000000-0000-0000-0000-000000000141	Mensalidade	00000000-0000-0000-0000-000000000014	gasto	🎓	#8B5CF6	t	\N	2026-05-23 02:37:42.344403+00
00000000-0000-0000-0000-000000000142	Cursos Online	00000000-0000-0000-0000-000000000014	gasto	💡	#8B5CF6	t	\N	2026-05-23 02:37:42.344403+00
00000000-0000-0000-0000-000000000143	Livros	00000000-0000-0000-0000-000000000014	gasto	📖	#8B5CF6	t	\N	2026-05-23 02:37:42.344403+00
00000000-0000-0000-0000-000000000151	Cinema/Teatro	00000000-0000-0000-0000-000000000015	gasto	🎭	#06B6D4	t	\N	2026-05-23 02:37:42.345284+00
00000000-0000-0000-0000-000000000152	Jogos	00000000-0000-0000-0000-000000000015	gasto	🕹️	#06B6D4	t	\N	2026-05-23 02:37:42.345284+00
00000000-0000-0000-0000-000000000153	Viagem	00000000-0000-0000-0000-000000000015	gasto	✈️	#06B6D4	t	\N	2026-05-23 02:37:42.345284+00
00000000-0000-0000-0000-000000000154	Bares/Baladas	00000000-0000-0000-0000-000000000015	gasto	🍻	#06B6D4	t	\N	2026-05-23 02:37:42.345284+00
00000000-0000-0000-0000-000000000171	Celular	00000000-0000-0000-0000-000000000017	gasto	📲	#6366F1	t	\N	2026-05-23 02:37:42.346348+00
00000000-0000-0000-0000-000000000172	Softwares	00000000-0000-0000-0000-000000000017	gasto	🖥️	#6366F1	t	\N	2026-05-23 02:37:42.346348+00
00000000-0000-0000-0000-000000000173	Clubes/Serviços	00000000-0000-0000-0000-000000000017	gasto	🔄	#6366F1	t	\N	2026-05-23 02:37:42.346348+00
00000000-0000-0000-0000-000000000191	Netflix	00000000-0000-0000-0000-000000000019	gasto	🎬	#DC2626	t	\N	2026-05-23 02:37:42.347375+00
00000000-0000-0000-0000-000000000192	Spotify	00000000-0000-0000-0000-000000000019	gasto	🎵	#DC2626	t	\N	2026-05-23 02:37:42.347375+00
00000000-0000-0000-0000-000000000193	YouTube Premium	00000000-0000-0000-0000-000000000019	gasto	▶️	#DC2626	t	\N	2026-05-23 02:37:42.347375+00
00000000-0000-0000-0000-000000000194	Disney+	00000000-0000-0000-0000-000000000019	gasto	🏰	#DC2626	t	\N	2026-05-23 02:37:42.347375+00
00000000-0000-0000-0000-000000000195	Amazon Prime	00000000-0000-0000-0000-000000000019	gasto	📦	#DC2626	t	\N	2026-05-23 02:37:42.347375+00
00000000-0000-0000-0000-000000000181	Empréstimo	00000000-0000-0000-0000-000000000018	gasto	💳	#059669	t	\N	2026-05-23 02:37:42.348413+00
00000000-0000-0000-0000-000000000182	Cartão Crédito	00000000-0000-0000-0000-000000000018	gasto	💳	#059669	t	\N	2026-05-23 02:37:42.348413+00
00000000-0000-0000-0000-000000000183	Tarifas Bancárias	00000000-0000-0000-0000-000000000018	gasto	🏦	#059669	t	\N	2026-05-23 02:37:42.348413+00
00000000-0000-0000-0000-000000000184	Seguro	00000000-0000-0000-0000-000000000018	gasto	🛡️	#059669	t	\N	2026-05-23 02:37:42.348413+00
\.


--
-- Data for Name: classificacao_logs; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.classificacao_logs (id, transacao_id, usuario_id, estrategia, modelo_ia_versao, prompt_versao, prompt_hash, resposta_raw, categoria_sugerida, confianca, tokens_usados, latencia_ms, categoria_final, corrigida, corrigida_em, motivo_correcao, criado_em) FROM stdin;
\.


--
-- Data for Name: consentimentos_lgpd; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.consentimentos_lgpd (id, usuario_id, tipo, versao_politica, consentido, ip_origem, criado_em, revogado_em, revogado_motivo) FROM stdin;
4b73bc1e-9c32-4bed-becb-fee9516bd648	11111111-1111-1111-1111-111111111111	tratamento_dados_financeiros	1.0	t	192.0.2.1	2026-05-23 02:37:42.351468+00	\N	\N
471174a9-c3cc-48f5-8f33-223b41b1b100	11111111-1111-1111-1111-111111111111	uso_ia	1.0	t	192.0.2.1	2026-05-23 02:37:42.351468+00	\N	\N
f1bb4b28-42b8-4ab4-9dca-14fb1ebe0a1b	11111111-1111-1111-1111-111111111111	armazenamento_extrato	1.0	t	192.0.2.1	2026-05-23 02:37:42.351468+00	\N	\N
\.


--
-- Data for Name: contas_financeiras; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.contas_financeiras (id, usuario_id, nome, tipo, banco, saldo_inicial, padrao, ativa, criado_em, atualizado_em) FROM stdin;
22222222-2222-2222-2222-222222222201	11111111-1111-1111-1111-111111111111	Nubank Conta	corrente	Nubank	2500.00	t	t	2026-05-23 02:37:42.351468+00	\N
22222222-2222-2222-2222-222222222202	11111111-1111-1111-1111-111111111111	Itaú Corrente	corrente	Itaú	10000.00	f	t	2026-05-23 02:37:42.351468+00	\N
22222222-2222-2222-2222-222222222203	11111111-1111-1111-1111-111111111111	Carteira/Dinheiro	dinheiro	\N	300.00	f	t	2026-05-23 02:37:42.351468+00	\N
\.


--
-- Data for Name: extratos; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.extratos (id, usuario_id, conta_id, arquivo_nome, arquivo_uuid, hash_arquivo, banco_detectado, parser_versao_id, score_extracao, periodo_inicio, periodo_fim, status, total_lancamentos, lancamentos_confirmados, lancamentos_pendentes, lancamentos_ignorados, versao, criado_em, atualizado_em) FROM stdin;
33333333-3333-3333-3333-333333333301	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	nubank_maio_2025.pdf	aa847994-b5ed-434e-8d9e-c2ff058bdbc8	dfb3b9b4a068b32fef73721b68a0f0b582e4210790a62ea1840f83afeb04576d	Nubank	935e7c0a-98f2-4467-b682-84191fa9b9a0	0.980	2025-05-01	2025-05-31	concluido	8	6	2	0	1	2026-05-23 02:37:42.351468+00	\N
\.


--
-- Data for Name: notificacoes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.notificacoes (id, usuario_id, canal, tipo, titulo, mensagem, enviada, enviada_em, erro, tentativas, criado_em) FROM stdin;
62331910-bf90-4ed6-a5fb-f5efe1f7360b	11111111-1111-1111-1111-111111111111	email	boas_vindas	Bem-vindo ao FinApp! 🎉	Olá João, sua conta foi criada com sucesso. Comece importando seu primeiro extrato!	t	2026-05-23 02:37:42.351468+00	\N	0	2026-05-23 02:37:42.351468+00
\.


--
-- Data for Name: parser_versoes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.parser_versoes (id, banco, versao, ativo, score_qualidade, total_usos, total_erros, descricao, depreciado_em) FROM stdin;
935e7c0a-98f2-4467-b682-84191fa9b9a0	Nubank	v1.0	t	0.980	0	0	Parser Nubank — extrato CSV/PDF	\N
041f7706-080d-46c5-8d88-75fa7c10ea35	Itaú	v1.0	t	0.960	0	0	Parser Itaú  — PDF padrão	\N
70b85a92-cd3e-4aad-a46d-a3d3ee5057be	Bradesco	v1.0	t	0.940	0	0	Parser Bradesco — PDF	\N
77888372-45ee-4303-9caa-81a21ed727da	Santander	v1.0	t	0.920	0	0	Parser Santander — PDF	\N
882a9d4d-a096-4bbe-af9a-1ca093c14700	Caixa	v1.0	t	0.900	0	0	Parser Caixa — PDF	\N
83847800-6c48-408b-9d98-5e232dd9f34d	Inter	v1.0	t	0.970	0	0	Parser Inter — CSV/PDF	\N
f9bf35f9-c57c-4acb-9e5b-1ad98617ecb2	XP	v1.0	t	0.930	0	0	Parser XP Investimentos — PDF	\N
dd7793a7-e210-456e-bec5-67b0f4fd39df	Genérico	v1.0	t	0.700	0	0	Parser genérico de fallback	\N
\.


--
-- Data for Name: processamento_jobs; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.processamento_jobs (id, extrato_id, tipo, status, tentativas, max_tentativas, payload, erro_mensagem, worker_id, lock_expires_at, correlation_id, enfileirado_em, iniciado_em, concluido_em, proximo_retry) FROM stdin;
\.


--
-- Data for Name: regras_classificacao; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.regras_classificacao (id, usuario_id, termo, categoria_id, subcategoria, score_confianca, vezes_aplicada, vezes_corretas, vezes_incorretas, correcoes_consecutivas, congelada, congelada_motivo, congelada_ate, criada_por, ultima_aplicacao, ultima_correcao, criado_em) FROM stdin;
7bb2ce3b-36d2-406f-a433-f0d5390e4f52	11111111-1111-1111-1111-111111111111	IFOOD	00000000-0000-0000-0000-000000000103	Delivery	1.000	0	0	0	0	f	\N	\N	ia_auto	\N	\N	2026-05-23 02:37:42.351468+00
aa7a947c-2401-4ddb-acdb-43778d7d50ba	11111111-1111-1111-1111-111111111111	NETFLIX	00000000-0000-0000-0000-000000000191	Streaming	1.000	0	0	0	0	f	\N	\N	ia_auto	\N	\N	2026-05-23 02:37:42.351468+00
48e78508-f060-43e6-8482-24bf2c09e7bd	11111111-1111-1111-1111-111111111111	UBER	00000000-0000-0000-0000-000000000112	Transporte	1.000	0	0	0	0	f	\N	\N	ia_auto	\N	\N	2026-05-23 02:37:42.351468+00
963e5e9a-89f3-4b7d-af1b-49d63a300888	11111111-1111-1111-1111-111111111111	DROGA RAIA	00000000-0000-0000-0000-000000000132	Farmácia	1.000	0	0	0	0	f	\N	\N	usuario	\N	\N	2026-05-23 02:37:42.351468+00
13603715-8b8c-4473-b0d0-b00fcf263324	11111111-1111-1111-1111-111111111111	ENEL	00000000-0000-0000-0000-000000000123	Energia	1.000	0	0	0	0	f	\N	\N	usuario	\N	\N	2026-05-23 02:37:42.351468+00
\.


--
-- Data for Name: snapshots_financeiros; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.snapshots_financeiros (id, usuario_id, conta_id, ano, mes, saldo_inicial, total_receitas, total_gastos, saldo_final, fechado, fechado_em) FROM stdin;
37214389-24b4-46f7-90ab-56c46ccd5a7f	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	2025	5	2500.00	1500.00	801.89	3198.11	f	\N
d3d3e7dd-2185-4f75-8c79-0637299625f4	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222202	2025	5	10000.00	6800.00	180.00	16620.00	f	\N
20cbd484-c8c9-4df3-8aec-fa10d867fdc9	11111111-1111-1111-1111-111111111111	\N	2025	5	12800.00	8300.00	1003.89	20096.11	f	\N
\.


--
-- Data for Name: transacoes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transacoes (id, usuario_id, conta_id, extrato_id, transacao_estorno_id, tipo, descricao_original, descricao_usuario, descricao_normalizada, valor, data_transacao, data_lancamento, categoria_id, subcategoria, estabelecimento, origem, status_revisao, confianca_ia, recorrente, periodo_recorrencia, observacao, deleted_at, versao, criado_em, atualizado_em) FROM stdin;
d5ead8da-d617-46c2-9739-0acf5492e020	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 02/05 PAGUE MENOS SP	\N	COMPRA PAGUE MENOS SP	347.89	2025-05-02	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000101	Supermercado	Pague Menos	pdf	confirmada	92	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
275a935d-41fe-4cb0-9e52-ae4b4568077f	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 04/05 IFOOD*RESTAURANTE SP	\N	IFOOD RESTAURANTE SP	58.90	2025-05-04	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000103	Delivery	iFood	pdf	confirmada	95	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
491228de-cfd7-4e75-9c0b-1c5f0a1ab1b9	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 05/05 UBER *TRIP SP	\N	UBER TRIP SP	34.70	2025-05-05	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000112	Uber/99	Uber	pdf	confirmada	98	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
df47ebbe-1496-4ca1-91e5-203f17af29bc	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 07/05 NETFLIX.COM SP	\N	NETFLIX COM SP	55.90	2025-05-07	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000191	Streaming	Netflix	pdf	confirmada	99	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
434e4716-dee8-443a-9708-f5690ef944d7	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 10/05 DROGA RAIA SP	\N	DROGA RAIA SP	89.50	2025-05-10	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000132	Farmácia	Droga Raia	pdf	confirmada	91	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
fb040e1b-3db6-4dd9-b0ad-ac35f9d9affb	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222202	\N	\N	gasto	\N	\N	ENEL SP ENERGIA ELETRICA	180.00	2025-05-12	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000123	Energia	ENEL SP	manual	confirmada	\N	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
bf59bc66-2d32-4ada-9be5-9dfe57dbe0fe	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	33333333-3333-3333-3333-333333333301	\N	gasto	COMPRA 20/05 AMAZON MKTPLC SP	\N	AMAZON MKTPLC SP	215.00	2025-05-20	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000020	\N	Amazon	pdf	pendente_revisao	45	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
bc4e82dd-4cc1-47ca-9a7b-0558d0d44a32	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222203	\N	\N	gasto	\N	\N	LANCHONETE ESQUINA	22.00	2025-05-22	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000105	Lanchonete	Lanchonete Esquina	manual	confirmada	\N	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
a810b558-842b-4ac3-86a8-7485aa0a3109	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222202	\N	\N	receita	\N	Salário Maio/2025	SALARIO MAIO 2025	6800.00	2025-05-05	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000001	\N	\N	manual	confirmada	\N	t	mensal	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
48921deb-1204-4689-ad64-bb816d23c711	11111111-1111-1111-1111-111111111111	22222222-2222-2222-2222-222222222201	\N	\N	receita	\N	Projeto Web — Cliente ABC	PROJETO WEB CLIENTE ABC	1500.00	2025-05-15	2026-05-23 02:37:42.351468+00	00000000-0000-0000-0000-000000000002	Desenvolvimento	\N	manual	confirmada	\N	f	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.usuarios (id, codigo, cpf, cpf_hash, nome_completo, email, senha_hash, telefone, telegram_chat_id, whatsapp_id, saldo_inicial_definido, moeda, ativo, email_verificado, token_verificacao, token_verificacao_expira, ultimo_login, tentativas_login, bloqueado_ate, refresh_token_hash, api_key_hash, deleted_at, versao, criado_em, atualizado_em) FROM stdin;
11111111-1111-1111-1111-111111111111	FIN-DEMO01	ENCRYPTED_CPF_PLACEHOLDER	7ec94663084bd506d4f0c3e21042df233681fd7426e93f397c921b1d3e397bba	João Silva Demonstração	joao.demo@finapp.com.br	$2b$12$K2hXdW7eL9mNqRvP3sBuCeXgTlAaDz5yQfJcMkwE1nHoVpYrI8uGa	(11) 99999-0000	\N	\N	t	BRL	t	t	\N	\N	2026-05-23 02:37:42.351468+00	0	\N	\N	\N	\N	1	2026-05-23 02:37:42.351468+00	\N
\.


--
-- Name: categoria_thresholds categoria_thresholds_categoria_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categoria_thresholds
    ADD CONSTRAINT categoria_thresholds_categoria_id_key UNIQUE (categoria_id);


--
-- Name: categoria_thresholds categoria_thresholds_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categoria_thresholds
    ADD CONSTRAINT categoria_thresholds_pkey PRIMARY KEY (id);


--
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);


--
-- Name: classificacao_logs classificacao_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classificacao_logs
    ADD CONSTRAINT classificacao_logs_pkey PRIMARY KEY (id);


--
-- Name: consentimentos_lgpd consentimentos_lgpd_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.consentimentos_lgpd
    ADD CONSTRAINT consentimentos_lgpd_pkey PRIMARY KEY (id);


--
-- Name: contas_financeiras contas_financeiras_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contas_financeiras
    ADD CONSTRAINT contas_financeiras_pkey PRIMARY KEY (id);


--
-- Name: extratos extratos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.extratos
    ADD CONSTRAINT extratos_pkey PRIMARY KEY (id);


--
-- Name: notificacoes notificacoes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacoes
    ADD CONSTRAINT notificacoes_pkey PRIMARY KEY (id);


--
-- Name: parser_versoes parser_versoes_banco_versao_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parser_versoes
    ADD CONSTRAINT parser_versoes_banco_versao_key UNIQUE (banco, versao);


--
-- Name: parser_versoes parser_versoes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parser_versoes
    ADD CONSTRAINT parser_versoes_pkey PRIMARY KEY (id);


--
-- Name: processamento_jobs processamento_jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.processamento_jobs
    ADD CONSTRAINT processamento_jobs_pkey PRIMARY KEY (id);


--
-- Name: regras_classificacao regras_classificacao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.regras_classificacao
    ADD CONSTRAINT regras_classificacao_pkey PRIMARY KEY (id);


--
-- Name: snapshots_financeiros snapshots_financeiros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.snapshots_financeiros
    ADD CONSTRAINT snapshots_financeiros_pkey PRIMARY KEY (id);


--
-- Name: snapshots_financeiros snapshots_financeiros_usuario_id_conta_id_ano_mes_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.snapshots_financeiros
    ADD CONSTRAINT snapshots_financeiros_usuario_id_conta_id_ano_mes_key UNIQUE (usuario_id, conta_id, ano, mes);


--
-- Name: transacoes transacoes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_codigo_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_codigo_key UNIQUE (codigo);


--
-- Name: usuarios usuarios_cpf_hash_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_cpf_hash_key UNIQUE (cpf_hash);


--
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: idx_audit_correlation; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_audit_correlation ON ONLY public.auditoria_eventos USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_01_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_01_correlation_id_idx ON public.auditoria_eventos_2025_01 USING btree (correlation_id);


--
-- Name: idx_audit_entidade; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_audit_entidade ON ONLY public.auditoria_eventos USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_01_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_01_entidade_entidade_id_idx ON public.auditoria_eventos_2025_01 USING btree (entidade, entidade_id);


--
-- Name: idx_audit_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_audit_usuario ON ONLY public.auditoria_eventos USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_01_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_01_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_01 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_02_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_02_correlation_id_idx ON public.auditoria_eventos_2025_02 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_02_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_02_entidade_entidade_id_idx ON public.auditoria_eventos_2025_02 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_02_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_02_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_02 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_03_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_03_correlation_id_idx ON public.auditoria_eventos_2025_03 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_03_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_03_entidade_entidade_id_idx ON public.auditoria_eventos_2025_03 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_03_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_03_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_03 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_04_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_04_correlation_id_idx ON public.auditoria_eventos_2025_04 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_04_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_04_entidade_entidade_id_idx ON public.auditoria_eventos_2025_04 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_04_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_04_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_04 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_05_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_05_correlation_id_idx ON public.auditoria_eventos_2025_05 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_05_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_05_entidade_entidade_id_idx ON public.auditoria_eventos_2025_05 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_05_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_05_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_05 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_06_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_06_correlation_id_idx ON public.auditoria_eventos_2025_06 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_06_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_06_entidade_entidade_id_idx ON public.auditoria_eventos_2025_06 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_06_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_06_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_06 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_07_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_07_correlation_id_idx ON public.auditoria_eventos_2025_07 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_07_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_07_entidade_entidade_id_idx ON public.auditoria_eventos_2025_07 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_07_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_07_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_07 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_08_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_08_correlation_id_idx ON public.auditoria_eventos_2025_08 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_08_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_08_entidade_entidade_id_idx ON public.auditoria_eventos_2025_08 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_08_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_08_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_08 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_09_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_09_correlation_id_idx ON public.auditoria_eventos_2025_09 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_09_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_09_entidade_entidade_id_idx ON public.auditoria_eventos_2025_09 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_09_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_09_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_09 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_10_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_10_correlation_id_idx ON public.auditoria_eventos_2025_10 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_10_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_10_entidade_entidade_id_idx ON public.auditoria_eventos_2025_10 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_10_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_10_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_10 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_11_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_11_correlation_id_idx ON public.auditoria_eventos_2025_11 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_11_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_11_entidade_entidade_id_idx ON public.auditoria_eventos_2025_11 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_11_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_11_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_11 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2025_12_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_12_correlation_id_idx ON public.auditoria_eventos_2025_12 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2025_12_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_12_entidade_entidade_id_idx ON public.auditoria_eventos_2025_12 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2025_12_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2025_12_usuario_id_criado_em_idx ON public.auditoria_eventos_2025_12 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_01_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_01_correlation_id_idx ON public.auditoria_eventos_2026_01 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_01_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_01_entidade_entidade_id_idx ON public.auditoria_eventos_2026_01 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_01_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_01_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_01 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_02_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_02_correlation_id_idx ON public.auditoria_eventos_2026_02 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_02_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_02_entidade_entidade_id_idx ON public.auditoria_eventos_2026_02 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_02_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_02_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_02 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_03_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_03_correlation_id_idx ON public.auditoria_eventos_2026_03 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_03_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_03_entidade_entidade_id_idx ON public.auditoria_eventos_2026_03 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_03_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_03_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_03 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_04_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_04_correlation_id_idx ON public.auditoria_eventos_2026_04 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_04_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_04_entidade_entidade_id_idx ON public.auditoria_eventos_2026_04 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_04_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_04_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_04 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_05_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_05_correlation_id_idx ON public.auditoria_eventos_2026_05 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_05_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_05_entidade_entidade_id_idx ON public.auditoria_eventos_2026_05 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_05_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_05_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_05 USING btree (usuario_id, criado_em DESC);


--
-- Name: auditoria_eventos_2026_06_correlation_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_06_correlation_id_idx ON public.auditoria_eventos_2026_06 USING btree (correlation_id);


--
-- Name: auditoria_eventos_2026_06_entidade_entidade_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_06_entidade_entidade_id_idx ON public.auditoria_eventos_2026_06 USING btree (entidade, entidade_id);


--
-- Name: auditoria_eventos_2026_06_usuario_id_criado_em_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX auditoria_eventos_2026_06_usuario_id_criado_em_idx ON public.auditoria_eventos_2026_06 USING btree (usuario_id, criado_em DESC);


--
-- Name: idx_categorias_padrao; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_categorias_padrao ON public.categorias USING btree (padrao) WHERE (padrao = true);


--
-- Name: idx_categorias_tipo; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_categorias_tipo ON public.categorias USING btree (tipo);


--
-- Name: idx_categorias_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_categorias_usuario ON public.categorias USING btree (usuario_id) WHERE (usuario_id IS NOT NULL);


--
-- Name: idx_classlog_transacao; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_classlog_transacao ON public.classificacao_logs USING btree (transacao_id);


--
-- Name: idx_contas_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_contas_usuario ON public.contas_financeiras USING btree (usuario_id);


--
-- Name: idx_extratos_hash; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_extratos_hash ON public.extratos USING btree (hash_arquivo);


--
-- Name: idx_extratos_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_extratos_usuario ON public.extratos USING btree (usuario_id, criado_em DESC);


--
-- Name: idx_regras_usuario_termo; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_regras_usuario_termo ON public.regras_classificacao USING btree (usuario_id, termo);


--
-- Name: idx_snapshots_usuario_mes; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_snapshots_usuario_mes ON public.snapshots_financeiros USING btree (usuario_id, ano DESC, mes DESC);


--
-- Name: idx_transacoes_categoria; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_transacoes_categoria ON public.transacoes USING btree (categoria_id);


--
-- Name: idx_transacoes_conta; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_transacoes_conta ON public.transacoes USING btree (conta_id);


--
-- Name: idx_transacoes_extrato; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_transacoes_extrato ON public.transacoes USING btree (extrato_id) WHERE (extrato_id IS NOT NULL);


--
-- Name: idx_transacoes_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_transacoes_status ON public.transacoes USING btree (status_revisao);


--
-- Name: idx_transacoes_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_transacoes_usuario ON public.transacoes USING btree (usuario_id, data_transacao DESC) WHERE (deleted_at IS NULL);


--
-- Name: idx_usuarios_codigo; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_usuarios_codigo ON public.usuarios USING btree (codigo);


--
-- Name: idx_usuarios_cpf_hash; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_usuarios_cpf_hash ON public.usuarios USING btree (cpf_hash) WHERE (deleted_at IS NULL);


--
-- Name: idx_usuarios_email; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_usuarios_email ON public.usuarios USING btree (email) WHERE (deleted_at IS NULL);


--
-- Name: auditoria_eventos_2025_01_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_01_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_01_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_01_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_01_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_01_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_02_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_02_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_02_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_02_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_02_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_02_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_03_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_03_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_03_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_03_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_03_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_03_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_04_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_04_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_04_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_04_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_04_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_04_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_05_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_05_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_05_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_05_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_05_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_05_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_06_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_06_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_06_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_06_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_06_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_06_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_07_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_07_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_07_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_07_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_07_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_07_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_08_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_08_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_08_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_08_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_08_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_08_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_09_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_09_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_09_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_09_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_09_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_09_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_10_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_10_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_10_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_10_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_10_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_10_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_11_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_11_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_11_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_11_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_11_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_11_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2025_12_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2025_12_correlation_id_idx;


--
-- Name: auditoria_eventos_2025_12_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2025_12_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2025_12_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2025_12_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_01_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_01_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_01_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_01_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_01_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_01_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_02_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_02_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_02_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_02_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_02_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_02_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_03_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_03_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_03_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_03_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_03_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_03_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_04_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_04_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_04_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_04_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_04_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_04_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_05_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_05_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_05_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_05_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_05_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_05_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos_2026_06_correlation_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_correlation ATTACH PARTITION public.auditoria_eventos_2026_06_correlation_id_idx;


--
-- Name: auditoria_eventos_2026_06_entidade_entidade_id_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_entidade ATTACH PARTITION public.auditoria_eventos_2026_06_entidade_entidade_id_idx;


--
-- Name: auditoria_eventos_2026_06_usuario_id_criado_em_idx; Type: INDEX ATTACH; Schema: public; Owner: -
--

ALTER INDEX public.idx_audit_usuario ATTACH PARTITION public.auditoria_eventos_2026_06_usuario_id_criado_em_idx;


--
-- Name: auditoria_eventos auditoria_eventos_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE public.auditoria_eventos
    ADD CONSTRAINT auditoria_eventos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: categoria_thresholds categoria_thresholds_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categoria_thresholds
    ADD CONSTRAINT categoria_thresholds_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id);


--
-- Name: categorias categorias_categoria_pai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_categoria_pai_id_fkey FOREIGN KEY (categoria_pai_id) REFERENCES public.categorias(id);


--
-- Name: categorias categorias_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: classificacao_logs classificacao_logs_categoria_final_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classificacao_logs
    ADD CONSTRAINT classificacao_logs_categoria_final_fkey FOREIGN KEY (categoria_final) REFERENCES public.categorias(id);


--
-- Name: classificacao_logs classificacao_logs_categoria_sugerida_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classificacao_logs
    ADD CONSTRAINT classificacao_logs_categoria_sugerida_fkey FOREIGN KEY (categoria_sugerida) REFERENCES public.categorias(id);


--
-- Name: classificacao_logs classificacao_logs_transacao_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classificacao_logs
    ADD CONSTRAINT classificacao_logs_transacao_id_fkey FOREIGN KEY (transacao_id) REFERENCES public.transacoes(id);


--
-- Name: classificacao_logs classificacao_logs_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classificacao_logs
    ADD CONSTRAINT classificacao_logs_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: consentimentos_lgpd consentimentos_lgpd_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.consentimentos_lgpd
    ADD CONSTRAINT consentimentos_lgpd_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: contas_financeiras contas_financeiras_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contas_financeiras
    ADD CONSTRAINT contas_financeiras_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: extratos extratos_conta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.extratos
    ADD CONSTRAINT extratos_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES public.contas_financeiras(id);


--
-- Name: extratos extratos_parser_versao_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.extratos
    ADD CONSTRAINT extratos_parser_versao_id_fkey FOREIGN KEY (parser_versao_id) REFERENCES public.parser_versoes(id);


--
-- Name: extratos extratos_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.extratos
    ADD CONSTRAINT extratos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: notificacoes notificacoes_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacoes
    ADD CONSTRAINT notificacoes_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: processamento_jobs processamento_jobs_extrato_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.processamento_jobs
    ADD CONSTRAINT processamento_jobs_extrato_id_fkey FOREIGN KEY (extrato_id) REFERENCES public.extratos(id);


--
-- Name: regras_classificacao regras_classificacao_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.regras_classificacao
    ADD CONSTRAINT regras_classificacao_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id);


--
-- Name: regras_classificacao regras_classificacao_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.regras_classificacao
    ADD CONSTRAINT regras_classificacao_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: snapshots_financeiros snapshots_financeiros_conta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.snapshots_financeiros
    ADD CONSTRAINT snapshots_financeiros_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES public.contas_financeiras(id);


--
-- Name: snapshots_financeiros snapshots_financeiros_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.snapshots_financeiros
    ADD CONSTRAINT snapshots_financeiros_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: transacoes transacoes_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id);


--
-- Name: transacoes transacoes_conta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES public.contas_financeiras(id);


--
-- Name: transacoes transacoes_extrato_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_extrato_id_fkey FOREIGN KEY (extrato_id) REFERENCES public.extratos(id);


--
-- Name: transacoes transacoes_transacao_estorno_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_transacao_estorno_id_fkey FOREIGN KEY (transacao_estorno_id) REFERENCES public.transacoes(id);


--
-- Name: transacoes transacoes_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transacoes
    ADD CONSTRAINT transacoes_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict 4Aiya4Rs06y0PpuaBSiUoFP8WM6XYYPh9HvnQxMvIjK01244pSH6sYaNKfPZH3k


SET default_tablespace = '';
SET default_table_access_method = heap;

\connect estoque_produtos;

CREATE TABLE IF NOT EXISTS public.tb_categorias
(
    id uuid NOT NULL,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tb_categorias_pkey PRIMARY KEY (id),
    CONSTRAINT tb_categorias_unique_name UNIQUE (nome)
);

INSERT INTO public.tb_categorias (id, nome) VALUES
  (gen_random_uuid(), 'Bebidas'),
  (gen_random_uuid(), 'Hortifruti'),
  (gen_random_uuid(), 'Padaria'),
  (gen_random_uuid(), 'Açougue'),
  (gen_random_uuid(), 'Limpeza'),
  (gen_random_uuid(), 'Higiene Pessoal'),
  (gen_random_uuid(), 'Congelados'),
  (gen_random_uuid(), 'Laticínios'),
  (gen_random_uuid(), 'Mercearia'),
  (gen_random_uuid(), 'Pet Shop');

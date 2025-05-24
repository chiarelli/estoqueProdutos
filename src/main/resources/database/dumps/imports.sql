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

INSERT INTO public.tb_categorias (id, nome) 
VALUES
  ('a7ce8685-f710-4628-b449-9ed07c534261', 'Bebidas'),
  ('85aec76d-b899-4eb3-af61-01f4d9120f9b', 'Hortifruti'),
  ('ab882be1-c518-4227-bca8-d5126b361b6a', 'Padaria'),
  ('fb50da03-ba9f-4f78-ad0d-3d04c65f718a', 'Açougue'),
  ('d31b8d3e-9a23-43c3-a279-cbe6022e0c17', 'Limpeza'),
  ('a64901c3-f453-4a32-856e-e3eea5c67bb2', 'Higiene Pessoal'),
  ('9200bc62-7b89-42f4-9f69-df17a071ac36', 'Congelados'),
  ('022b570e-bf1b-4d90-b18a-4d75dfe291d1', 'Laticínios'),
  ('48f9ab24-b953-4d75-b236-3c166f85c574', 'Mercearia'),
  ('659435a2-c91d-4bff-b5c7-5384cc23b49f', 'Pet Shop');

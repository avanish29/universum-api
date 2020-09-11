DROP TABLE public.tmp;

CREATE TABLE public.tmp (
    code varchar(255),
    label varchar(255),
    dir varchar(20),
    "default" bool
);

copy public.tmp(code, "label", dir, "default") FROM 'F:\\Universum\\universum-api\\universum-i18-service\\src\\main\\resources\\db\\data\\available_language.csv' DELIMITER ',' CSV HEADER;

INSERT INTO public.available_language (code, "label", dir, "default") SELECT t.code, t."label", t.dir, t."default" FROM public.tmp t ON conflict (code) DO UPDATE SET "label" = excluded."label", dir = excluded.dir, "default" = excluded."default";

DROP TABLE public.tmp;
INSERT INTO public.users
(id, full_name, "password", email, role_id, "token", token_expiration)
VALUES(1, 'Admin', '$2a$10$hlG2d0.i0Bki.B2KNL9Qf.BpT.L8/GXqN11F1wcqO6S6xcGaEwph2', 'admin@mail.com', 2, NULL, NULL);
INSERT INTO public.users
(id, full_name, "password", email, role_id, "token", token_expiration)
VALUES(2, 'Cliente', '$2a$10$/aAtJmwJwQa/895rwQnt4Om3.zk8qTgJvItfgwm1w9wRBByUsyOMe', 'cliente@mail.com', 1, NULL, NULL);
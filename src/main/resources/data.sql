INSERT INTO customer (name, account_balance)
VALUES ('Joonas', 100.0);
INSERT INTO customer (name, account_balance)
VALUES ('Antti', 200.0);
INSERT INTO customer (name, account_balance)
VALUES ('Mikko', 300.0);

INSERT INTO game_event (id, customer_id, event_type, created_at, amount, new_balance)
VALUES (1, 1, 'PURCHASE', now(), 10, 90);
INSERT INTO game_event (id, customer_id, event_type, created_at, amount)
VALUES (2, 2, 'PURCHASE', now(), 20, 180);
INSERT INTO game_event (id, customer_id, event_type, created_at, amount)
VALUES (3, 3, 'WIN', now(), 30, 270);

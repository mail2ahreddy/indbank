INSERT INTO account (id, name, balance) VALUES
(1, 'Haranadha', 5000),
(2, 'Drushil', 3000)
ON CONFLICT DO NOTHING;

INSERT INTO transactions (account_id, type, amount)
VALUES
(1, 'DEPOSIT', 500),
(2, 'DEPOSIT', 1000)
ON CONFLICT DO NOTHING;

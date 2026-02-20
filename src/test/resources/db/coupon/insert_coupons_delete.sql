INSERT INTO coupon (id, external_id, code, description, coupon_status, discount_value, expiration_date, published, redeemed, deleted_at, created_at)
VALUES (1, RANDOM_UUID(), 'ABC123', 'Cupom para teste de delete', 1, 5.00, '2099-12-31', true, false, NULL, CURRENT_TIMESTAMP);


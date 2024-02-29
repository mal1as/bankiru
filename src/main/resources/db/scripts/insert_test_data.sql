insert into user_data(name, surname, patronymic, profit_per_month, work_company)
values ('Юзер1', 'юзер1', null, 100000, 'сбербанк'),
       ('Юзер2', 'юзер2', null, 100000, 'на дядю Ваню');

insert into "user"(email, password_hash, phone_number, user_data_id)
values ('bankiru465@gmail.com', '$2a$12$q6I2/xubS.C/cBMUIPWdU.hWtMATkOCIjwzsznCZPuKrQ2DPBdHna', '88002222222', 1),
       ('mishadyakonov140501@gmail.com', '$2a$12$alIjyvANCYUaJ4SEDRC9CudDxlz2j1AUn.DHnbP4h5dom4kg1Pqpu', '88003333333', 2);

insert into user_roles(user_id, role_id)
values (3, 3),
       (4, 3);

insert into bank(description, name, phone_number, rate, ownership_type_id)
values ('тинькофф', 'тинькофф', '88004444444', 1, 3);

insert into address(building_number, country, house_number, office_number, street, town)
values (1, 'Russia', 1, 1, 'Lenina', 'Chelyabinsk');

insert into credit_rule(max_period, max_rate, max_sum, min_period, min_rate, min_sum, bank_id, credit_type_id)
values (10000, 100, 1000000, 1, 1, 1, 1, 2);

insert into bank_addresses(bank_id, address_id)
values (1, 1);

insert into user_request(deposit, period, sum, user_id)
values (true, 10, 10000, 3),
       (true, 10, 10000, 4);

insert into credit_request(bank_id, credit_rule_id, request_status_id, user_request_id)
values (1, 1, 1, 1),
       (1, 1, 1, 2);
--- DML Insert some default types
do
$$
    begin
        if not exists(select 1 from credit_type) then
            insert into credit_type(name, deposit)
            values ('Наличными', false),
                   ('Под залог', true),
                   ('Под залог недвижимости', true),
                   ('Рефинансирование', false);
        end if;

        if not exists(select 1 from ownership_type) then
            insert into ownership_type(name, short_name)
            values ('Общество с ограниченной ответственностью', 'ООО'),
                   ('Индивидуальный предприниматель', 'ИП'),
                   ('Акционерное общество', 'АО'),
                   ('Публичное акционерное общество', 'ПАО');
        end if;

        if not exists(select 1 from request_status) then
            insert into request_status(name)
            values ('Ожидайте решения'),
                   ('Заявка отклонена'),
                   ('Заявка одобрена');
        end if;

        if not exists(select 1 from role) then
            insert into role(name)
            values ('ADMIN'),
                   ('BANK_WORKER'),
                   ('AUTH_USER');
        end if;

        if not exists(select 1 from "user") then
            insert into "user"(email, password_hash, phone_number)
            values ('admin@gmail.com', '$2a$10$niMRIOM8R74aBjWwRx9/6.4XVunO6bpGfXUesi.etVQZnZGeNDTuC', '88005553535'),
                   ('bankw1@gmail.com', '$2a$10$hcXOpbReW6R1YW9n1ZYkLOWXIcfsBvC.OY1MNd8ombTV5RBSDr1.e', '88001111111');

            insert into user_roles(user_id, role_id)
            values (1, 1),
                   (2, 2);
        end if;
    end
$$ language plpgsql;
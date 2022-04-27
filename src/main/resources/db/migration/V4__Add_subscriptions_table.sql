create table user_subscriptions (
        channel_id bigint not null references usr (id),
        subscriber_id bigint not null references usr (id),
        primary key (channel_id, subscriber_id)
) engine=InnoDB;
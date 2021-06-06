drop table easy_document if exists;

create table easy_document (
                               document_id bigint generated by default as IDENTITY,
                               title varchar(100) not null,
                               type varchar(300) not null
);

drop table document_approval if exists;

create table document_approval (
                                   approval_seq bigint generated by default as IDENTITY,
                                   user_id varchar(100) not null,
                                   is_approved boolean not null default false,
                                   comment varchar(300),
                                   approval_order integer not null,
                                   document_id bigint not null

);
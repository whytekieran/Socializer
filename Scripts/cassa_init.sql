-- Initial database structure - Social Media Y4 Project
-- Version: 0.1
-- Author: Peter nagy

-- Create User column family
-- Development configuration
create KEYSPACE app_user_data
with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

-- Production configuration
-- create KEYSPACE app_user_data
-- with replication = {'class': 'NetworkTopologyStrategy', 'replication_factor' : 3}
-- and DURABLE_WRITES = true;

 -- User table
 create table app_user_data.user(
	user_uuid uuid, email text, password_hash text, hash_secret text, firstname text, surname text, slug text, dob text, phone_number text, address_1 text, address_2 text, address_3 text, address_city text, address_county text, address_country text,
	address_json text, profile_pic_uuid uuid, background_pic_uuid uuid, workplaces list<text>, professional_skills list<text>, lived_in list<text>, connections list<uuid>,
	created timestamp, updated timestamp,
	primary key ((user_uuid, email), slug)
 );

create index on app_user_data.user (email);
create index on app_user_data.user (firstname);
create index on app_user_data.user (surname);

-- User secrets
create table app_user_data.user_secret(
	user_uuid uuid,
	secret_hash text,
	challenge text,
	iv text,
	salt text,
	enc_private_key text,
	public_key text,
	created timestamp,
	updated timestamp,
	primary key(user_uuid)
);

-- Timeline table
create table app_user_data.timeline(
	user_uuid uuid,
	parrent_uuid uuid,
	album_uuid uuid,
	content_data text,
	content_type text,
	like_count int,
	unlike_count int,
	visibility text,
	created timeuuid,
	updated timestamp,
	primary key(user_uuid, created)
) with CLUSTERING order by (created desc);

create index on app_user_data.timeline (parrent_uuid);
create index on app_user_data.timeline (album_uuid);

-- Album table
create table app_user_data.album(
	album_uuid uuid,
	user_uuid uuid,
	content_uuids list<uuid>,
	name text,
	visibility int,
	created timestamp,
	updated timestamp,
	primary key((album_uuid), created, user_uuid)
) with CLUSTERING order by (created desc);

create index on app_user_data.album (user_uuid);

-- Message session
create table app_user_data.message_session(
	msession_uuid uuid,
	user_uuid_list list<uuid>,
	name text,
	permission int,
	created timestamp,
	updated timestamp,
	primary key((msession_uuid), created)
) with CLUSTERING order by (created desc);

create table app_user_data.message(
	message_uuid uuid,
	msession_uuid uuid,
	user_uuid uuid,
	content text,
	content_type int,
	created timestamp,
	updated timestamp,
	primary key((message_uuid), created)
) with CLUSTERING order by (created desc);

create index on app_user_data.message (msession_uuid);
create index on app_user_data.message (user_uuid);

-- //////////////////////////////////////
-- //////////
-- Application column family
-- //////////
-- //////////////////////////////////////

-- Create application data column family
-- Development configuration
create KEYSPACE app_data
with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

-- Production configuration
-- create KEYSPACE app_data
-- with replication = {'class': 'NetworkTopologyStrategy', 'replication_factor' : 3}
-- and DURABLE_WRITES = true;

 -- user log
 create table app_data.app_user_log(
	ulog_uuid uuid,
	user_uuid uuid,
	source text,
	api_route text,
	ip_address text,
	platfrom text,
	created timestamp,
	primary key((ulog_uuid), created)
 ) with CLUSTERING order by (created desc);
 
 create index on app_data.app_user_log (user_uuid);
 
 -- app client log (api)
 create table app_data.app_client_log(
	client_uuid uuid,
	internal_request text,
	api_route text,
	ip_address text,
	created timestamp,
	primary key(client_uuid, created)
 ) with CLUSTERING order by (created desc);
 
 -- application log
 create table app_data.app_log(
	applog_tuid timestamp,
	client_uuid uuid,
	log_type int,
	content text,
	sender text,
	primary key(client_uuid, applog_tuid)
 ) with CLUSTERING order by (applog_tuid desc);

 -- Oauth2 data 
 create table app_data.oauth2_data(
	access_token uuid,
	client_uuid uuid,
	user_uuid uuid,
	expires timestamp,
	scope text,
	token_type text,
	created timestamp,
	primary key(access_token)
 );
 
 create index on app_data.oauth2_data (user_uuid);
 create index on app_data.oauth2_data (client_uuid);

 -- Oauth2 clients 
 create table app_data.oauth2_client(
	client_uuid uuid,
	user_uuid uuid,
	client_secret text,
	redirect_uri text,
	scope text,
	grant_types list<text>,
	created timestamp,
	primary key(client_uuid)
 );
 
 create index on app_data.oauth2_client (user_uuid);


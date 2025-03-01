DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_database
      WHERE datname = 'crm4ig'
   ) THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE crm4ig');
   END IF;
END
$$;


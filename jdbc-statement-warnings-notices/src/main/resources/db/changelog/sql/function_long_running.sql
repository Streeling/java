CREATE FUNCTION long_running(n int)
RETURNS boolean AS $$
DECLARE
    i int;
BEGIN
    RAISE NOTICE 'Entering long_running(%)', n;
    FOR i IN 1..5 LOOP
        PERFORM pg_sleep(5);
        RAISE NOTICE 'Done unit of work %', i;
    END LOOP;
    RAISE NOTICE 'Exiting long_running(%)', n;
    RETURN TRUE;
END;
$$  LANGUAGE plpgsql;
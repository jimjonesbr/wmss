CREATE OR REPLACE FUNCTION wmss_loadxml(filename text)
  RETURNS XML
  VOLATILE
  LANGUAGE plpgsql AS
$f$
    DECLARE
        CONTENT bytea;
        loid oid;
        lfd INTEGER;
        lsize INTEGER;
BEGIN
        loid := lo_import(filename);
        lfd := lo_open(loid,262144);
        lsize := lo_lseek(lfd,0,2);
        perform lo_lseek(lfd,0,0);
        CONTENT := loread(lfd,lsize);
        perform lo_close(lfd);
        perform lo_unlink(loid);
 
        RETURN XMLPARSE(DOCUMENT convert_from(CONTENT,'UTF8'));
    END;
$f$;
package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author matej
 * @project hw04-0036516398
 * @created 04/11/2020
 */
class QueryParserTest {
    @Test
    void testQueryOneDirectStatement() {
        String query = "query   jmbag =     \"0000000012\"";

        QueryParser parser = new QueryParser(query);
        assertTrue(parser.isDirectQuery());
        assertEquals("0000000012", parser.getQueriedJMBAG());
        assertSame(parser.getQuery().get(0).getStrategy(), FieldValueGetters.JMBAG);
    }

    @Test
    void testQueryOneNonDirectStatement() {
        String query = "query   firstName =     \"Ana\"";

        QueryParser parser = new QueryParser(query);
        assertFalse(parser.isDirectQuery());
        assertSame(parser.getQuery().get(0).getStrategy(), FieldValueGetters.FIRST_NAME);
    }

    @Test
    void testQueryMoreStatements() {
        String query = "query   firstName =     \"Ana\" aNd lastName <= \"Horvat\"";

        QueryParser parser = new QueryParser(query);
        assertFalse(parser.isDirectQuery());
        assertSame(parser.getQuery().get(0).getStrategy(), FieldValueGetters.FIRST_NAME);
        assertSame(parser.getQuery().get(1).getStrategy(), FieldValueGetters.LAST_NAME);
    }

    @Test
    void testQueryMoreStatements2() {
        String query = "query   firstName LIKE     \"A*na\" aNd lastName <= \"Horvat\" AND jmbag=\"000000085\" ";

        QueryParser parser = new QueryParser(query);
        assertFalse(parser.isDirectQuery());
        assertSame(parser.getQuery().get(0).getStrategy(), FieldValueGetters.FIRST_NAME);
        assertSame(parser.getQuery().get(1).getStrategy(), FieldValueGetters.LAST_NAME);
        assertSame(parser.getQuery().get(2).getStrategy(), FieldValueGetters.JMBAG);
        assertEquals("A*na",parser.getQuery().get(0).getLiteral());
    }

    @Test
    void testInvalidQueryThrowsException1() {
        String query = "query   firstName LIKE     \"A*na\" aNd lastName <= \"Horvat\" jmbag=\"000000085\" ";

        assertThrows(QueryException.class,()->{
            new QueryParser(query);
        });
    }

    @Test
    void testInvalidQueryThrowsException2() {
        String query = "query   firstName LIKE     \"A*na\" aNd lastName <= \"Horvat\" and jmbag=\"000000085\" ";

        assertThrows(QueryException.class,()->{
            new QueryParser(query).getQueriedJMBAG();
        });
    }
}
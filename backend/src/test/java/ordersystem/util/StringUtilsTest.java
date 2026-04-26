package ordersystem.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void capitalize_null_deberiaRetornarNull() {
        assertNull(StringUtils.capitalize(null));
    }

    @Test
    void capitalize_vacio_deberiaRetornarVacio() {
        assertEquals("", StringUtils.capitalize(""));
    }

    @Test
    void capitalize_conEspaciosDobles() {
        assertEquals("Pizza Napolitana", StringUtils.capitalize("pizza  napolitana"));
    }

    @Test
    void capitalize_normal() {
        assertEquals("Pizza Margarita", StringUtils.capitalize("pizza margarita"));
    }
}
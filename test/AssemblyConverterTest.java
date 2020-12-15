import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to validate the proper functionality of the AssemblyConverter method.
 * @author Robert Max Davis
 */
class AssemblyConverterTest {

    AssemblyConverter aCon;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        aCon = new AssemblyConverter();
    }

    @org.junit.jupiter.api.Test
    void AssemblyConvertTest() {
        String assemblyCode =
                "main:    LDR     'F',i   ;F >> Accumulator  \n" +
                "while:   SUBR    1,i                        \n" +
                "         CPR     'A',i   ;Acc == A          \n" +
                "         BREQ    endWh   ;If so STOP        \n" +
                "         BR      while   ;Else repeat       \n" +
                "endWh:   STOP                               \n" +
                "         .END ";

        assertEquals("11000000 00000000 01000110 10000000 00000000 00000001 10110000 00000000 01000001 " +
                "00001010 00000000 00001111 00000100 00000000 00000011 00000000 ", aCon.generateHexString(assemblyCode));
    }
}
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to validate the proper functionality of the Calculator method.
 * @author Robert Max Davis
 */
class CalculatorTest {

    private Calculator testCalc;
    private BitString bitString1;
    private BitString bitString2;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        testCalc = new Calculator();
        bitString1 = new BitString();
        bitString2 = new BitString();
    }

    @org.junit.jupiter.api.Test
    void not() {
        //Positive
        bitString1.setValue2sComp(3);
        assertEquals(-4, testCalc.not(bitString1).getValue2sComp());

        //Negative
        bitString1.setValue2sComp(-4);
        assertEquals(3, testCalc.not(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void negate() {
        //Positive
        bitString1.setValue2sComp(3);
        assertEquals(-3, testCalc.negate(bitString1).getValue2sComp());

        //Negative
        bitString1.setValue2sComp(-3);
        assertEquals(3, testCalc.negate(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void shiftLeft() {
        //In Bound Positive
        bitString1.setValue2sComp(35);
        assertEquals(70, testCalc.shiftLeft(bitString1).getValue2sComp());

        //In Bound Negative
        bitString1.setValue2sComp(-35);
        assertEquals(-70, testCalc.shiftLeft(bitString1).getValue2sComp());

        //Out Bound Positive
        bitString1.setValue2sComp(27551);
        assertEquals(-10434, testCalc.shiftLeft(bitString1).getValue2sComp());

        //Out Bound Negative
        bitString1.setValue2sComp(-18993);
        assertEquals(27550, testCalc.shiftLeft(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void shiftRight() {
        //In Bound Positive
        bitString1.setValue2sComp(35);
        assertEquals(17, testCalc.shiftRight(bitString1).getValue2sComp());

        //In Bound Negative
        bitString1.setValue2sComp(-35);
        assertEquals(-18, testCalc.shiftRight(bitString1).getValue2sComp());

        //Out Bound Positive
        bitString1.setValue2sComp(27551);
        assertEquals(13775, testCalc.shiftRight(bitString1).getValue2sComp());

        //Out Bound Negative
        bitString1.setValue2sComp(-18993);
        assertEquals(-9497, testCalc.shiftRight(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void rotateLeft() {
        //Out Bound Negative Odd
        bitString1.setValue2sComp(-18993);
        assertEquals(27551, testCalc.rotateLeft(bitString1).getValue2sComp());

        //Out Bound Negative Even
        bitString1.setValue2sComp(-18992);
        assertEquals(27553, testCalc.rotateLeft(bitString1).getValue2sComp());

        //Out Bound Positive Odd
        bitString1.setValue2sComp(27551);
        assertEquals(-10434, testCalc.rotateLeft(bitString1).getValue2sComp());

        //Out Bound Negative Even
        bitString1.setValue2sComp(27550);
        assertEquals(-10436, testCalc.rotateLeft(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void rotateRight() {
        //Out Bound Negative Odd
        bitString1.setValue2sComp(-18993);
        assertEquals(-9497, testCalc.rotateRight(bitString1).getValue2sComp());

        //Out Bound Negative Even
        bitString1.setValue2sComp(-18992);
        assertEquals(23272, testCalc.rotateRight(bitString1).getValue2sComp());

        //Out Bound Positive Odd
        bitString1.setValue2sComp(27551);
        assertEquals(-18993, testCalc.rotateRight(bitString1).getValue2sComp());

        //Out Bound Positive Even
        bitString1.setValue2sComp(27550);
        assertEquals(13775, testCalc.rotateRight(bitString1).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void add() {
        //In Bound Positive Result
        bitString1.setValue2sComp(10);
        bitString2.setValue2sComp(-8);
        assertEquals(2, testCalc.add(bitString1, bitString2).getValue2sComp());

        //In Bound Negative Result
        bitString1.setValue2sComp(-10);
        bitString2.setValue2sComp(8);
        assertEquals(-2, testCalc.add(bitString1, bitString2).getValue2sComp());

        //Out Bound Positive Result
        bitString1.setValue2sComp(-18000);
        bitString2.setValue2sComp(-16000);
        assertEquals(31536, testCalc.add(bitString1, bitString2).getValue2sComp());

        //Out Bound Negative Result
        bitString1.setValue2sComp(18000);
        bitString2.setValue2sComp(16000);
        assertEquals(-31536, testCalc.add(bitString1, bitString2).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void subtract() {
        //In Bound Positive Result
        bitString1.setValue2sComp(10);
        bitString2.setValue2sComp(-8);
        assertEquals(18, testCalc.subtract(bitString1, bitString2).getValue2sComp());

        //In Bound Negative Result
        bitString1.setValue2sComp(-10);
        bitString2.setValue2sComp(8);
        assertEquals(-18, testCalc.subtract(bitString1, bitString2).getValue2sComp());

        //Out Bound Positive Result
        bitString1.setValue2sComp(-18000);
        bitString2.setValue2sComp(16000);
        assertEquals(31536, testCalc.subtract(bitString1, bitString2).getValue2sComp());

        //Out Bound Negative Result
        bitString1.setValue2sComp(18000);
        bitString2.setValue2sComp(-16000);
        assertEquals(-31536, testCalc.subtract(bitString1, bitString2).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void and() {
        //Positive Result
        bitString1.setValue2sComp(10);
        bitString2.setValue2sComp(-8);
        assertEquals(8, testCalc.and(bitString1, bitString2).getValue2sComp());

        //Negative Result
        bitString1.setValue2sComp(-10);
        bitString2.setValue2sComp(-8);
        assertEquals(-16, testCalc.and(bitString1, bitString2).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void or() {
        //Positive Result
        bitString1.setValue2sComp(17);
        bitString2.setValue2sComp(8);
        assertEquals(25, testCalc.or(bitString1, bitString2).getValue2sComp());

        //Negative Result
        bitString1.setValue2sComp(17);
        bitString2.setValue2sComp(-8);
        assertEquals(-7, testCalc.or(bitString1, bitString2).getValue2sComp());
    }

    @org.junit.jupiter.api.Test
    void updateFlags() {
        //Positive
        bitString1.setValue2sComp(80);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getNFlag());
        assertFalse(testCalc.getZFlag());

        //Zero
        bitString1.setValue2sComp(0);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getNFlag());
        assertTrue(testCalc.getZFlag());

        //Negative
        bitString1.setValue2sComp(-80);
        testCalc.updateFlags(bitString1);
        assertTrue(testCalc.getNFlag());
        assertFalse(testCalc.getZFlag());
    }

    @org.junit.jupiter.api.Test
    void getNFlag() {
        //Negative
        bitString1.setValue2sComp(-8);
        testCalc.updateFlags(bitString1);
        assertTrue(testCalc.getNFlag());

        //Zero
        bitString1.setValue2sComp(0);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getNFlag());

        //Positive
        bitString1.setValue2sComp(8);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getNFlag());
    }

    @org.junit.jupiter.api.Test
    void getZFlag() {
        //Negative
        bitString1.setValue2sComp(-6);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getZFlag());

        //Zero
        bitString1.setValue2sComp(0);
        testCalc.updateFlags(bitString1);
        assertTrue(testCalc.getZFlag());

        //Positive
        bitString1.setValue2sComp(6);
        testCalc.updateFlags(bitString1);
        assertFalse(testCalc.getZFlag());
    }

    @org.junit.jupiter.api.Test
    void getVFlag() {
        //Overflow
        bitString1.setValue2sComp(18000);
        bitString2.setValue2sComp(16000);
        testCalc.add(bitString1, bitString2);
        assertTrue(testCalc.getVFlag());

        //No Overflow
        bitString1.setValue2sComp(-20);
        bitString2.setValue2sComp(-40);
        testCalc.add(bitString1, bitString2);
        assertFalse(testCalc.getVFlag());
    }

    @org.junit.jupiter.api.Test
    void getCFlag() {
        //Carry
        bitString1.setValue2sComp(-20);
        testCalc.shiftLeft(bitString1);
        assertTrue(testCalc.getCFlag());

        //No Carry
        bitString1.setValue2sComp(20);
        testCalc.shiftLeft(bitString1);
        assertFalse(testCalc.getCFlag());
    }
}
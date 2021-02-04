package hr.fer.oprpp1.hw01.demo;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.PI;

import hr.fer.oprpp1.hw01.ComplexNumber;
import org.junit.jupiter.api.Test;

public class ComplexNumberTest {

    @Test
    public void testFromRealMethod() {
        double realInput = 6.1;
        double imaginaryInput = 0.0;
        var complexNumber = ComplexNumber.fromReal(realInput);
        var real = complexNumber.getReal();
        var imaginary = complexNumber.getImaginary();
        assertTrue(realInput == real && imaginary == imaginaryInput);
    }

    @Test
    public void testFromImaginary() {
        double realInput = 0.0;
        double imaginaryInput = 5.1;
        var complexNumber = ComplexNumber.fromImaginary(imaginaryInput);
        var real = complexNumber.getReal();
        var imaginary = complexNumber.getImaginary();
        assertTrue(realInput == real && imaginary == imaginaryInput);
    }

    @Test
    public void testFromMagnitudeAndAngleSimple() {
        assertEquals(new ComplexNumber(1, 1), ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2), PI / 4.0));
    }

    @Test
    public void testFromMagnitudeAndAngleComplex() {
        assertEquals(new ComplexNumber(100, - 8), ComplexNumber.fromMagnitudeAndAngle(4 * Math.sqrt(629), - 0.07982998));
    }

    @Test
    public void testParseOnlyReal() {
        assertEquals(new ComplexNumber(5.3, 0.0), ComplexNumber.parse("5.3"));
    }

    @Test
    public void testParseOnlyReal3() {
        assertEquals(new ComplexNumber(- 555.111, 0.0), ComplexNumber.parse("-555.111"));
    }

    @Test
    public void testParseOnlyImaginary() {
        assertEquals(new ComplexNumber(0, - 5.3), ComplexNumber.parse("-5.3i"));
    }

    @Test
    public void testParseBothValues() {
        assertEquals(new ComplexNumber(5.3, - 3.0), ComplexNumber.parse("5.3-3i"));
    }

    @Test
    public void testParseBothValues2() {
        assertEquals(new ComplexNumber(- 5, 1), ComplexNumber.parse("-5+i"));
    }

    @Test
    public void testParseOnlyI() {
        assertEquals(new ComplexNumber(0, 1.0), ComplexNumber.parse("i"));
    }

    @Test
    public void testParseOnlyINegative() {
        assertEquals(new ComplexNumber(0, - 1.0), ComplexNumber.parse("-i"));
    }

    @Test
    public void testParseWrongArgumentStrangeCharacter() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("5-3d"));
    }

    @Test
    public void testParseWrongArgumentDoubleMinus() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("5--3i"));
    }

    @Test
    public void testParseWrongArgumentPlusMinus() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("5+-3i"));
    }

    @Test
    public void testParseWrongArgumentDoublePlus() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("5++3i"));
    }

    @Test
    public void testParseWrongArgumentTooManyValues() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("5-6+3i"));
    }

    @Test
    public void testGetAngle() {
        assertEquals(PI / 2, new ComplexNumber(0, 1).getAngle());
    }

    @Test
    public void testGetMagnitude() {
        assertEquals(3, new ComplexNumber(3, 0).getMagnitude());
    }

    @Test
    public void testAddMethod() {
        assertEquals(new ComplexNumber(100, 3), new ComplexNumber(94, - 1).add(new ComplexNumber(6, 4)));
    }

    @Test
    public void testSubMethod() {
        assertEquals(new ComplexNumber(100, 3), new ComplexNumber(94, - 1).sub(new ComplexNumber(- 6, - 4)));
    }

    @Test
    public void testMulMethod() {
        assertEquals(new ComplexNumber(23, 14), new ComplexNumber(2, 5).mul(new ComplexNumber(4, - 3)));
    }

    @Test
    public void testDivMethod() {
        assertEquals(new ComplexNumber(1 / 13.0, - 18 / 13.0), new ComplexNumber(3, - 4).div(new ComplexNumber(3, 2)));
    }

    @Test
    public void testPowMethod() {
        assertEquals(new ComplexNumber(122, - 597), new ComplexNumber(2, 3).power(5));
    }

    @Test
    public void testRootMethod() {
        assertEquals(new ComplexNumber(- 1.0106429, 1.1532283), new ComplexNumber(5, 12).root(6)[2]);
    }

    @Test
    public void testToString() {
        assertEquals("1.00-6.20i", new ComplexNumber(1, - 6.2).toString());
    }

    @Test
    public void testAngle1() {
        assertEquals(5 * PI / 4, ComplexNumber.parse("-1-i").getAngle());
    }

    @Test
    public void testAngle2() {
        assertEquals(3 * PI / 4, ComplexNumber.parse("-1+i").getAngle());
    }

    @Test
    public void testAngle3() {
        assertEquals(7 * PI / 4, ComplexNumber.parse("1-i").getAngle());
    }

    @Test
    public void toStringTest(){
        assertEquals("1.50-4.00i",new ComplexNumber(1.5,-4).toString());
    }
}

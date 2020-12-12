/**
 * Performs bitwise and general calculations on BitString objects. Flags denoting
 * specific results from these operation are stored in boolean values.
 * @author Robert Max Davis
 */
public class Calculator {

    private boolean nFlag;
    private boolean zFlag;
    private boolean vFlag;
    private boolean cFlag;

    /**
     * Constructs the Calculator class and sets the NZVP flags to false.
     */
    public Calculator() {
        nFlag = false;
        zFlag = false;
        vFlag = false;
        cFlag = false;
    }

    /**
     * Performs the bitwise not operation on the given BitString.
     * Sets the N and Z flags.
     * @param bitString the given BitString
     * @return a BitString whose bits are inverted from the given BitString
     */
    public BitString not(BitString bitString) {
        BitString newBitString = new BitString();
        int newValue = ~bitString.getValue2sComp();

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Negates the value of the given BitString.
     * Sets the N, Z, and V flags.
     * @param bitString the given BitString
     * @return a BitString whose signed value is negated from the given BitString
     */
    public BitString negate(BitString bitString) {
        BitString newBitString = new BitString();
        int newValue = bitString.getValue2sComp() * -1;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;
        this.vFlag = Math.abs(newValue) == Math.pow(2, bitString.getLength() - 1);

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs the bitwise shift left operation on the given BitString.
     * Sets the N, Z, V, and C flags.
     * @param bitString the given BitString
     * @return a BitString whose bits are shifted left from the given BitString
     */
    public BitString shiftLeft(BitString bitString) {
        BitString newBitString = new BitString();
        int value = bitString.getValue2sComp();
        int newValue = value << 1;
        int mod = 1 << (bitString.getLength() - 1);
        int max = mod - 1;
        int min = mod * - 1;

        if (newValue > max || newValue < min)
            newValue = Math.floorMod((newValue + mod), (2 * mod)) - mod;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;
        this.vFlag = value < (mod >> 1);
        this.cFlag = value < 0;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs the bitwise shift right operation on the given BitString.
     * Sets the N, Z, and C flags.
     * @param bitString the given BitString
     * @return a BitString whose bits are shifted right from the given BitString
     */
    public BitString shiftRight(BitString bitString) {
        BitString newBitString = new BitString();
        int value = bitString.getValue2sComp();
        int newValue = value >> 1;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;
        this.cFlag = value % 2 == 1;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs a circular shift right operation on the given BitString.
     * Sets the C flag.
     * @param bitString the given BitString
     * @return a BitString whose bits are circularly shifted right from the given BitString
     */
    public BitString rotateRight(BitString bitString) {
        BitString newBitString = bitString.copy();
        char[] mBits = newBitString.getBits();
        int endPos = bitString.getLength() - 1;

        char endBit = mBits[endPos];
        for(int i = endPos; i > 0; i--) { mBits[i] = mBits[i - 1]; }
        mBits[0] = endBit;

        this.cFlag = (bitString.getBits())[endPos] == '1';

        return newBitString;
    }

    /**
     * Performs a circular shift left operation on the given BitString.
     * Sets the C flag.
     * @param bitString the given BitString
     * @return a BitString whose bits are circularly shifted left from the given BitString
     */
    public BitString rotateLeft(BitString bitString) {
        BitString newBitString = bitString.copy();
        char[] mBits = newBitString.getBits();
        int endPos = bitString.getLength() - 1;

        char firstBit = mBits[0];
        for(int i = 0; i < endPos; i++) { mBits[i] = mBits[i + 1]; }
        mBits[endPos] = firstBit;

        this.cFlag = (bitString.getBits())[0] == '1';

        return newBitString;
    }

    /**
     * Performs an add operation on the given BitString.
     * Sets the N, Z, V, and C flags.
     * @param bitString the first given BitString
     * @param bitString2 the second given BitString
     * @return a BitString whose bits are the sum of the given BitStrings
     */
    public BitString add(BitString bitString, BitString bitString2) {
        BitString newBitString = new BitString();
        int value1 = bitString.getValue2sComp();
        int value2 = bitString2.getValue2sComp();
        int newValue = value1 + value2;
        int mod = 1 << (bitString.getLength() - 1);
        int max = mod - 1;
        int min = mod * - 1;

        if (newValue > max || newValue < min)
            newValue = Math.floorMod((newValue + mod), (2 * mod)) - mod;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;
        this.vFlag = (value1 < 0 == value2 < 0) && (value1 < 0 != newValue < 0);
        this.cFlag = newValue > max;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs a subtract operation on the given BitString.
     * Sets the N, Z, V, and C flags.
     * @param bitString the first given BitString
     * @param bitString2 the second given BitString
     * @return a BitString whose bits are the difference of the given BitStrings
     */
    public BitString subtract(BitString bitString, BitString bitString2) {
        BitString newBitString = new BitString();
        int value1 = bitString.getValue2sComp();
        int value2 = bitString2.getValue2sComp();
        int newValue = value1 - value2;
        int mod = 1 << (bitString.getLength() - 1);
        int max = mod - 1;
        int min = mod * - 1;

        if (newValue > max || newValue < min)
            newValue = Math.floorMod((newValue + mod), (2 * mod)) - mod;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;
        this.vFlag = (value1 < 0 != value2 < 0) && (value1 < 0 != newValue < 0);
        this.cFlag = Math.abs(value1) < Math.abs(value2);

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs a bitwise and operation on the given BitString.
     * Sets the N and Z flags.
     * @param bitString the first given BitString
     * @param bitString2 the second given BitString
     * @return a BitString whose bits are bitwise and of the given BitStrings
     */
    public BitString and(BitString bitString, BitString bitString2) {
        BitString newBitString = new BitString();
        int value1 = bitString.getValue2sComp();
        int value2 = bitString2.getValue2sComp();
        int newValue = value1 & value2;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Performs a bitwise or operation on the given BitString.
     * Sets the N and Z flags.
     * @param bitString the first given BitString
     * @param bitString2 the second given BitString
     * @return a BitString whose bits are bitwise or of the given BitStrings
     */
    public BitString or(BitString bitString, BitString bitString2) {
        BitString newBitString = new BitString();
        int value1 = bitString.getValue2sComp();
        int value2 = bitString2.getValue2sComp();
        int newValue = value1 | value2;

        this.nFlag = newValue < 0;
        this.zFlag = newValue == 0;

        newBitString.setValue2sComp(newValue);
        return newBitString;
    }

    /**
     * Updates the N and Z flags from the given value. This method is designed for LD and
     * DECI instructions. While calculations are not performed on them, they do set flags.
     * Sets the N and Z flags.
     * @param bitString the given bitString
     */
    public void updateFlags(BitString bitString) {
        int bitStringValue = bitString.getValue2sComp();
        this.nFlag = bitStringValue < 0;
        this.zFlag = bitStringValue == 0;
    }

    /**
     * Returns the boolean value representing negation.
     * @return the negative flag
     */
    public boolean getNFlag() {return this.nFlag;}

    /**
     * Returns the boolean value representing zero.
     * @return the zero flag
     */
    public boolean getZFlag() {return this.zFlag;}

    /**
     * Returns the boolean value representing overflow.
     * @return the overflow flag
     */
    public boolean getVFlag() {return this.vFlag;}

    /**
     * Returns the boolean value representing carry over.
     * @return the carry flag
     */
    public boolean getCFlag() {return this.cFlag;}
    
    /**
     * Sets the boolean value representing negation.
     * @param theBit the negative flag
     */
    public void setNFlag(boolean theBit) {this.nFlag = theBit;}
    
    /**
     * Sets the boolean value representing zero.
     * @param theBit the zero flag
     */
    public void setZFlag(boolean theBit) {this.zFlag = theBit;}
    
    /**
     * Sets the boolean value representing overflow.
     * @param theBit the overflow flag
     */
    public void setVFlag(boolean theBit) {this.vFlag = theBit;}
    
    /**
     * Sets the boolean value representing carry over.
     * @param theBit the carry flag
     */
    public void setCFlag(boolean theBit) {this.cFlag = theBit;}
}

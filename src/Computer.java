/**
 * Computer class comprises of memory and accumulator (register A) and
 * can execute the instructions based on PC and IR.
 * @author Robert Max Davis
 * @author Daniel Ty
 */
public class Computer {

    private final static int MAX_MEMORY = 24; //Feel free to increase if necessary

    private Calculator bitCalc;
    private BitString mMemory[];
    private BitString mPC;
    private BitString mIR;
    private BitString mA;

    private StringBuilder output;

    /**
     * Initializes initial state
     */
    public Computer() {
        bitCalc = new Calculator();
        mPC = new BitString();
        mPC.setValue(0);
        mIR = new BitString();
        mIR.setValue(0);
        mA = new BitString();
        mA.setValue(0);

        output = new StringBuilder();

        mMemory = new BitString[MAX_MEMORY];
        for (int i = 0; i < MAX_MEMORY; i++) {
            mMemory[i] = new BitString();
            mMemory[i].setBits(new char [] {'0','0', '0','0', '0','0', '0','0'});
        }

    }
    
    /**
     * A getter for the Calculator. This is to be used only for 
     * testing purposes.
     * @return the Calculator
     */
    public Calculator getCalculator() {return bitCalc;}
    
    /**
     * A getter for the value of the PC. This is to be used only for
     * testing purposes.
     * @return the PC
     */
    public BitString getPC() {return mPC.copy();}

    /**
     * Returns the BitString at the register specified by theRegister
     * @return the BitString at the register
     */
    public BitString getRegister() {
        return mA.copy();
    }

    public void setRegister(BitString theData) {
        mA = theData;
    }

    /**
     * Loads a BitString into memory at current address, then updates address by one
     * @param theBits The bit string instruction (in binary)
     */
    public void loadWord(String theBits) {
        // Convert bit lines into 8-bit bitstrings and load into computer.
        // Handles bit lines that contain 24, 16, or 8 bits.
        theBits = theBits.replaceAll(" ", "");
        if (theBits.length() == 24) {
            BitString first = new BitString();
            first.setBits(theBits.substring(0, 8).toCharArray());
            mMemory[mPC.getValue()] = first;
            mPC.addOne();

            BitString second = new BitString();
            second.setBits(theBits.substring(8, 16).toCharArray());
            mMemory[mPC.getValue()] = second;
            mPC.addOne();

            BitString third = new BitString();
            third.setBits(theBits.substring(16, 24).toCharArray());
            mMemory[mPC.getValue()] = third;
            mPC.addOne();

        } else if (theBits.length() == 16) {
            BitString first = new BitString();
            first.setBits(theBits.substring(0, 8).toCharArray());
            mMemory[mPC.getValue()] = first;
            mPC.addOne();

            BitString second = new BitString();
            second.setBits(theBits.substring(8, 16).toCharArray());
            mMemory[mPC.getValue()] = second;
            mPC.addOne();

        } else if (theBits.length() == 8) {
            BitString bits = new BitString();
            bits.setBits(theBits.toCharArray());
            mMemory[mPC.getValue()] = bits;
            mPC.addOne();
        }

    }

    /**
     * Loads a 24 bit word into memory at the given address.
     * @param theBits a string of bits
     * @param theAddress data or instruction or address to be loaded into memory
     */
    public void loadWord(String theBits, int theAddress) {
        if (theBits.length() == 24) {
            BitString first = new BitString();
            first.setBits(theBits.substring(0, 8).toCharArray());
            mMemory[theAddress] = first;

            BitString second = new BitString();
            second.setBits(theBits.substring(8, 16).toCharArray());
            mMemory[theAddress + 1] = second;

            BitString third = new BitString();
            third.setBits(theBits.substring(16, 24).toCharArray());
            mMemory[theAddress + 2] = third;

        } else if (theBits.length() == 16) {
            BitString first = new BitString();
            first.setBits(theBits.substring(0, 8).toCharArray());
            mMemory[theAddress] = first;

            BitString second = new BitString();
            second.setBits(theBits.substring(8, 16).toCharArray());
            mMemory[theAddress + 1] = second;

        } else if (theBits.length() == 8) {
            BitString first = new BitString();
            first.setBits(theBits.substring(0, 8).toCharArray());
            mMemory[theAddress] = first;
        }
    }

    /**
     * Returns the BitString at address of theMemory
     * @param theMemory the memory address
     * @return the BitString at the memory address
     */
    public BitString getMemory(int theMemory) {
        return mMemory[theMemory];
    }

    public String getOutput() {
        return output.toString();
    }

    /**
     * Selects the next two BitStrings stored in memory and concatenates them
     * in order to create a 16 bit BitString.
     * @return the length 16 BitString formed from the next two BitStrings in memory
     */
    private BitString concatenateWords() {
        char[] wordArray = new char[16];
        int destPos = 0;

        for (int i = 0; i < 2; i++) {
            mIR = mMemory[mPC.getValue()].copy();
            char[] word = mIR.getBits().clone();
            mPC.addOne();

            System.arraycopy(word, 0, wordArray, destPos, 8);
            destPos += 8;
        }

        BitString opBS = new BitString();
        opBS.setBits(wordArray);
        return opBS;
    }

    /**
     * Selects the two concurrent BitStrings stored in memory at the specified address and concatenates them in order
     * to create a 16 bit BitString
     * @param address the address of the first BitString in memory
     * @return the length 16 BitString formed from the next two BitStrings in memory
     */
    private BitString concatenateWords(int address) {
        char[] wordArray = new char[16];
        int destPos = 0;

        if (address < 0 || address > 255) {
            throw new IllegalArgumentException("Address Request is Out of Bounds");
        }

        for (int i = 0; i < 2; i++) {
            char[] word = mMemory[address + i].getBits();

            System.arraycopy(word, 0, wordArray, destPos, 8);
            destPos += 8;
        }

        BitString opBS = new BitString();
        opBS.setBits(wordArray);
        return opBS;
    }
    
    /**
     * Performs a branch operation if C status bit is true,
     * immediate instruction.
     * (specifier: 0001 0100)
     */
    public void executeBRC() {
        BitString operand = concatenateWords();
        if(bitCalc.getCFlag())
            mPC.setValue2sComp(operand.getValue() - 1); 
        	// Subtract 1 since PC adds 1 automatically
    }
    
    /**
     * Execute NOTr instruction
     * (specifier:0001 1000)
     */
    public void executeNOTr() {
        BitString bitsA = mA.copy();
        mA = bitCalc.not(bitsA);
        int result = mA.getValue();
        bitCalc.setNFlag(result < 0);
        bitCalc.setZFlag(result == 0);
    }
    
    /**
     * Execute NEGr instruction
     * (specifier:0001 1010)
     */
    public void executeNEGr() {
    	BitString bitsA = mA.copy();
        mA = bitCalc.negate(bitsA);
        int result = mA.getValue();
        bitCalc.setNFlag(result < 0);
        bitCalc.setZFlag(result == 0);
        bitCalc.setVFlag(result < (-1 << 15) || result >= (1 << 15));
    }
    
    /**
     * Execute ASLr instruction
     * (specifier:0001 1100)
     */
    public void executeASLr() {
    	BitString bitsA = mA.copy();
        mA = bitCalc.shiftLeft(bitsA);
        int result = mA.getValue();
        bitCalc.setNFlag(result < 0);
        bitCalc.setZFlag(result == 0);
        bitCalc.setVFlag(result < (-1 << 15) || result >= (1 << 15));
        bitCalc.setCFlag(result >= (1 << 15));
    }
    
    /**
     * Execute ASRr instruction
     * (specifier:0001 1110)
     */
    public void executeASRr() {
    	BitString bitsA = mA.copy();
        mA = bitCalc.shiftRight(bitsA);
        int result = mA.getValue();
        bitCalc.setNFlag(result < 0);
        bitCalc.setZFlag(result == 0);
        bitCalc.setCFlag(result >= (1 << 15));
    }
    
    /**
     * Execute ROLr instruction
     * (specifier:0010 0000)
     */
    public void executeROLr() {
    	BitString bitsA = mA.copy();
        mA = bitCalc.rotateLeft(bitsA);
        int result = mA.getValue();
        bitCalc.setCFlag(result >= (1 << 15));
    }
    
    /**
     * Execute RORr instruction
     * (specifier:0010 0010)
     */
    public void executeRORr() {
    	BitString bitsA = mA.copy();
        mA = bitCalc.rotateRight(bitsA);
        int result = mA.getValue();
        bitCalc.setCFlag(result >= (1 << 15));
    }
    

    /**
     * Performs Character output from the operand, immediate instruction.
     * (specifier: 0101 0000)
     */
    public void executeChOutI() {
        BitString operand = concatenateWords();
        output.append((char)operand.getValue());
    }

    /**
     * Performs Character output from the operand, direct instruction.
     * (specifier: 0101 0001)
     */
    public void executeChOutD() {
        BitString operand = concatenateWords();
        int address = operand.getValue();
        BitString memBits = concatenateWords(address);
        output.append((char)memBits.getValue());
    }

    /**
     * Performs add the operand to the A register, immediate instruction.
     * (specifier: 0111 0000)
     */
    public void executeAddI() {
        BitString operand = concatenateWords();
        mA = bitCalc.add(mA, operand).copy();
    }

    /**
     * Performs add the operand to the A register, direct instruction.
     * (specifier: 0111 0001)
     */
    public void executeAddD() {
        BitString operand = concatenateWords();
        int address = operand.getValue2sComp();
        BitString memBits = concatenateWords(address);
        mA = bitCalc.add(mA, memBits).copy();
    }

    /**
     * Performs subtract the operand from the A register, immediate instruction.
     * (specifier: 1000 0000)
     */
    public void executeSubtractI() {
        BitString operand = concatenateWords();
        mA = bitCalc.subtract(mA, operand).copy();
    }

    /**
     * Performs subtract the operand from the A register, direct instruction.
     * (specifier: 1000 0001)
     */
    public void executeSubtractD() {
        BitString operand = concatenateWords();
        int address = operand.getValue2sComp();
        BitString memBits = concatenateWords(address);
        mA = bitCalc.subtract(mA, memBits).copy();
    }


    /**
     * Performs bitwise AND from the A register, immediate instruction.
     * (specifier: 1001 0000)
     */
    public void executeAndI() {
        BitString operand = concatenateWords();
        mA = bitCalc.and(mA, operand).copy();
    }

    /**
     * Performs bitwise AND from the A register, direct instruction.
     * (specifier: 1001 0001)
     */
    public void executeAndD() {
        BitString operand = concatenateWords();
        int address = operand.getValue2sComp();
        BitString memBits = concatenateWords(address);
        mA = bitCalc.and(mA, memBits).copy();
    }

    /**
     * Performs bitwise OR from the A register, immediate instruction.
     * (specifier: 1010 0000)
     */
    public void executeOrI() {
        BitString operand = concatenateWords();
        mA = bitCalc.or(mA, operand).copy();
    }

    /**
     * Performs bitwise OR from the A register, direct instruction.
     * (specifier: 1010 0001)
     */
    public void executeOrD() {
        BitString operand = concatenateWords();
        int address = operand.getValue2sComp();
        BitString memBits = concatenateWords(address);
        mA = bitCalc.or(mA, memBits).copy();
    }

    /**
     * Sets NZVC status bits based off of the difference of the
     * A register and operand, immediate instruction.
     * (specifier: 10110000)
     */
    public void executeCPr() {
        int diff = 0;
        BitString operand = concatenateWords();
        diff = mA.getValue2sComp() - operand.getValue2sComp();
        bitCalc.setNFlag(diff < 0);
        bitCalc.setZFlag(diff == 0);
        bitCalc.setVFlag(diff < (-1 << 15) || diff >= (1 << 15));
        bitCalc.setCFlag(diff >= (1 << 15));
    }

    /**
     * Performs load the operand into the A register, immediate instruction.
     * (specifier: 1100 0000)
     */
    public void executeLdI() {
        BitString operand = concatenateWords();
        bitCalc.updateFlags(mA);
        mA = operand.copy();
    }

    /**
     * Performs load the operand into the A register, direct instruction.
     * (specifier: 1100 0001)
     */
    public void executeLdD() {
        BitString operand = concatenateWords();
        int address = operand.getValue();
        BitString memBits = concatenateWords(address);
        mA = memBits.copy();
    }

    /**
     * Performs store the A register to the operand instruction.
     * (specifier: 1110 0001)
     */
    public void executeSt() {
        BitString bitsA = mA.copy();
        String stringA = String.copyValueOf(bitsA.getBits());
        BitString operand = concatenateWords();
        int opAddress = operand.getValue();
        loadWord(stringA, opAddress);
    }

    public void executeBR() {
        BitString operand = concatenateWords();
        mPC.setValue(operand.getValue());
    }
    
    /**
     * Executes the various opCodes loaded into memory
     */
    public void execute() {
        int counter = 0;
        BitString specifierStr;
        int specifier;
        mPC.setValue(0);

        while (true) {
            // Fetch the instruction
            mIR = mMemory[mPC.getValue()];
            mPC.addOne();

            // Decode the instruction's first 8 bits
            // to figure out the instruction specifier's decimal value
            specifierStr = mIR.substring(0, 8);
            specifier = specifierStr.getValue();

            switch (specifier) {
                case 4:					//(specifier: 0000 0100)
                    executeBR();		//Branch unconditional
                    break;
                case 20:				//(specifier: 0001 0100)
                	executeBRC();		//Branch if carry, Immediate
                	break;
                case 24:				//(specifier: 0001 1000)
                	executeNOTr();		//Bitwise invert r
                	break;
                case 26:				//(specifier:0001 1010)
                	executeNEGr();      //Negate r
                	break;
                case 28:				//(specifier:0001 1100)
                	executeASLr();      //Arithmetic shift left
                	break;
                case 30:				//(specifier:0001 1110)
                	executeASRr();		//Arithmetic shift left
                	break;		
                case 32:				//(specifier:0010 0000)
                	executeROLr();		//Rotate left r
                	break;
                case 34:				//(specifier:0010 0010)
                	executeRORr();		//Rotate right r
                	break;
                case 80: 				//(specifier: 0101 0000)
                    executeChOutI();	//Char Output Immediate
                    break;
                case 81: 				//(specifier: 0101 0001)
                    executeChOutD();	//Char Output Direct
                    break;
                case 112: 				//(specifier: 0111 0000)
                    executeAddI();		//Add Immediate
                    break;
                case 113: 				//(specifier: 0111 0001)
                    executeAddD();		//Add Direct
                    break;
                case 128: 				//(specifier: 1000 0000)
                    executeSubtractI();	//Subtract Immediate
                    break;
                case 129: 				//(specifier: 1000 0001)
                    executeSubtractD();	//Subtract Direct
                    break;
                case 144:               //(specifier: 1001 0000)
                    executeAndI();      //And Immediate
                    break;
                case 145:               //(specifier: 1001 0001)
                    executeAndD();      //And Direct
                    break;
                case 160:               //(specifier: 1010 0000)
                    executeOrI();      //Or Immediate
                    break;
                case 161:               //(specifier: 1010 0001)
                    executeOrD();      //Or Direct
                    break;
                case 192: 				//(specifier: 1100 0000)
                    executeLdI();		//Load Immediate
                    break;
                case 193: 				//(specifier: 1100 0001)
                    executeLdD();		//Load Direct
                    break;
                case 225:				//(specifier: 1110 0001)
                    executeSt();		//Store
                    break;
                case 0: 				//(specifier: 0000 0000)
                    System.out.println("Memory:");
                    for (BitString b: mMemory) {
                        System.out.print("(" + counter +")" + "\t");
                        System.out.println(b.getBits());
                        counter++;
                    }
                    return;				//Stop
            }
        }
    }
}

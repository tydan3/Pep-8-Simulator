
/**
 * Computer class comprises of memory and accumulator (register A) and
 * can execute the instructions based on PC and IR.
 * @author Robert Max Davis
 * @author Daniel Ty
 */
public class Computer {

    private final static int MAX_MEMORY = 24; //Feel free to increase if necessary

    private BitString mMemory[];
    private BitString mPC;
    private BitString mIR;
    private BitString mA;
    private int address;
    
	private StringBuilder output;

    /**
     * Initializes initial state
     */
    public Computer() {
        mPC = new BitString();
        mPC.setValue(0);
        mIR = new BitString();
        mIR.setValue(0);
        mA = new BitString();
        mA.setValue(0);
        address = 0;

        output = new StringBuilder();
        
        mMemory = new BitString[MAX_MEMORY];
        for (int i = 0; i < MAX_MEMORY; i++) {
            mMemory[i] = new BitString();
            mMemory[i].setBits(new char [] {'0','0', '0','0', '0','0', '0','0'});
        }
        
    }

    /**
     * Returns the BitString at the register specified by theRegister
     * @param theRegister the register specified
     * @return the BitString at the register
     */
    public BitString getRegister(int theRegister) {
        return mA;
    }

	public void setRegister(BitString theData) {
		mA = theData;
	}

	/**
	 * Loads a 24 bit word into memory at the given address. 
	 * @param address memory address
	 * @param word data or instruction or address to be loaded into memory
	 */
	public void loadWord(int address, BitString word) {
		if (address < 0 || address >= MAX_MEMORY) {
			throw new IllegalArgumentException("Invalid address");
		}
		mMemory[address] = word;
	}
	
    /**
     * Loads a BitString into memory at current address, then updates address by one
     * @param theBits The bit string instruction (in binary)
     */
    public void loadWord(BitString theBits) {
    	mMemory[address] = theBits;
    	address++;
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
    	int sum = mA.getValue() + operand.getValue();
    	mA.setValue(sum);
    }

    /**
     * Performs add the operand to the A register, direct instruction.
     * (specifier: 0111 0001)
     */
    public void executeAddD() {
    	BitString operand = concatenateWords();
    	int address = operand.getValue();
    	BitString memBits = concatenateWords(address);
    	int sum = mA.getValue() + memBits.getValue();
    	mA.setValue(sum);
    	
    }
    
    /**
     * Performs subtract the operand from the A register, immediate instruction.
     * (specifier: 1000 0000)
     */
    public void executeSubtractI() {
    	BitString operand = concatenateWords();    	
    	int diff = mA.getValue() - operand.getValue();
    	mA.setValue(diff);
    }
    
    /**
     * Performs subtract the operand from the A register, direct instruction.
     * (specifier: 1000 0001)
     */
    public void executeSubtractD() {
    	BitString operand = concatenateWords();
    	int address = operand.getValue();
    	BitString memBits = concatenateWords(address);
    	int diff = mA.getValue() - memBits.getValue();
    	mA.setValue(diff);
    }
    
    /**
     * Performs load the operand into the A register, immediate instruction.
     * (specifier: 1100 0000)
     */
    public void executeLdI() {
    	BitString operand = concatenateWords();
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
    	int address = operand.getValue();
    	
    	// Convert Register A bits into 8-bit bitstrings and stores them to addresses.
    	// Handles Register A when it contains 24, 16, or 8 bits.
		if (stringA.length() == 24) {          
			BitString first = new BitString();
			first.setBits(stringA.substring(0, 8).toCharArray());
			mMemory[address] = first;
			
			BitString second = new BitString();
			second.setBits(stringA.substring(8, 16).toCharArray());
			mMemory[address + 1] = second;
			
			BitString third = new BitString();
			third.setBits(stringA.substring(16, 24).toCharArray());
			mMemory[address + 2] = third;
			
		} else if (stringA.length() == 16) {          
			BitString first = new BitString();
			first.setBits(stringA.substring(0, 8).toCharArray());
			mMemory[address] = first;
			
			BitString second = new BitString();
			second.setBits(stringA.substring(8, 16).toCharArray());
			mMemory[address + 1] = second;
			
		} else if (stringA.length() == 8) {
			BitString first = new BitString();
			first.setBits(stringA.substring(0, 8).toCharArray());
			mMemory[address] = first;
		}
		
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

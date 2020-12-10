import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author Daniel Ty
 *
 */
class ComputerTest {
	private Computer testComputer;
	private BitString testBitString;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		testComputer = new Computer();
		testBitString = new BitString();
	}

	@Test
	void executeChOutITest() {
		System.out.println();
		System.out.print("executeChOutITest ");
		
		testComputer.loadWord("0101 0000 0000 0000 0100 1000"); //Output immediate: ascii char H (#72)
		testComputer.execute();
		assertEquals("H", testComputer.getOutput());
	}

	@Test
	void executeChOutDTest() {
		System.out.println();
		System.out.print("executeChOutDTest ");
		
		testComputer.loadWord("0101 0001 0000 0000 0000 0100"); //Output direct: ascii char K (#50) <- address 5
		testComputer.loadWord("0000 0000"); //Stop
		testComputer.loadWord("0000 0000 0100 1011"); //#50 -> address 5
		testComputer.execute();
		assertEquals("K", testComputer.getOutput());
	}
	
	@Test
	void executeAddITest() {
		System.out.println();
		System.out.print("executeAddITest ");
		
		testComputer.loadWord("0111 0000 0000 0010 1100 1000"); //Add immediate: accumulator + #712
		testComputer.execute();
		assertEquals(712, testComputer.getRegister().getValue());
		
	}	
	
	@Test
	void executeAddDTest() {
		System.out.println();
		System.out.print("executeAddDTest ");
		
		testComputer.loadWord("0111 0001 0000 0000 0000 0100"); //Add direct: accumulator + address 4 (#1000)
		testComputer.loadWord("0000 0000"); //Stop
		testComputer.loadWord("0000 0011 1110 1000"); //#1000 -> address 4
		testComputer.execute();
		assertEquals(1000, testComputer.getRegister().getValue());
	}	
	
	@Test
	void executeSubtractITest() {
		System.out.println();
		System.out.print("executeSubtractITest ");
		
		testComputer.loadWord("1000 0000 0000 0010 0101 1000"); //Subtract immediate: accumulator - #600
		testComputer.execute();
		assertEquals(-600, testComputer.getRegister().getValue2sComp());
	}	
	
	@Test
	void executeSubtractDTest() {
		System.out.println();
		System.out.print("executeSubtractDTest ");
		
		testComputer.loadWord("1000 0001 0000 0000 0000 0100"); //Subtract direct: accumulator - address 4 (#890)
		testComputer.loadWord("0000 0000"); //Stop
		testComputer.loadWord("0000 0011 0111 1010"); //#890 -> address 4
		testComputer.execute();
		assertEquals(-890, testComputer.getRegister().getValue2sComp());
	}
	
	@Test
	void executeLdITest() {
		System.out.println();
		System.out.print("executeLdITest ");
		
		testComputer.loadWord("1100 0000 0000 0000 0011 0010"); //Load immediate: 0000 0000 0011 0010 -> accumulator
		testComputer.execute();
		assertTrue("0000000000110010".equals(testComputer.getRegister().toString()));
	}
	
	@Test 
	void executeLdDTest() {
		System.out.println();
		System.out.print("executeLdDTest ");
		
		testComputer.loadWord("1100 0001 0000 0000 0000 0100");
		testComputer.loadWord("0000 0000");
		testComputer.loadWord("0000 0000 0100 0100");
		testComputer.execute();
		assertTrue("0000000001000100".equals(testComputer.getRegister().toString()));
	}
	
	@Test
	void executeStTest() {
		System.out.println();
		System.out.print("executeStTest ");
		
		testBitString.setBits("0000000001000100".toCharArray());
		testComputer.setRegister(testBitString);
		testComputer.loadWord("1110 0001 0000 0000 0000 1111");
		testComputer.execute();
		assertTrue("0000000001000100".equals(testComputer.getMemory(15).toString() 
				+ testComputer.getMemory(16).toString()));
	}
}

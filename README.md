# pep8-simulator
README:
This program is meant to simulate the Pep/8 virtual machine. Currently, this
implementation supports asm code and 23 Pep/8 instructions in immediate or direct 
adddressing modes where applicable. It consists of a BitString Class (bitstring creation
and modification), a Computer Class (memory, instructions, IR, PC, Accumulator), 
a MainFrame Class (GUI, input, output), a Calculator class for setting flags, and
a AssemblyConverter Class (converts asm to bin).

How our program works is our GUI takes in the of asm code from the source code window,
uses the an AssemblyConverter to convert that asm code into String of binary. Those
binary lines are split into 8-bits and loaded into the Computer Class.
Each of those lines are converted BitString loaded into memory. Each 8-bit BitString will
take up a single address in Memory. 

Memory only consists of 32 addresses currently for readability, but can be modified by
changing the value for MAX_MEMORY in the Computer Class. The memory can be seen in our GUI;
it will show the addresses with values in binary. This is the memory post-execution.

To run this program, run the 'MainFrame' class. This will bring up a user interface
for user interaction. Input asm code into the Source code window and click run. 
When given the appropriate instructions, output will appear in the Output window, the
value in the accumulator will appear in the Accumulator window (in decimal), and the
memory will appear in the Memory(bin) window.


Supported Instructions:
-----------------------------------------------------
STOP
NOTR
NEGR
ASLR
ASRR
ROLR
RORR

BR
BRLE
BREQ
BRLT
BRNE
BRGE
BRV
BRC

CHARO
ADDR
SUBR
ANDR
ORR
CPR
LDR
STR



Examples:
-----------------------------------------------------
	CHARO	'U',i	; Output 'U'
	CHARO	'W',i	; Output 'W'
	STOP                        
	.END           
-----------------------------------------------------
	ADDR	10,i	; Accumulator + 70
	SUBR	4,i	; Accumulator - 4
	STOP
	.END
-----------------------------------------------------
	LDR	'A',i	; 'A'-> Accumulator
	STR	11,i	; Accumulator >> Address 11
	CHARO	11,d	; Output Address 11
	STOP
	.END
-----------------------------------------------------
	main:	LDR	70,i	; 70 >> Accumulator
	        STR	28,i
	        CHARO	28,d
    while:	SUBR	1,i
            CPR	70,i	; Acc == A
            BREQ	endWh	; If so STOP
	        BR	while	; Else repeat
    endWh:	STOP
	        .END
	
  
-----------------------------------------------------


  

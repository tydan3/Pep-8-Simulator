import java.util.*;
import java.util.stream.IntStream;

/**
 * Converts assembly code from the the given input into binary code.
 * @author Robert Max Davis
 */
public class AssemblyConverter {
    private static HashMap<String, Integer> offsetMap;
    private static ArrayList<ArrayList<String>> arrList;
    private static ArrayList<String> binList;

    /**
     * Initializes the private variables
     */
    public AssemblyConverter() {
        offsetMap = new HashMap<>();
        arrList = new ArrayList<>();
        binList = new ArrayList<>();
    }

    /**
     * Performs the conversion operation on the assemblyCode parameter to binary code.
     *
     * @param assemblyCode A string of assembly code
     * @return A string of binary code
     */
    public String generateHexString(String assemblyCode) {
        if (assemblyCode.isBlank()) {
            return "";
        }

        //Splits the assembly code into tokens for parsing
        String assembly = assemblyCode.replaceAll("[,\t]", " ").toUpperCase();
        String[] assemblyArr = assembly.split("\n");

        //Initializes an arrayList of each individual instruction's tokens inside
        //the primary arrayList
        for (int i = 0; i < assemblyArr.length; i++) {
            String[] assemblyComment = assemblyArr[i].split(";");
            StringTokenizer strToken = new StringTokenizer(assemblyComment[0], " ");
            arrList.add(new ArrayList<>());

            while (strToken.hasMoreTokens()) {
                String s = strToken.nextToken();
                arrList.get(i).add(s);
            }
        }

        //Generates the binary code for each instruction which are added to the hexList
        for (ArrayList<String> strings : arrList) {
            String tag = null;

            ArrayList<String> currentArr = new ArrayList<>(strings);
            if (currentArr.get(0).contains(":")) {
                tag = currentArr.get(0);
                currentArr.remove(0);
            }

            if (currentArr.size() == 1) {
                addOpCode(currentArr.get(0), tag);
            } else if (currentArr.size() == 2) {
                addOpCode(currentArr.get(0), currentArr.get(1), tag);
            } else {
                addOpcode(currentArr.get(0), currentArr.get(1), currentArr.get(2), tag);
            }
        }

        //Tabulates the offset tags and records them in a map
        for (int i = 0; i < binList.size(); i++) {
            if (binList.get(i).contains(":")) {
                String[] hexArr = binList.get(i).split(":");
                offsetMap.put(hexArr[0], i);
                binList.set(i, hexArr[1]);
            }
        }

        return listToBinString();
    }

    /**
     * Calculates the binary code for operations that do not need input from
     * immediate or direct.
     *
     * @param opcode The instruction intended to be performed
     * @param tag The offset tag for branch instructions
     */
    private void addOpCode(String opcode, String tag) {
        boolean exists = true;
        String instr = "";
        
        if (tag != null) {
            instr = tag;
        }

        switch (opcode) {
            case "STOP" -> instr += "00000000";
            case "NOTR" -> instr += "00011000";
            case "NEGR" -> instr += "00011010";
            case "ASLR" -> instr += "00011100";
            case "ASRR" -> instr += "00011110";
            case "ROLR" -> instr += "00100000";
            case "RORR" -> instr += "00100010";
            default -> exists = false;
        }

        if (exists) {
            binList.add(instr);
        }
    }

    /**
     * Calculates the binary code for branch operations.
     *
     * @param opcode The instruction intended to be performed
     * @param offset The tag to which the branch instruction should offset
     * @param tag The offset tag for branch instructions
     */
    private void addOpCode(String opcode, String offset, String tag) {
        boolean exists = true;
        String instr = "";

        if (tag != null) {
            instr = tag;
        }

        switch (opcode) {
            case "BR" -> instr += "00000100";
            case "BRLE" -> instr += "00000110";
            case "BRLT" -> instr += "00001000";
            case "BREQ" -> instr += "00001010";
            case "BRNE" -> instr += "00001100";
            case "BRGE" -> instr += "00001110";
            case "BRV" -> instr += "00010010";
            case "BRC" -> instr += "00010100";
            default -> exists = false;
        }

        if (exists) {
            binList.add(instr + "!" + offset);
            IntStream.range(0, 2).forEachOrdered(i -> binList.add("0"));
        }
    }

    /**
     * Calculates the binary code for operations that require an
     * immediate or direct addressing mode.
     *
     * @param opcode The instruction intended to be performed
     * @param value The value for either the immediate or the direct
     * @param mode The addressing mode
     * @param tag The offset tag for branch instructions
     */
    private void addOpcode(String opcode, String value, String mode, String tag) {
        boolean exists = true;
        String instr = "";

        if (tag != null) {
            instr = tag;
        }

        switch (opcode) {
            case "CHARI" -> instr += "010010";
            case "CHARO" -> instr += "010100";
            case "ADDR" -> instr += "011100";
            case "SUBR" -> instr += "100000";
            case "ANDR" -> instr += "100100";
            case "ORR" -> instr += "101000";
            case "CPR" -> instr += "101100";
            case "LDR" -> instr += "110000";
            case "STR" -> instr += "111000";
            default -> exists = false;
        }

        if (mode.equals("I")) {
            instr += "00";
        } else if (mode.equals("D")) {
            instr += "01";
        } else {
            instr += "11";
        }

        if (exists) {
            StringBuilder hexBuild = new StringBuilder();
            binList.add(instr);

            char tempChar = value.charAt(0);
            int valDec;

            if (tempChar == '\'')
                valDec = value.charAt(1);
            else
                valDec = Integer.parseInt(value);

            String binVal = Integer.toBinaryString(valDec);
            hexBuild.append("0".repeat(Math.max(0, 16 - binVal.length())));
            hexBuild.append(binVal);

            for (int i = 0; i < 2; i++)
                binList.add(hexBuild.substring(8 * i, 8 * i + 8));
        }
    }

    /**
     * Generates the binary string associated with the instructions stored in
     * the binList arrayList. The offsetMap that determined the specific instruction
     * index is used to calculate the numerical offset and is added to the branch instruction
     * binary.
     *
     * @return The binary code string converted from the original assembly source
     */
    private String listToBinString() {
        StringBuilder binary = new StringBuilder();

        for (int i = 0; i < binList.size(); i++) {
            if (binList.get(i).contains("!")) {
                StringBuilder offsetBuild = new StringBuilder();
                int offset = i + 1;

                String[] binArr = binList.get(i).split("!");

                if (offsetMap.get(binArr[1]) != null)
                    offset = offsetMap.get(binArr[1]);

                String binVal = Integer.toBinaryString(offset);
                offsetBuild.append(binArr[0]);
                offsetBuild.append("0".repeat(Math.max(0, 16 - binVal.length())));
                offsetBuild.append(binVal);

                for (int j = 0; j < 3; j++) {
                    binary.append(offsetBuild.substring(8 * j, 8 * j + 8));
                    binary.append(" ");
                }

                i += 2;
            } else {
                binary.append(binList.get(i));
                binary.append(" ");
            }
        }

        return binary.toString();
    }
}

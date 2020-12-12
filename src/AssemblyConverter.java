import java.util.*;
import java.util.stream.IntStream;

/**
 * Converts assembly code from the the given input into hexadecimal code.
 * @author Robert Max Davis
 */
public class AssemblyConverter {
    private static HashMap<String, Integer> offsetMap;
    private static ArrayList<ArrayList<String>> arrList;
    private static ArrayList<String> hexList;

    /**
     * Initializes the private variables
     */
    public AssemblyConverter() {
        offsetMap = new HashMap<>();
        arrList = new ArrayList<>();
        hexList = new ArrayList<>();
    }

    /**
     * Performs the conversion operation on the assemblyCode parameter to hexadecimal code.
     *
     * @param assemblyCode A string of assembly code
     * @return A string of hexadecimal code
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

        //Generates the hex code for each instruction which are added to the hexList
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

            hexList.add(" ");
        }

        //Tabulates the offset tags and records them in a map
        int spaces = 0;
        for (int i = 0; i < hexList.size(); i++) {
            if (hexList.get(i).equals(" ")) {
                spaces++;
            }
            else if (hexList.get(i).contains(":")) {
                String[] hexArr = hexList.get(i).split(":");
                offsetMap.put(hexArr[0], i - spaces);
                hexList.set(i, hexArr[1]);
            }
        }

        return listToHexString();
    }

    /**
     * Calculates the hexadecimal code for operations that do not need input from
     * immediate or direct.
     *
     * @param opcode The instruction intended to be performed
     * @param tag The offset tag for branch instructions
     */
    private void addOpCode(String opcode, String tag) {
        boolean exists = true;
        String hex = "";
        
        if (tag != null) {
            hex = tag;
        }

        switch (opcode) {
            case "STOP":
                hex += "00";
                break;
            case "NOTR":
                hex += "18";
                break;
            case "NEGR":
                hex += "1A";
                break;
            case "ASLR":
                hex += "1C";
                break;
            case "ASRR":
                hex += "1E";
                break;
            case "ROLR":
                hex += "20";
                break;
            case "RORR":
                hex += "22";
                break;
            default:
                exists = false;
        }

        if (exists) {
            hexList.add(hex);
        }
    }

    /**
     * Calculates the hexadecimal code for branch operations.
     *
     * @param opcode The instruction intended to be performed
     * @param offset The tag to which the branch instruction should offset
     * @param tag The offset tag for branch instructions
     */
    private void addOpCode(String opcode, String offset, String tag) {
        boolean exists = true;
        String brHexCode = "";

        if (tag != null) {
            brHexCode = tag;
        }

        switch (opcode) {
            case "BR":
            case "BRC":
                brHexCode += "04";
                break;
            case "BRLE":
                brHexCode += "06";
                break;
            case "BRLT":
                brHexCode += "08";
                break;
            case "BREQ":
                brHexCode += "0A";
                break;
            case "BRNE":
                brHexCode += "0C";
                break;
            case "BRGE":
                brHexCode += "0E";
                break;
            case "BRN":
            case "BRZ":
                break;
            case "BRV":
                brHexCode += "02";
                break;
            default:
                exists = false;
        }

        if (exists) {
            String hexBuild = brHexCode + "!" + offset;
            hexList.add(hexBuild);
        }

        IntStream.range(0, 2).forEachOrdered(i -> hexList.add("00"));
    }

    /**
     * Calculates the hexadecimal code for operations that require an
     * immediate or direct addressing mode.
     *
     * @param opcode The instruction intended to be performed
     * @param value The value for either the immediate or the direct
     * @param mode The addressing mode
     * @param tag The offset tag for branch instructions
     */
    private void addOpcode(String opcode, String value, String mode, String tag) {
        boolean exists = true;
        int addMode;
        String hexTag = "";

        if (tag != null) {
            hexTag = tag;
        }

        if (mode.equals("I")) {
            addMode = 0;
        } else if (mode.equals("D")) {
            addMode = 1;
        } else {
            addMode = 3;
        }

        switch (opcode) {
            case "LDR":
                addMode += 192;
                break;
            case "STR":
                addMode += 224;
                break;
            case "ADDR":
                addMode += 112;
                break;
            case "SUBR":
                addMode += 128;
                break;
            case "ANDR":
                addMode += 144;
                break;
            case "ORR":
                addMode += 160;
                break;
            case "CHARO":
                addMode += 80;
                break;
            case "CHARI":
                addMode += 72;
                break;
            default:
                exists = false;
        }

        if (exists) {
            hexList.add(hexTag + Integer.toHexString(addMode));

            StringBuilder hexBuild = new StringBuilder();
            int valDec = Integer.parseInt(value);
            String hexVal = Integer.toHexString(valDec);
            hexBuild.append("0".repeat(Math.max(0, 4 - hexVal.length())));
            hexBuild.append(hexVal);

            for (int i = 0; i < 4; i += 2) {
                hexList.add(hexBuild.substring(i, i + 2).toUpperCase());
            }
        }
    }

    /**
     * Generates the hexadecimal string associated with the instructions stored in
     * the hexList arrayList. The offsetMap that determined the specific instruction
     * index is used to calculate the numerical offset and is added to the branch instruction
     * hexcode.
     *
     * @return The hexadecimal code string converted from the original assembly source
     */
    private String listToHexString() {
        StringBuilder hexCode = new StringBuilder();
        int spaces = 0;

        for (int i = 0; i < hexList.size(); i++) {
            if (hexList.get(i).contains("!")) {
                String[] hexArr = hexList.get(i).split("!");

                StringBuilder offsetBuild = new StringBuilder();
                int offset = i + 3 - spaces;

                for (String s : offsetMap.keySet()) {
                    if (hexArr[1].equals(s)) {
                        offset = offsetMap.get(s);
                        break;
                    }
                }

                String hexVal = Integer.toHexString(offset);
                offsetBuild.append(hexArr[0]);
                offsetBuild.append("0".repeat(Math.max(0, 4 - hexVal.length())));
                offsetBuild.append(hexVal);
                hexCode.append(offsetBuild.toString());
                i += 2;
            } else {
                if (hexList.get(i).equals(" ")) {
                    spaces++;
                }
                hexCode.append(hexList.get(i));
            }
        }

        return hexCode.toString();
    }
}

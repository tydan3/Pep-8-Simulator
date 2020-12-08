import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

/**
 * This is the main driver program with GUI. It starts the program
 * and builds the user interface.
 * @author Eric Anderson
 * @author Daniel Ty
 */
public class MainFrame extends JFrame implements ActionListener {
    /**
     * serial version uid.
     */
    private static final long serialVersionUID = 1L;
    private Computer myComp;

    /**
     * constructor.
     */
    public MainFrame() {
        super("Pep/8 Simulator Simulator");
        myComp = new Computer();
    }
    /**
     * set-up.
     */
    public void start() {

        setSize(450, 800);

        // "Input label"
        final JLabel assemblyCodeLabel = new JLabel("Input");
        assemblyCodeLabel.setBounds(20, 50, 100, 30);
        assemblyCodeLabel.setText("Instruction (bin):");
        assemblyCodeLabel.setForeground(Color.BLUE);
        add(assemblyCodeLabel);

        // "Output Label"
        final JLabel outputLabel = new JLabel("Output");
        outputLabel.setBounds(220, 50, 100, 30);
        outputLabel.setText("Output:");
        outputLabel.setForeground(Color.BLUE);
        add(outputLabel);

        // Input Text Area
        final JTextArea inputArea = new JTextArea("");
        inputArea.setBounds(15, 85, 200, 600);
        add(inputArea);

        // Input Text Area
        final JTextArea outputArea = new JTextArea("");
        outputArea.setBounds(220, 85, 200, 600);
        add(outputArea);

        // Button1
        final JButton runButton = new JButton("Run");
        runButton.setBounds(20, 20, 95, 30);
        add(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	myComp = new Computer(); //reset computer
            	// Scanner of input text
            	Scanner input = new Scanner(inputArea.getText());
            	while(input.hasNextLine()) {
            		String line = input.nextLine();
            		String code = line.replaceAll(" ", ""); //remove spaces between bits
            		
            		// Convert code lines into 8-bit bitstrings and load into computer.
            		// Handles code lines that contain 24, 16, or 8 bits.
            		if (code.length() == 24) {          
            			BitString first = new BitString();
            			first.setBits(code.substring(0, 8).toCharArray());
            			myComp.loadWord(first);
            			
            			BitString second = new BitString();
            			second.setBits(code.substring(8, 16).toCharArray());
            			myComp.loadWord(second);
            			
            			BitString third = new BitString();
            			third.setBits(code.substring(16, 24).toCharArray());
            			myComp.loadWord(third);
            			
            		} else if (code.length() == 16) {          
            			BitString first = new BitString();
            			first.setBits(code.substring(0, 8).toCharArray());
            			myComp.loadWord(first);
            			
            			BitString second = new BitString();
            			second.setBits(code.substring(8, 16).toCharArray());
            			myComp.loadWord(second);
            			
            		} else if (code.length() == 8) {
            			BitString bits = new BitString();
            			bits.setBits(code.toCharArray());
            			myComp.loadWord(bits);
            		}
            	}
            	input.close();
            	myComp.execute();            	
                outputArea.setText(myComp.getOutput());
            }
        });

        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainFrame frame = new MainFrame();
                frame.start();

            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private AssemblyConverter myConverter;

    /**
     * constructor.
     */
    public MainFrame() {
        super("Pep/8 Simulator");
        myComp = new Computer();
    }
    /**
     * set-up.
     */
    public void start() {

        setSize(825, 695);

        // "Input label"
        final JLabel sourceCodeLabel = new JLabel("Source");
        sourceCodeLabel.setBounds(20, 50, 100, 30);
        sourceCodeLabel.setText("Source Code:");
        sourceCodeLabel.setForeground(Color.BLUE);
        add(sourceCodeLabel);

        final JLabel memLabel = new JLabel("Memory");
        memLabel.setBounds(640, 50, 100, 30);
        memLabel.setText("Memory (bin):");
        memLabel.setForeground(Color.BLUE);
        add(memLabel);
        
        // "Output Label"
        final JLabel outputLabel = new JLabel("Output");
        outputLabel.setBounds(20, 390, 100, 30);
        outputLabel.setText("Output:");
        outputLabel.setForeground(Color.BLUE);
        add(outputLabel);
        
        final JLabel accumLabel = new JLabel("Accumulator");
        accumLabel.setBounds(400, 390, 100, 30);
        accumLabel.setText("Accumulator:");
        accumLabel.setForeground(Color.BLUE);
        add(accumLabel);
        
        // Source Text Area (TEXT)
        final JTextArea sourceArea = new JTextArea("");
        sourceArea.setBounds(15, 85, 600, 300);
        add(sourceArea);
        
        final JTextArea memArea = new JTextArea("");
        memArea.setBounds(640, 85, 150, 550);
        add(memArea);

        // Accumulator Text Area
        final JTextArea accumArea = new JTextArea("");
        accumArea.setBounds(400, 425, 215, 100);
        add(accumArea);

        // Output Text Area
        final JTextArea outputArea = new JTextArea("");
        outputArea.setBounds(20, 425, 355, 210);
        add(outputArea);

        // Button1
        final JButton runButton = new JButton("Run");
        runButton.setBounds(20, 20, 95, 30);
        add(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	myComp = new Computer();
            	myConverter = new AssemblyConverter();
            	
            	// get source input, convert to bin, load into memory, execute
            	String input = sourceArea.getText();
            	String bin = myConverter.generateHexString(input);
            	String [] binArray = bin.split(" ");
            	for (String bits: binArray) {
            		myComp.loadWord(bits);
            	}
            	myComp.execute();
            	
            	// set accumulator, output, and memory text areas
                accumArea.setText("" + myComp.getRegister().getValue2sComp());
                outputArea.setText(myComp.getOutput());
            	StringBuilder memory = new StringBuilder();
                int address = 0;
            	for (BitString s: myComp.getMemory()) {
            		memory.append(address + ")\t" + s + "\n");
            		address++;
            	}
            	memArea.setText(memory.toString());
                
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

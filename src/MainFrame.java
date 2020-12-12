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
        assemblyCodeLabel.setText("Instruction: (text)");
        assemblyCodeLabel.setForeground(Color.BLUE);
        add(assemblyCodeLabel);

        // "Output Label"
        final JLabel outputLabel = new JLabel("Output");
        outputLabel.setBounds(220, 50, 100, 30);
        outputLabel.setText("Output:");
        outputLabel.setForeground(Color.BLUE);
        add(outputLabel);
        
        // Input Text Area (TEXT)
        final JTextArea inputTextArea = new JTextArea("");
        inputTextArea.setBounds(15, 85, 200, 600);
        add(inputTextArea);

        // Input Text Area (BINARY)
        final JTextArea inputArea = new JTextArea("");
        inputArea.setBounds(15, 690, 405, 20);
        add(inputArea);

        // Output Text Area
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
            		myComp.loadWord(line);
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

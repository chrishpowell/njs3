/*
 * Send some birth data to BirthChart
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.Util;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.Border;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BirthDataInput
{
    private final static JFrame     jf = new JFrame("Birth Data");
    private static JLabel           jlName, jlBirth, jlDate, jlTime, jlPlace;
    private static JTextField       tfName, tfDate, tfTime, tfPlace;

    public static String getHoroscope( String name, String place, LocalDate ld, LocalTime lt )
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("####.######");
        
        // Create user
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        BirthChart bc = new BirthChart(name, ldt, place).init();
        
        // Create the horoscope wheel for this user
        bc.createWheel();
        HoroHouse hh = bc.getWheel();
        
        // Set birthplace
        bc.setPlace( hh.getLatLon() );
        
        // Personal data
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " +bc.getShortName()+ "\r\n");
        sb.append("  Datetime: " +bc.getBirthDate().toString()+ "\r\n");
        sb.append("  Birth place: " +bc.getBirthPlaceName()+ "\r\n");
        sb.append("  Lat/Lon: " +bc.getBirthPlace().fmtLatLonDMS()+ "\r\n");
        sb.append("\r\nHouses (Placidus)      Declination\r\n");
            
        // Dump wheel data
        hh.getCpaMap().forEach((k,v)->{
            double deg = Math.toDegrees(v.getAngle());
            sb.append( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"   "+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true)+ "\r\n" );
        });
        
        return sb.toString();
    }
    
    public static void main(String[] args)
    {
        // Initialise
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(300,300);
        
        // Layout
        Container pane = jf.getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        /*
         * Components
         */
        // Labels
        jlName = new JLabel(" Name: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.ipady = 12;
        pane.add(jlName,gbc);
        
        jlBirth = new JLabel(" Birth ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.ipady = 20;
        pane.add(jlBirth,gbc);
        
        jlDate = new JLabel(" Date (yyyy-mm-dd): ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.ipady = 20;
        pane.add(jlDate,gbc);
        
        jlTime = new JLabel(" Time (24h): ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.ipady = 20;
        pane.add(jlTime,gbc);
        
        jlPlace = new JLabel(" Place: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.ipady = 20;
        pane.add(jlPlace,gbc);
        
        // Text input
        // Name
        tfName = new JTextField(" eg: Micky Mouse",30);
        tfName.setEditable(true);
        tfName.setBackground(Color.LIGHT_GRAY);

        Border border = BorderFactory.createLineBorder(Color.darkGray, 2);
        tfName.setBorder(border);
        
        tfName.requestFocusInWindow();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.ipady = 8;
        gbc.insets = new Insets(5,0,0,5);
        gbc.gridwidth = 2;
        pane.add(tfName,gbc);
        
        // Date
        tfDate = new JTextField(" eg: 1990-03-27",30);
        tfDate.setEditable(true);
        tfDate.setBackground(Color.LIGHT_GRAY);
        tfDate.setBorder(border);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.ipady = 8;
        gbc.insets = new Insets(5,0,0,5);
        pane.add(tfDate,gbc);
        
        // Time
        tfTime = new JTextField(" eg: 13:05",30);
        tfTime.setEditable(true);
        tfTime.setBackground(Color.LIGHT_GRAY);
        tfTime.setBorder(border);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 3;
        gbc.ipady = 8;
        gbc.insets = new Insets(5,0,0,5);
        pane.add(tfTime,gbc);
        
        // Place
        tfPlace = new JTextField(" eg: Berlin, Germany",30);
        tfPlace.setEditable(true);
        tfPlace.setBackground(Color.LIGHT_GRAY);
        tfPlace.setBorder(border);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.ipady = 8;
        gbc.insets = new Insets(5,0,0,5);
        pane.add(tfPlace,gbc);

        // Send request
        JButton bSend = new JButton("Send");
        bSend.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.ipady = 0;
        gbc.insets = new Insets(25,8,5,8);
        pane.add(bSend,gbc);
        
        // Trigger get horoscope
        bSend.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                String horoscope = "No horoscope";
                try
                {
                    LocalDate ld = LocalDate.parse(tfDate.getText().trim());
                    LocalTime lt = LocalTime.parse(tfTime.getText().trim());
                    horoscope = getHoroscope(tfName.getText(),tfPlace.getText(),ld,lt);
                }
                catch( Exception ex )
                {
                    JOptionPane.showMessageDialog(jf,ex.getMessage(),"Horoscope error",JOptionPane.WARNING_MESSAGE);
                }

                UIManager.put("OptionPane.messageFont", new Font("Courier", Font.PLAIN, 12));
                JOptionPane.showMessageDialog(jf,horoscope,"Horoscope",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Display, has to have 'pack' to work.
        jf.pack();
        jf.setVisible(true);
    }
}

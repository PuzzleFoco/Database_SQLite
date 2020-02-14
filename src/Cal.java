import com.sun.istack.internal.localization.NullLocalizable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cal extends JPanel {
    protected static int yy;
    protected static int mm;
    protected static int dd;
    //static int yea, mon, day;

    protected JButton labs[][];
    protected int leadGap = 0;

    Calendar calendar = new GregorianCalendar();
    protected final int thisYear = calendar.get(Calendar.YEAR);
    protected final int thisMonth = calendar.get(Calendar.MONTH);

    private JButton b0;

    private JComboBox monthChoice;

    private JComboBox yearChoice;

/*    public void setYea (int newyea){
        this.yea = newyea; }

    public static int getYea (){return yea;}*/

    Cal() {
        super();
        setYYMMDD(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        buildGUI();
        recompute();
    }

    Cal(int year, int month, int today) {
        super();
        setYYMMDD(year, month, today);
        buildGUI();
        recompute();
    }

    private void setYYMMDD(int year, int month, int today) {
        yy = year;
        mm = month;
        dd = today;
    }



    String[] months = { "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" };

    private void buildGUI() {
        getAccessibleContext().setAccessibleDescription(
                "Calendar not accessible yet. Sorry!");
        setBorder(BorderFactory.createEtchedBorder());

        setLayout(new BorderLayout());

        JPanel tp = new JPanel();
        tp.add(monthChoice = new JComboBox());
        for (int i = 0; i < months.length; i++)
            monthChoice.addItem(months[i]);
        monthChoice.setSelectedItem(months[mm]);
        monthChoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = monthChoice.getSelectedIndex();
                if (i >= 0) {
                    mm = i;
                    // System.out.println("Month=" + mm);
                    //mon = i;
                    recompute();
                }
            }
        });
        monthChoice.getAccessibleContext().setAccessibleName("Months");
        monthChoice.getAccessibleContext().setAccessibleDescription(
                "Choose a month of the year");

        tp.add(yearChoice = new JComboBox());
        yearChoice.setEditable(true);
        for (int i = yy - 5; i < yy + 5; i++)
            yearChoice.addItem(Integer.toString(i));
        yearChoice.setSelectedItem(Integer.toString(yy));
        yearChoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = yearChoice.getSelectedIndex();
                System.out.println(yy);
                if (i >= 0) {
                    yy = Integer.parseInt(yearChoice.getSelectedItem()
                            .toString());
                    System.out.println("Year=" + yy);
                    //setYea(yy);
                    recompute();
                }
            }
        });
        add(BorderLayout.CENTER, tp);

        JPanel bp = new JPanel();
        bp.setLayout(new GridLayout(7, 7));
        labs = new JButton[6][7]; // first row is days

        bp.add(b0 = new JButton("Mo"));
        bp.add(new JButton("Di"));
        bp.add(new JButton("Mi"));
        bp.add(new JButton("Do"));
        bp.add(new JButton("Fr"));
        bp.add(new JButton("Sa"));
        bp.add(new JButton("So"));

        ActionListener dateSetter = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String num = e.getActionCommand();
                if (!num.equals("")) {
                    // set the current day highlighted
                    setDayActive(Integer.parseInt(num));
                    //day = Integer.parseInt(num);
                }
            }
        };

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++) {
                bp.add(labs[i][j] = new JButton(""));
                labs[i][j].addActionListener(dateSetter);
            }

        add(BorderLayout.SOUTH, bp);
    }

    public final static int dom[] = { 31, 28, 31, 30, /* jan feb mar apr */
            31, 30, 31, 31, /* may jun jul aug */
            30, 31, 30, 31 /* sep oct nov dec */
    };

    /** Compute which days to put where, in the Cal panel */
    protected void recompute() {
        // System.out.println("Cal::recompute: " + yy + ":" + mm + ":" + dd);
        if (mm < 0 || mm > 11)
            throw new IllegalArgumentException("Month " + mm
                    + " bad, must be 0-11");
        clearDayActive();
        calendar = new GregorianCalendar(yy, mm, dd);
        leadGap = new GregorianCalendar(yy, mm, 0).get(Calendar.DAY_OF_WEEK) -1;
        int daysInMonth = dom[mm];
        if (isLeap(calendar.get(Calendar.YEAR)) && mm == 1)
            ++daysInMonth;

        for (int i = 0; i < (leadGap-1); i++) {
            labs[0][i].setText("");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            JButton b = labs[(leadGap + i -1) / 7][(leadGap + i - 1) % 7];
            b.setText(Integer.toString(i));
        }

        for (int i = leadGap +1 + daysInMonth; i < 6 * 7; i++) {
            labs[(i) / 7][(i) % 7].setText("");
        }

        if (thisYear == yy && mm == thisMonth)
            setDayActive(dd); // shade the box for today

        repaint();
    }

    public boolean isLeap(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            return true;
        return false;
    }

    /** Set the year, month, and day */
    public void setDate(int yy, int mm, int dd) {
        // System.out.println("Cal::setDate");
        this.yy = yy;
        this.mm = mm; // starts at 0, like Date
        this.dd = dd;
        recompute();
    }

    /** Unset any previously highlighted day */
    private void clearDayActive() {
        JButton b;

        // First un-shade the previously-selected square, if any
        if (activeDay > 0) {
            b = labs[(leadGap + activeDay - 1) / 7][(leadGap + activeDay - 1) % 7];
            b.setBackground(b0.getBackground());
            b.repaint();
            activeDay = -1;
        }
    }

    private int activeDay = -1;

    /** Set just the day, on the current month */
    public void setDayActive(int newDay) {

        clearDayActive();

        // Set the new one
        if (newDay <= 0)
            dd = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
        else
            dd = newDay;
        // Now shade the correct square
        Component square = labs[(leadGap + newDay - 1) / 7][(leadGap + newDay - 1) % 7];
        square.setBackground(Color.yellow);
        square.repaint();
        activeDay = newDay;
    }

    public static void main(String[] av) {
        JFrame f = new JFrame("Kalender");
        Container c = f.getContentPane();
        c.setLayout(new FlowLayout());
        JButton buttonok = new JButton("OK");

        c.add(new Cal());
        c.add(buttonok);

        f.pack();
        f.setVisible(true);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final LocalDate[] date = new LocalDate[1];

        buttonok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                String dateInString;

                if (mm < 9) {
                    if (dd > 9) dateInString = (yy + "-0" + (mm+1) + "-" + dd);
                    else dateInString = (yy + "-0" + (mm+1) + "-0" + dd);
                }
                else if (dd<10) dateInString = (yy + "-" + (mm+1) + "-0" + dd);
                else dateInString = (yy + "-" + (mm+1) + "-" + dd);

                date[0] = LocalDate.parse(dateInString);
                System.out.println(date[0]);
            }
        });
    }
}

package ui.gui;

import javax.swing.*;
import java.util.List;

// runs popup windows
public class PopupWindow {

    // EFFECTS: makes popup to ask for an int
    public static int getInt(String text) {
        String s;
        int x;
        while (true) {
            try {
                s = JOptionPane.showInputDialog(null, text);
                x = Integer.parseInt(s);
                break;
            } catch (Exception e) {
                // try again
            }
        }
        return x;
    }

    // EFFECTS: makes popup to ask for an int of min or higher
    public static int getInt(String text, int min) {
        while (true) {
            int x = getInt(text + " [" + min + ", )");
            if (min <= x) {
                return x;
            }
        }
    }

    // EFFECTS: makes popup to ask for an int of min or higher and max or lower
    public static int getInt(String text, int min, int max) {
        while (true) {
            int x = getInt(text + "[" + min + ", " + max + "]");
            if (min <= x && x <= max) {
                return x;
            }
        }
    }

    // EFFECTS: makes popup to ask for a double
    public static double getDouble(String text) {
        String s;
        double x;
        while (true) {
            try {
                s = JOptionPane.showInputDialog(null, text);
                x = Double.parseDouble(s);
                break;
            } catch (Exception e) {
                // try again
            }
        }
        return x;
    }

    // EFFECTS: makes popup to ask for a double of min or higher
    public static double getDouble(String text, int min) {
        while (true) {
            double x = getDouble(text + " [" + min + ", )");
            if (min <= x) {
                return x;
            }
        }
    }

    // EFFECTS: makes popup to ask for a string
    public static String getString(String text) {
        String s = null;
        while (s == null) {
            try {
                s = JOptionPane.showInputDialog(null, text);
            } catch (Exception e) {
                // try again
            }
        }
        return (s);
    }

    // REQUIRES: there is at least one option
    // EFFECTS: makes popup to ask for a string from a selection
    public static String getString(String text, List<String> options) {
        String s;
        String choices = options.get(0);
        for (int k = 1; k < options.size(); k++) {
            choices += ", " + options.get(k);
        }
        while (true) {
            s = getString(text + " (" + choices + ")");
            for (String option : options) {
                if (s.equals(option)) {
                    return (s);
                }
            }
            PopupWindow.showMessage("please enter one of: " + choices);
        }
    }

    // EFFECTS: makes popup to ask for a string
    public static String getNonEmptyString(String text) {
        String s = "";
        while (s.equals("")) {
            s = getString(text);
        }
        return (s);
    }

    // EFFECTS: makes popup ask for yes or no and return as boolean
    public static Boolean getYesNo(String text) {
        int reply;
        while (true) {
            try {
                reply = JOptionPane.showConfirmDialog(null, text, "Yes or No", JOptionPane.YES_NO_OPTION);
                break;
            } catch (Exception e) {
                // try again
            }
        }
        return (reply == JOptionPane.YES_OPTION);
    }

    // EFFECTS: makes popup to display a message
    public static void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

}

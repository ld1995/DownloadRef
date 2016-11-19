package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintTextField extends JTextField
{
    private String hint;
    private boolean hintOnFocus = true;
    private int hintAlignment = JTextField.LEFT;

    public HintTextField(String text, String hint, boolean hintOnFocus)
    {
        super();

        this.hint = hint;
        this.hintOnFocus = hintOnFocus;
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                HintTextField.this.focusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                HintTextField.this.focusLost();
            }
        });

        setText(text);
    }

    public HintTextField(String text, String hint)
    {
        this(text,hint,true);
    }

    public HintTextField(String hint)
    {
        this("",hint);
    }

    public HintTextField()
    {
        this("...");
    }

    protected void focusGained()
    {
        if (hintOnFocus)
        {
            repaint();
        }
    }

    protected void focusLost()
    {
        if (hintOnFocus)
        {
            repaint();
        }
    }

    protected boolean isEmpti(String text)
    {
        return text == null || text.trim().isEmpty();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isEmpti(getText()) && !(isFocusOwner() && hintOnFocus)) {
            Insets insets = getInsets();
            Insets margins = getMargin();
            int left = insets.left + margins.left;
            int right = insets.right + margins.right;
            int maxWidth = getWidth() - (left + right);
            FontMetrics fontMetrics = g.getFontMetrics();
            String hint = this.hint;
            while(fontMetrics.stringWidth(hint)>maxWidth) {
                int len = hint.length() - 4;
                if (len < 0)
                    break;
                else
                    hint = hint.substring(0,left) + "...";
            }
            int x = left;
            if (hintAlignment == JTextField.RIGHT) {
                x += maxWidth - fontMetrics.stringWidth(hint);
            } else if (hintAlignment == JTextField.CENTER) {
                x += (maxWidth - fontMetrics.stringWidth(hint)) / 2;
            }
            int y = getBaseline(getWidth(),getHeight());
            g.drawString(hint,x,y);
        }
    }

    public static HintTextField printHint(String text)
    {
        HintTextField hintTextField = new HintTextField("",text,false);
        return hintTextField;
    }
}


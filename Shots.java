import vsms.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/** Renders every screen to PNG using demo data (works headless). Usage: java Shots <outDir> */
public class Shots {
    static void layoutAll(Component c) {
        if (c instanceof javax.swing.table.JTableHeader) {
            javax.swing.table.JTableHeader h = (javax.swing.table.JTableHeader) c;
            Container vp = h.getParent();
            int w = vp != null && vp.getParent() instanceof JScrollPane
                ? ((JScrollPane) vp.getParent()).getViewport().getWidth() : h.getPreferredSize().width;
            h.setSize(w, 28);
        }
        if (c instanceof Container) {
            ((Container) c).doLayout();
            for (Component k : ((Container) c).getComponents()) layoutAll(k);
        }
    }

    static void save(JComponent c, int w, int h, File f) throws Exception {
        c.setSize(w, h);
        layoutAll(c);
        layoutAll(c);
        layoutAll(c);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        c.paint(g);
        g.dispose();
        ImageIO.write(img, "png", f);
    }

    public static void main(String[] a) throws Exception {
        System.setProperty("java.awt.headless", "true");
        File out = new File(a.length > 0 ? a[0] : "screenshots");
        out.mkdirs();
        Ui.install();
        Repo repo = new MemoryRepo();
        SwingUtilities.invokeAndWait(() -> {
            try {
                LoginPanel lp = new LoginPanel(repo, () -> { });
                lp.setBorder(BorderFactory.createEmptyBorder());
                save(lp, 420, 400, new File(out, "01_login.png"));
                MainPanel mp = new MainPanel(repo, () -> { });
                int i = 2;
                for (String s : mp.screenNames()) {
                    mp.show(s);
                    String file = String.format("%02d_%s.png", i++, s.toLowerCase().replace(' ', '_'));
                    save(mp, 1180, 700, new File(out, file));
                }
            } catch (Exception e) { throw new RuntimeException(e); }
        });
        System.exit(0);
    }
}

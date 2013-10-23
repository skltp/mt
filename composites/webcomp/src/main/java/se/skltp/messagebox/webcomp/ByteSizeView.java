package se.skltp.messagebox.webcomp;

/**
 * Add a view to display a size in bytes in a human way.
 *
 * Counts in whole b or one-digit resolution kb/mb/gb
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ByteSizeView {

    long sizeInBytes;

    public ByteSizeView(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    @Override
    public String toString() {
        double gb = sizeInBytes / (1024*1024*1024.0);
        if (gb >= 1) {
            return String.format("%1$.1fgb", gb);
        }
        double mb = sizeInBytes / (1024*1024.0);
        if (mb >= 1) {
            return String.format("%1$.1fmb", mb);
        }
        double kb = sizeInBytes / 1024.0;
        if (kb >= 1) {
            return String.format("%1$.1fkb", kb);
        }
        return sizeInBytes + "b";
    }
}

package pl.xsolve.props2xls.tools;

/**
 * http://stackoverflow.com/questions/1001290/console-based-progress-in-java
 *
 * @author laalto from stackoverflow :-)
 */
public class ProgressBar {

    public static void updateProgress(int current, int max) {
        updateProgress((double) current / (double) max);
        System.out.print(" " + current + "/" + max);
    }

    static void updateProgress(double progressPercentage) {
        final int width = 50; // progress bar width in chars

        System.out.print("\r[");
        int i = 0;
        for (; i <= (int) (progressPercentage * width); i++) {
            System.out.print(".");
        }
        for (; i < width; i++) {
            System.out.print(" ");
        }
        System.out.print("]");
    }

    public static void main(String[] args) {
        try {
            for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
//                updateProgress(progressPercentage);
                updateProgress((int) (progressPercentage * 100), 100);
                Thread.sleep(40);
            }
        } catch (InterruptedException ignored) {
            //ignored
        }
    }

}

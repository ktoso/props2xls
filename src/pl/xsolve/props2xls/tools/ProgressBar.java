/*
 * This file is part of props2xls.
 *
 * props2xls is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * props2xls is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with props2xls.  If not, see <http://www.gnu.org/licenses/>.
 */

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

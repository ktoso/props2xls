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

package pl.xsolve.props2xls.tools

/**
 * @author Konrad Ktoso Malawski
 */
class ElapsedTime {

  long elapsedTimeInMilis

  short seconds
  short minutes
  long hours

  def ElapsedTime(long t0) {
    elapsedTimeInMilis = System.currentTimeMillis() - t0;
    convert();
  }

  def convert() {
    long time = elapsedTimeInMilis / 1000;
    seconds = (int) (time % 60)
    minutes = (int) ((time % 3600) / 60)
    hours = (int) (time / 3600)
  }

  def String toString() {
    "${hours}h ${minutes}m ${seconds}s"
  }
}

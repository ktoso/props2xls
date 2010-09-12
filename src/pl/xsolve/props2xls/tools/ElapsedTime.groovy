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

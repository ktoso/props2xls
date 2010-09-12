package pl.xsolve.props2xls.tools

/**
 * @author Konrad Ktoso Malawski
 */
public class Items {

  List<Item> list = []

  def current = null

  public prepare(String filename, String locale) {
//    def file = filename
    def nameAndLocale = filename.split("_", 2)
    def file = nameAndLocale[0]
//    def locale = nameAndLocale[1][0..nameAndLocale[1].indexOf('.') - 1]

    current = list.find { it ->
      it.filename == file //&& it.allProps.keySet().contains(locale)
    }

    if (current == null) {
      current = new Item(file, locale)
      list += current
    }
  }

  public add(lang, key, value) {
    current.add(lang, key, value)
  }

  public stats() {
    "${list.size()}"
  }

  public Set langs() {
    Set langs = new HashSet()
    list.each { it ->
      it.allProps.keySet().each {lang ->
        def p = lang.split("_")
        Locale locale = new Locale(p[0], p[1])
        langs.add("${locale} (${locale.getDisplayLanguage()})")
      }
    }

    langs
  }
}

protected static class Item {
  String filename
  //lang, [key, value]
  Map<String, Map> allProps = new HashMap()

  def Item(filename, locale) {
    this.filename = filename;
    allProps.put(locale, new HashMap())
  }

  def add(String locale, String key, String value) {
    if (!allProps.containsKey(locale)) {
      allProps.put(locale, new HashMap())

    }

    allProps.get(locale).put(key, value)
  }

  def getInAllLangs(String key) {
    def all = []
    allProps.entrySet().each { entry ->
      all += entry.getValue().get(key)
    }

    all
  }
}

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

  public List langs() {
    List langs = []

    list.each { it ->
      it.allProps.keySet().each {lang ->
        Locale locale
        if (lang.contains("_")) {
          def p = lang.split("_")
          locale = new Locale(p[0], p[1])
        } else {
          locale = new Locale(lang)
        }
        def string = "${locale} (${locale.getDisplayLanguage()})"
        if (!langs.contains(string)) {
          langs.add(string)
        }
      }

      langs.reverse()
    }
  }

}

protected static class Item {
    String filename
    //[lang, [key, value]]
    Map<String, Map<String, String>> allProps = new HashMap()

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
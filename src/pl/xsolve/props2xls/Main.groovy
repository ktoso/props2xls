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

package pl.xsolve.props2xls

import pl.xsolve.props2xls.tools.gdata.ImportClient
import pl.xsolve.props2xls.tools.SimpleCommandLineParser
import pl.xsolve.props2xls.tools.Items
import pl.xsolve.props2xls.tools.ElapsedTime

/**
 * This Groovy app will take all *[LOCALE].properties files (recursively) from the specified directory,
 * and write them to Google docs!
 *
 * This app is particularly useful for GWT developers who have clients that want to see the i18n strings you'll be using,
 * or want to translate them themselves and somehow fear *.properties files. It's also super easy to contribute to such an
 * doc once it's been uploaded.
 *
 * If you'd like me to write an analogical docs -> properties import script - mail me! :-)
 *
 * Please note that you will be prompted for an password and login if you don't specify any during app invocation.
 *
 * @author Konrad Ktoso Malawski <konrad.malawski@java.pl>
 */
public class Main {

  static long t0 = System.currentTimeMillis()

  static boolean verbose

  static StringBuilder sb = new StringBuilder()

  public static void main(String[] args) {
    def parser = new SimpleCommandLineParser(args)
    if (args.length < 1) {
      println """"
USAGE:

./props2xls.sh [OPTIONS] [FOLDER]
OR
java -jar props2xls.jar [OPTIONS] [FOLDER]

[OPTIONS] = {
  -u, -user, --username     google account username
                            default = interactive mode, the app wil ask you for this param
  -p, --pass, --password    google account password
                            default = interactive mode, the app wil ask you for this param
  -n, --perbatch            number of updates per one "batch update"
                            default = 200
  -s, --spreadsheet         spreadsheet name to import data into
                            default = props2xls
  -w, --worksheet           worksheet (in this spreadsheet) name to import data into
                            default = properties
}
"""
      System.exit(-1)
    }

    //params and setup
    verbose = parser.containsKey("verbose")
    def username = parser.getValue("username", "user", "u")
    def password = parser.getValue("password", "pass", "p")

    int itemsPerBatch = 200
    if (parser.containsKey("perbatch", "n")) {
      itemsPerBatch = Integer.valueOf(parser.getValue("perbatch", "n"))
    }

    def spreadsheet = parser.getValue("spreadsheet", "s")
    if (!spreadsheet) spreadsheet = "props2xls"

    def worksheet = parser.getValue("worksheet", "w")
    if (!worksheet) worksheet = "properties"

    def directoryName = args[args.length - 1]
    def fileSubStr = ".properties"

    def filePattern = ~/${fileSubStr}/
    def directory = new File(directoryName)

    if (!directory.isDirectory()) {
      println "The provided directory name ${directoryName} is NOT a directory."
      System.exit(-2)
    }

    Items itemz = new Items();

    println "# Searching for files including ${fileSubStr} in directory ${directoryName}..."
    def findFilenameClosure = { file ->
      if (filePattern.matcher(file.name).find()) {
        println "# \tLoading ${file.name} (size ${file.size()})..."

        def nameAndLocale = file.name.split("_", 2)
        def locale = nameAndLocale[1][0..nameAndLocale[1].indexOf('.') - 1]

        itemz.prepare(file.name, locale)

        Properties props = new Properties();
        props.load file.newReader("UTF-8")
        for (pair in props.entrySet()) {
          itemz.add(locale, pair.key, pair.value)
        }

        println "# \t\tDone. (${itemz.stats()} resource bundles loaded)"
      }
    }

    println "# Matching Files:"
    directory.eachFileRecurse(findFilenameClosure)
    print "\n\n"


    def start = "filename;;;;property;;;;"

    pront start

    itemz.langs().allProps.get(0).each { lang ->
      Locale locale
      if (lang.key.contains("_")) {
        def p = lang.key.split("_")
        locale = new Locale(p[0], p[1])
      } else {
        locale = new Locale(lang.key)
      }
      def string = "${locale} (${locale.getDisplayLanguage()});;;;"

      pront string
    }
    pront "\n"

    //each "file"
    for (item in itemz.list) {
      def localesAndValues = item.allProps.values()

      def prop = localesAndValues.asList().get(0)
//    entry.value.each { pr ->
//    def pr = entry.value[0]
      prop.keySet().each { property ->
//    def property = prop.get(anyLocale)
//    property/value at last...
//    String property = pr.key
        pront "${item.filename};;;;${property};;;;"
        item.getInAllLangs(property).each { it ->
          //it is a map of key values
          pront "${it};;;;"
        }
        pront "\n"
//  }
      }
    }

    if (!username) {
      def console = System.console()
      username = console.readLine("# Enter username: ")
    }

    if (!password) {
      def console = System.console()
      password = new String(console.readPassword("# Enter password for ${username}: "))
    }

    ImportClient.gogogo(
            username,
            password,
            itemsPerBatch,
            spreadsheet,
            worksheet,
            sb.toString()
    )

    println "\n\n# Done. \n# Execution took: ${new ElapsedTime(t0)}"
  }

  static void pront(str) {
    sb.append str
    if (verbose) {
      print str
    }
  }
}

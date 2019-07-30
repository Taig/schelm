import java.nio.charset.Charset

import com.helger.css.ECSSVersion
import com.helger.css.reader.CSSReader
import com.helger.css.writer.CSSWriter
import sbt._

case object NormalizeCssGenerator {
  val writer: CSSWriter = new CSSWriter().setHeaderText(null)

  def apply(pkg: String, source: File, target: File): Unit = {
    val code =
      s"""package $pkg
         |
         |trait NormalizeCss {
         |  val normalize: com.ayendo.schelm.css.Rule =
         |    com.ayendo.schelm.css.Rule.Raw(\"\"\"${css(source)}\"\"\".stripMargin)
         |}""".stripMargin

    IO.write(target, code)
  }

  def css(source: File): String = {
    val content = IO.read(source)
    val css = CSSReader.readFromString(
      content,
      Charset.defaultCharset(),
      ECSSVersion.CSS30
    )
    writer.getCSSAsString(css)
  }
}

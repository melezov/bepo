package com.mentatlabs.bepo
package email

import java.util.Locale

case class Attachment(
  filename: String
, mimeType: String
, content: Array[Byte]
)

object Attachment {
  def apply(filename: String, content: Array[Byte]): Attachment =
    Attachment(filename, MimeType.fromFileName(filename), content)
}

object MimeType {
  private[this] val Default = "application/octet-stream"

  private[this] lazy val ext2Mime = {
    val props = new java.util.Properties
    props.load(getClass.getResourceAsStream("mimetypes.properties"))
    val mmap = scala.collection.JavaConverters.propertiesAsScalaMapConverter(props)
    mmap.asScala.toMap withDefaultValue Default
  }

  def fromFileName(filename: String) =
    filename lastIndexOf '.' match {
      case x if x >= 0 =>
        val ext = filename substring (x + 1) toLowerCase(Locale.ENGLISH)
        ext2Mime(ext)

      case _ =>
        Default
    }
}

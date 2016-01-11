package com.mentatlabs.bepo
package email

sealed abstract class Address protected(val address: String)
case class From(override val address: String) extends Address(address)
case class To(override val address: String) extends Address(address)
case class ReplyTo(override val address: String) extends Address(address)
case class CC(override val address: String) extends Address(address)
case class BCC(override val address: String) extends Address(address)

case class Subject(title: String)

sealed abstract class EmailBody(val body: String)
case class TextBody(override val body: String) extends EmailBody(body)
case class HtmlBody(override val body: String) extends EmailBody(body)

case class Email(
  from: From
, replyTo: Option[ReplyTo]
, to: Seq[To]
, cc: Seq[CC]
, bcc: Seq[BCC]
, subject: Subject
, textBody: Option[TextBody]
, htmlBody: Option[HtmlBody]
, attachments: Seq[Attachment]
)

case class Attachment(
  filename: String
, mimeType: String
, content: Array[Byte]
)

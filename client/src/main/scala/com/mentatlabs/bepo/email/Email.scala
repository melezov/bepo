package com.mentatlabs.bepo
package email

object Email {
  def apply(from: From, to: To, subject: Subject, body: EmailBody) =
    new Email(
      from = from
    , replyTo = None
    , to = Seq(to)
    , cc = Nil
    , bcc = Nil
    , subject = subject
    , textBody = None
    , htmlBody = None
    , attachments = Nil
    ).add(body)
}

case class Email(
    from: From
  , replyTo: Option[ReplyTo]
  , to: Seq[To]
  , cc: Seq[CC]
  , bcc: Seq[BCC]
  , subject: Subject
  , textBody: Option[TextBody]
  , htmlBody: Option[HtmlBody]
  , attachments: Seq[Attachment]) {

  def add(address: Address) = address match {
    case from: From => copy(from = from)
    case replyTo: ReplyTo => copy(replyTo = Some(replyTo))
    case to: To => copy(to = this.to :+ to)
    case cc: CC => copy(cc = this.cc :+ cc)
    case bcc: BCC => copy(bcc = this.bcc :+ bcc)
  }

  def add(subject: Subject) = copy(subject = subject)

  def add(body: EmailBody) = body match {
    case textBody: TextBody => copy(textBody = Some(textBody))
    case htmlBody: HtmlBody => copy(htmlBody = Some(htmlBody))
  }

  def add(attachment: Attachment) =
    copy(attachments = this.attachments :+ attachment)
}

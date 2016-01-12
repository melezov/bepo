package com.mentatlabs.bepo.email

import com.mentatlabs.bepo.api.email.SendEmail
import org.revenj.patterns.DomainEventHandler

import scala.collection.JavaConverters._

class SendEmailEventHandler(mailer: Mailer) extends DomainEventHandler[SendEmail] {
  def handle(sendEmail: SendEmail): Unit = {

    if (sendEmail.getTextBody != null)
      throw new UnsupportedOperationException("Text body is not currently supported, use only HTML body instead!")

    val email = Email(
      from = From(sendEmail.getFrom)
    , replyTo = Option(sendEmail.getReplyTo).map(ReplyTo.apply)
    , to = sendEmail.getTo.asScala.map(To.apply)
    , cc = sendEmail.getCc.asScala.map(CC.apply)
    , bcc = sendEmail.getBcc.asScala.map(BCC.apply)
    , subject = Subject(sendEmail.getSubject)
    , textBody = Option(sendEmail.getTextBody).map(TextBody.apply)
    , htmlBody = Option(sendEmail.getHtmlBody).map(HtmlBody.apply)
    , attachments = sendEmail.getAttachments.asScala.map { attachment =>
        Attachment(
          filename = attachment.getFileName
          , mimeType = attachment.getMimeType
          , content = attachment.getContent
        )
      }
    )

    mailer.sendMail(email)
  }
}

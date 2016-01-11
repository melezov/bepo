package com.mentatlabs.bepo

import com.dslplatform.client.{Bootstrap, DomainProxy}
import org.slf4j.Logger
import scala.concurrent.{Future, ExecutionContext}

import email._

class Bepo(
    private val logger: Logger
  , private val endpoint: String
  , private implicit val executionContext: ExecutionContext) {

  val locator = {
    val components = new java.util.HashMap[Class[_], AnyRef]
    components.put(classOf[Logger], logger)

    val props = new java.util.Properties
    props.put("api-url", endpoint)
    props.put("package-name", "com.mentatlabs.bepo.api")

    Bootstrap.init(props, components)
  }

  def sendEmail(email: Email) = {
    import scala.collection.JavaConverters._

    val event = new api.email.SendEmail()
      .setFrom(email.from.toString)
      .setReplyTo(email.replyTo.map(_.toString).orNull)
      .setTo((email.to.map(_.toString).toList.asJava))
      .setCc(email.cc.map(_.toString).toList.asJava)
      .setBcc(email.bcc.map(_.toString).toList.asJava)
      .setSubject(email.subject.title)
      .setTextBody(email.textBody.map(_.body).orNull)
      .setHtmlBody(email.htmlBody.map(_.body).orNull)
      .setAttachments(email.attachments.map(att =>
        new api.email.Attachment()
          .setFileName(att.filename)
          .setMimeType(att.mimeType)
          .setContent(att.content)
      ).toList.asJava)

    Future {
      locator.resolve(classOf[DomainProxy]).submit(event).get
    }
  }
}

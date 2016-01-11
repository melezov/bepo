package com.mentatlabs.bepo.email

import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import javax.mail.{Address => MAddress, Transport, PasswordAuthentication, Message, Session}
import javax.mail.internet._
import com.typesafe.scalalogging.Logger

class Mailer(
    private val logger: Logger
  , private val credentials: Credentials) {

  private val passwordAuth = new javax.mail.Authenticator(){
    override protected def getPasswordAuthentication() = {
      new PasswordAuthentication(
        credentials.username
      , new String(credentials.password)
      )
    }
  }

  def sendMail(email: Email): Unit = {
    val properties = System.getProperties
    properties.putAll(credentials.properties)

    val session = Session.getDefaultInstance(properties, passwordAuth)
    val message = new MimeMessage(session)

    message.setFrom(email.from.address)

    for (replyTo <- email.replyTo) {
      message.setReplyTo(Array(new InternetAddress(replyTo.address)))
    }

    if (email.to.nonEmpty) {
      val to = Array.empty[MAddress] ++ email.to.map(a => new InternetAddress(a.address))
      message.setRecipients(Message.RecipientType.TO, to)
    }

    if (email.cc.nonEmpty) {
      val cc = Array.empty[MAddress] ++ email.cc.map(a => new InternetAddress(a.address))
      message.setRecipients(Message.RecipientType.CC, cc)
    }

    if (email.bcc.nonEmpty) {
      val bcc = Array.empty[MAddress] ++ email.bcc.map(a => new InternetAddress(a.address))
      message.setRecipients(Message.RecipientType.BCC, bcc)
    }

    message.setSubject(email.subject.title, "UTF-8")

    val multiPart = new MimeMultipart("related")
//    val multiPart = new MimeMultipart("alternative")

    for (textBody <- email.textBody) {
      val textBodyPart = new MimeBodyPart
      textBodyPart.setContent(textBody.body, "text/plain;charset=UTF-8")
      multiPart.addBodyPart(textBodyPart)
    }

    val (inlineImages, attachments) =
      email.attachments.partition { attachment =>
        email.htmlBody match {
          case Some(htmlBody) =>
            val marker = "cid:" + attachment.filename
            htmlBody.body.contains(marker)
          case _ =>
            false
        }
      }

    for (htmlBody <- email.htmlBody) yield {
//      val htmlMultiPart = new MimeMultipart("related")
      val htmlMultiPart = multiPart

      htmlMultiPart addBodyPart {
        val htmlBodyPart = new MimeBodyPart
        htmlBodyPart.setContent(htmlBody.body, "text/html;charset=UTF-8")
        htmlBodyPart
      }

      for (inlineImage <- inlineImages) {
        val inlineImagePart = makeFileBodyPart(inlineImage, Some(inlineImage.filename))
        htmlMultiPart addBodyPart inlineImagePart
      }

  //    val relatedWrapper = new MimeBodyPart
  //    relatedWrapper setContent htmlMultiPart
  //    multiPart addBodyPart relatedWrapper
    }

    for (attachment <- attachments) {
      val attachmentPart = makeFileBodyPart(attachment, None)
      multiPart addBodyPart attachmentPart
    }

    message.setContent(multiPart)
    Transport.send(message)
  }

  def makeFileBodyPart(attachment: Attachment, contentID: Option[String]): MimeBodyPart = {
    val fileBodyPart = new MimeBodyPart
    fileBodyPart.setDataHandler(new DataHandler(
      new ByteArrayDataSource(attachment.content, attachment.mimeType)
    ))

    val filename = attachment.filename
    contentID match {
      case Some(cid) =>
        fileBodyPart.setContentID("<" + cid + ">")
        fileBodyPart.setDisposition("inline")
      case _ =>
        fileBodyPart.setDisposition("attachment")
    }
    fileBodyPart.setFileName(filename)

    fileBodyPart
  }
}

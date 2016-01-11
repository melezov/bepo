package test

import com.dslplatform.client.{Bootstrap, DomainProxy}
import javax.xml.bind.DatatypeConverter

import org.slf4j.{ Logger => SLF4JLogger }
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

import com.mentatlabs.bepo.api.{ email => api }
import com.mentatlabs.bepo.email._

object Main extends App {
  val slf4jLogger = LoggerFactory.getLogger("bepo-client")
  val components = new java.util.HashMap[Class[_], AnyRef]
  components.put(classOf[SLF4JLogger], slf4jLogger)

  val props = new java.util.Properties
  props.put("api-url", "http://localhost:8080/")
  props.put("package-name", "com.mentatlabs.bepo.api")
  val locator = Bootstrap.init(props, components)

  def sendEmail(email: Email) = {
    val event = new api.SendEmail()
      .setFrom(email.from.toString)
      .setReplyTo(email.replyTo.map(_.toString).orNull)
      .setTo((email.to.map(_.toString).toList.asJava))
      .setCc(email.cc.map(_.toString).toList.asJava)
      .setBcc(email.bcc.map(_.toString).toList.asJava)
      .setSubject(email.subject.title)
      .setTextBody(email.textBody.map(_.body).orNull)
      .setHtmlBody(email.htmlBody.map(_.body).orNull)
      .setAttachments(email.attachments.map(att =>
        new api.Attachment()
          .setFileName(att.filename)
          .setMimeType(att.mimeType)
          .setContent(att.content)
      ).toList.asJava)

    locator.resolve(classOf[DomainProxy]).submit(event)
  }

  for (i <- 1 to 1) {
    val email = Email(
      From("info@mentat-labs.com", Some("Mentat Labs Ltd."))
      , To("marko@mentat-labs.com")
      , Subject("Bepo test " + i)
      , TextBody("This is a test")
    )//.add(ReplyTo("info@mentat-labs.com"))
      .add(HtmlBody(
"""<h1>Hi there!</h1>
 tonaonteunatoeu envelong <b> aeotua </b>a <img src="cid:envelopeXXX.png"/> aeouoaeu
"""))
//      .add(CC("info+test2@mentat-labs.com"), BCC("info+test1@mentat-labs.com"))
      .add(Attachment("envelopeXXX.png", DatatypeConverter.parseBase64Binary(
        """
        iVBORw0KGgoAAAANSUhEUgAAABAAAAALCAMAAABBPP0LAAAAqFBMVEU/gtjj7fL9
        /v7T4uvX5e3W5OyfpKn3+vtRjbCboKZZkrRak7R6qMOTuc7R4erQ4Oltc3qNk5mQ
        lpzg6/Hl7vPa5+6ZnqT4+/yeo6hOi71TjrFXkbOEipFwd351pcF2fYSBrcZzeoGB
        iI99hIt8g4p5gIiTmZ/c6O+SmJ7f6vDm7/RudHuVmqBwdn3y9/mdoqdvdXxs2e6J
        j5ZuvZ93zKN1u46H1nuI1ntghKHUAAAAg0lEQVR4XlXONRYDQQwDUNuDy0xhZsb7
        3yxONs2q038qBFrXTrlKk9tgNl+Op0PQ1MkRavL9SZDnmJvX4057ONMOMdtmRWHe
        V6AYHEJcyygMhXk2DUNJ3F3XjoKTqSqGA/0HSkkpGTYMkef1re2pH6SEPBBCKNlC
        Qp3EwA8X34eXuM0HGwUKgH1UVeIAAAAASUVORK5CYII=""")))
      .add(Attachment("text.txt", "ovo je attachment".getBytes("UTF-8")))

    sendEmail(email)

    Thread.sleep(1000)
  }
}

package com.mentatlabs.bepo.email

import java.io.FileInputStream
import java.util.Properties
import com.typesafe.scalalogging.Logger
import org.revenj.extensibility.{Container, SystemAspect}
import org.slf4j.LoggerFactory

class MailingAspect extends SystemAspect {
  override def configure(container: Container): Unit = {
    val logger = Logger(LoggerFactory.getLogger("bepo-server"))
    container.registerInstance(logger)

    val path = sys.props("user.home") + "/.config/bepo/bepo.properties"
    val properties = new Properties
    locally {
      val fis = new FileInputStream(path)
      properties.load(fis)
      fis.close()
    }
    container.registerInstance(Credentials(properties))
    container.register(classOf[Mailer])
  }
}

package com.mentatlabs.bepo.email

import java.util.Properties

case class Credentials private(
  username: String
, password: Array[Char]
, properties: Properties
)

object Credentials {
  def apply(properties: Properties): Credentials = {
    val username = properties.remove("mail.smtp.username")
      .asInstanceOf[String]
    val password = properties.remove("mail.smtp.password")
      .asInstanceOf[String].toCharArray
    Credentials(username, password, properties)
  }
}

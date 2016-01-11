package com.mentatlabs.bepo
package email

case class Subject(title: String)

sealed abstract class EmailBody(val body: String)
case class TextBody(override val body: String) extends EmailBody(body)
case class HtmlBody(override val body: String) extends EmailBody(body)

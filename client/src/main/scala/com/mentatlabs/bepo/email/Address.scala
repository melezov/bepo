package com.mentatlabs.bepo
package email

sealed abstract class Address protected(val kind: String, val email: String, val name: Option[String]) {
  override val toString = name match {
    case Some(n) => s""""$n" <$email>"""
    case _ => email
  }
}

trait AddressBuilder[T] {
  def apply(email: String): T = apply(email, None)
  def apply(email: String, name: Option[String]): T
}

case class From(override val email: String, override val name: Option[String]) extends Address("From", email, name)
object From extends AddressBuilder[From]

case class To(override val email: String, override val name: Option[String]) extends Address("To", email, name)
object To extends AddressBuilder[To]

case class ReplyTo(override val email: String, override val name: Option[String]) extends Address("ReplyTo", email, name)
object ReplyTo extends AddressBuilder[ReplyTo]

case class CC(override val email: String, override val name: Option[String]) extends Address("CC", email, name)
object CC extends AddressBuilder[CC]

case class BCC(override val email: String, override val name: Option[String]) extends Address("BCC", email, name)
object BCC extends AddressBuilder[BCC]

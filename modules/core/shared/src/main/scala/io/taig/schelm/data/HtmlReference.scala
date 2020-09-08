package io.taig.schelm.data

final case class HtmlReference[+Event](reference: Reference[Event, HtmlReference[Event]]) extends AnyVal

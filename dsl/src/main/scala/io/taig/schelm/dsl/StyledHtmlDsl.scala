package io.taig.schelm.dsl

import io.taig.schelm._
import io.taig.schelm.css.Styles

trait StyledHtmlDsl extends Dsl[Unit, Styles] with CssDsl[Unit, Styles] {
  override def element(name: String): Widget[Nothing, Unit, Styles] =
    Widget.pure(
      Styles.Empty,
      Component.Element(name, Attributes.Empty, Listeners.empty, Children.empty)
    )

  override def text(value: String): Widget[Nothing, Unit, Styles] =
    Widget.pure(Styles.Empty, Component.Text(value))

  override def updateAttributes[A](
      widget: Widget[A, Unit, Styles],
      f: Attributes => Attributes
  ): Widget[A, Unit, Styles] =
    widget.component match {
      case component: Component.Element[Widget[A, Unit, Styles], A] =>
        val update = component.copy(attributes = f(component.attributes))
        Widget.pure(widget.payload, update)
      case Component.Lazy(eval, hash) =>
        val component = Component.Lazy(eval.map(updateAttributes(_, f)), hash)
        Widget.pure(widget.payload, component)
      case Component.Fragment(children) =>
        val component = Component.Fragment(
          children.map((_, child) => updateAttributes(child, f))
        )
        Widget.pure(widget.payload, component)
      case _: Component.Text => widget
    }

  override def updateListeners[A](
      widget: Widget[A, Unit, Styles],
      f: Listeners[A] => Listeners[A]
  ): Widget[A, Unit, Styles] =
    widget.component match {
      case component: Component.Element[Widget[A, Unit, Styles], A] =>
        val update = component.copy(listeners = f(component.listeners))
        Widget.pure(widget.payload, update)
      case Component.Lazy(eval, hash) =>
        val component = Component.Lazy(eval.map(updateListeners(_, f)), hash)
        Widget.pure(widget.payload, component)
      case Component.Fragment(children) =>
        val component = Component.Fragment(
          children.map((_, child) => updateListeners(child, f))
        )
        Widget.pure(widget.payload, component)
      case _: Component.Text => widget
    }

  override def updateChildren[A](
      widget: Widget[A, Unit, Styles],
      f: Children[Widget[A, Unit, Styles]] => Children[
        Widget[A, Unit, Styles]
      ]
  ): Widget[A, Unit, Styles] =
    widget.component match {
      case component: Component.Element[Widget[A, Unit, Styles], A] =>
        val update = component.copy(children = f(component.children))
        Widget.pure(widget.payload, update)
      case Component.Lazy(eval, hash) =>
        val component = Component.Lazy(eval.map(updateChildren(_, f)), hash)
        Widget.pure(widget.payload, component)
      case Component.Fragment(children) =>
        val component = Component.Fragment(
          children.map((_, child) => updateChildren(child, f))
        )
        Widget.pure(widget.payload, component)
      case _: Component.Text => widget
    }

  override def updateStyles[A](
      widget: Widget[A, Unit, Styles],
      f: Styles => Styles
  ): Widget[A, Unit, Styles] = widget.copy(payload = f(widget.payload))
}

object StyledHtmlDsl extends StyledHtmlDsl

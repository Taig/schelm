package io.taig.schelm

import cats.{Eq, Monoid, MonoidK}

package object data {
  type Html[F[_]] = Fix[Node[F, *]]

  object Html {
    @inline
    def apply[F[_]](node: Node[F, Html[F]]): Html[F] = Fix(node)
  }

  type StateHtml[F[_]] = Fix[λ[A => State[F, Node[F, A]]]]

  object StateHtml {
    def apply[F[_]](state: State[F, Node[F, StateHtml[F]]]): StateHtml[F] =
      Fix[λ[A => State[F, Node[F, A]]]](state)
  }

  type WidgetHtml[F[_], Context] = Fix[λ[A => Contextual[Context, Node[F, A]]]]

  object WidgetHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, Node[F, WidgetHtml[F, Context]]]
    ): WidgetHtml[F, Context] =
      Fix[λ[A => Contextual[Context, Node[F, A]]]](widget)
  }

  type WidgetStateHtml[F[_], Context] = Fix[λ[A => Contextual[Context, State[F, Node[F, A]]]]]

  object WidgetStateHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, State[F, Node[F, WidgetStateHtml[F, Context]]]]
    ): WidgetStateHtml[F, Context] =
      Fix[λ[A => Contextual[Context, State[F, Node[F, A]]]]](widget)
  }

  type StateTree[+A] = PathTree[StateTree.States[A]]

  object StateTree {
    final case class States[+A](values: Vector[A]) extends AnyVal {
      @inline
      def get(index: Int): Option[A] = values.lift(index)

      def merge[B >: A](states: States[B]): States[B] =
        if (states.values.length >= values.length) states
        else States(states.values ++ values.slice(states.values.length, values.length))
    }

    object States {
      val Empty: States[Nothing] = States(Vector.empty)

      implicit val monoidK: MonoidK[States] = new MonoidK[States] {
        override def empty[A]: States[A] = Empty

        override def combineK[A](x: States[A], y: States[A]): States[A] = x merge y
      }

      implicit def monoid[A]: Monoid[States[A]] = monoidK.algebra

      implicit def eq[A: Eq]: Eq[States[A]] = Eq.by(_.values)
    }

    val Empty: StateTree[Nothing] = PathTree(value = States.Empty, children = Map.empty)
  }

  type ListenerTree[+F[_]] = PathTree[Listeners[F]]

  object ListenerTree {
    private val EmptyChildren: Map[Key, ListenerTree[Nothing]] = Map.empty

    val Empty: ListenerTree[Nothing] = PathTree(Listeners.Empty, EmptyChildren)

    @inline
    def apply[F[_]](listeners: Listeners[F], children: Map[Key, ListenerTree[F]]): ListenerTree[F] =
      PathTree(listeners, children)

    @inline
    def fromListeners[F[_]](listeners: Listeners[F]): ListenerTree[F] = PathTree(listeners, EmptyChildren)
  }
}

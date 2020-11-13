package io.taig.schelm

import io.taig.schelm.instances.AllInstances
import io.taig.schelm.syntax.AllSyntax

trait implicits extends AllInstances with AllSyntax

object implicits extends implicits

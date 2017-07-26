/*
 * ISC License
 *
 * Copyright (c) 2017, Philipp Dörfler
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package phd

import scala.annotation.tailrec
import scala.util.{Success, Try}

object Aux {
  @tailrec
  def tail[T](lists: List[List[T]]): List[List[T]] = lists match {
    case Nil => throw new UnsupportedOperationException("list empty")
    case List() :: rps => tail(rps)
    case rp :: rps => rp match {
      case Nil => rps
      case topList => topList.tail :: rps
    }
  }

  // init == all but the last
  // Vector() => Error
  // Vector(Vector(1, 2, 3), Vector()) => init(Vector(Vector(1, 2, 3))) => Vector(Vector(1, 2))
  // Vector(Vector(1, 2, 3)) => Vector(Vector(1, 2))
  @tailrec
  def init[T](lists: Vector[Vector[T]]): Vector[Vector[T]] = lists match {
    case Vector() => throw new UnsupportedOperationException("empty vector")
    case v if v.last.isEmpty => init(v.init)
    case v => v.last.init match {
      case e if e.isEmpty => v.init
      case l => v.init :+ l
    }
  }

  def inits[T](vecs: Vector[Vector[T]]): Seq[Vector[Vector[T]]] =
    Stream.iterate(vecs)(init) takeWhile (_.nonEmpty)
}

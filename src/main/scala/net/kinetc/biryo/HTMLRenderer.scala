package net.kinetc.biryo

import net.kinetc.biryo.NamuAST._

/**
  * Created by KINETC on 2017-07-28.
  */
class HTMLRenderer {

  var fns: List[FootNote] = List()

  // TODO : 제목 / 각주 / 목차
  def generateHTML(title: String, mark: NamuMark): String = {
    mark.dfs(footNoteLister)

    // title + main paragraph + footnote list
    s"<h1>$title</h1><hr>" +
    mark.dfsMap(escapeMapper).mkString.replace("\n", "<br>") +
    fns.reverse.map(_.dfsMap(escapeMapper).mkString.replace("\n", "<br>")).mkString
  }

  def footNoteLister(mark: NamuMark): Unit = {
    mark match {
      case f @ FootNote(_, _) => fns ::= f
      case _ => ()
    }
  }

  def escapeMapper: NamuMap = {
      case RawString(s) => RawString(escapeHTML(s))
      case InlineString(s) => InlineString(escapeHTML(s))
      case FootNote(v, n) => ReverseFootNote(v, n)
    }


  def escapeHTML(s: String): String = {
    s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
  }
}
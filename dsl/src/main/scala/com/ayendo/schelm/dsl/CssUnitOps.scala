package com.ayendo.schelm.dsl

final class CssUnitOps(val value: String) extends AnyVal {
  def cm: String = value + "cm"
  def mm: String = value + "mm"
  def in: String = value + "in"
  def px: String = value + "px"
  def pt: String = value + "pt"
  def pc: String = value + "pc"
  def em: String = value + "em"
  def ex: String = value + "ex"
  def ch: String = value + "ch"
  def rem: String = value + "rem"
  def vw: String = value + "vw"
  def vh: String = value + "vh"
  def vmin: String = value + "vmin"
  def vmax: String = value + "vmax"
  def % : String = value + "%"
}

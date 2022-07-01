package example

import org.parboiled2._
import org.parboiled2.support.hlist.HNil
object FencingRuleParser  {

  def parse(input: String): FencingPredicate = {
    val triedUnit = new FencingRuleParser(input).InputLine.run()
    triedUnit.get
  }

}

class FencingRuleParser(val input:ParserInput) extends Parser{
  def InputLine = rule { Expression ~ EOI }

  def Expression: Rule1[FencingPredicate] = rule { Factor }

  def Factor:Rule1[FencingPredicate] = rule { FencingPredicate | Parens }

  def Parens: Rule1[FencingPredicate] = rule { '(' ~ Expression ~ ')' }

  def FencingPredicate:Rule1[FencingPredicate] = rule { IsRateOwnerParser }

  def IsRateOwnerParser:Rule1[FencingPredicate]  = rule{ "IsRateOwner('" ~ capture(Alphanumerics) ~ "')" ~>(name => IsRateOwner(name))}

  def Alphanumerics = rule { oneOrMore(CharPredicate.AlphaNum) }

}

trait FencingPredicate

case class And(p1: FencingPredicate, p2: FencingPredicate)
case class Or(p1: FencingPredicate, p2: FencingPredicate)
case class IsRateOwner(ownerName:String) extends FencingPredicate

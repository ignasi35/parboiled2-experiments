package example

import scala.util.Failure
import scala.util.Success

import org.parboiled2._
object FencingRuleParser  {

  def parse(input: String): FencingPredicate = {
    val triedUnit = new FencingRuleParser(input).InputLine.run()
    triedUnit match {
      case Failure(parserError:ParseError) =>
        println(parserError.toString())
        parserError.traces.foreach(        println)
        throw parserError
      case Success(value) => value
    }
  }

}

class FencingRuleParser(val input:ParserInput) extends Parser{
  def InputLine = rule { Expression ~ EOI }

  def Expression: Rule1[FencingPredicate] = rule { WS ~ Factor ~ WS  }

  def Factor:Rule1[FencingPredicate] = rule { (FencingPredicate | Parens) }

  def Parens: Rule1[FencingPredicate] = rule { '(' ~ Expression ~ ')' }

  def FencingPredicate:Rule1[FencingPredicate] = rule { IsRateOwnerParser | IsRatePlanCodeParser }

  def IsRateOwnerParser:Rule1[FencingPredicate]  = rule{ "IsRateOwner("~ WS ~ "'" ~ capture(Alphanumerics)  ~ "'" ~ WS ~ ")" ~>(name => IsRateOwner(name))}
  def IsRatePlanCodeParser:Rule1[FencingPredicate]  = rule{ "IsRatePlanCode("~ WS ~ "'" ~ capture(Alphanumerics) ~ "'" ~ WS ~")" ~>(name => IsRatePlanCode(name))}

  def Alphanumerics = rule { oneOrMore(CharPredicate.AlphaNum) }

  def WS      = rule { zeroOrMore(anyOf(" \t \n")) }
}

trait FencingPredicate

case class And(p1: FencingPredicate, p2: FencingPredicate)
case class Or(p1: FencingPredicate, p2: FencingPredicate)
case class IsRateOwner(name:String) extends FencingPredicate
case class IsRatePlanCode(name:String) extends FencingPredicate

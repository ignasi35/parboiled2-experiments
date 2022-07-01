package example

import scala.util.Failure
import scala.util.Success

import org.parboiled2._
object FencingRuleParser  {

  def parse(input: String): FencingPredicate = {
    val parser = new FencingRuleParser(input)
    val triedUnit = parser.InputLine.run()
    triedUnit match {
      case Failure(parserError:ParseError) =>
        println(parser.formatError(parserError))
        throw parserError
      case Success(value) => value
    }
  }

}

class FencingRuleParser(val input:ParserInput) extends Parser{
  def InputLine = rule { Expression ~ EOI }

  def Expression: Rule1[FencingPredicate] = rule { WS ~ Term ~ WS  }

  def Term:Rule1[FencingPredicate] = rule {
    Factor ~ zeroOrMore(
      AndParser ~ Factor ~> ( (x,y) => And(x,y))
      | OrParser ~ Factor ~> ( (x,y) => Or(x,y))
    )
  }

  def Factor:Rule1[FencingPredicate] = rule { (FencingPredicate | Parens | NotParser) }

  def Parens: Rule1[FencingPredicate] = rule { '(' ~ Expression ~ ')' }

  def AndParser = rule { WS ~ "And" ~ WS }
  def OrParser = rule { WS ~ "Or" ~ WS }
  def NotParser = rule { "Not" ~ WS ~ Factor  ~> (x => Not(x)) }

  def FencingPredicate:Rule1[FencingPredicate] = rule { IsRateOwnerParser | IsRatePlanCodeParser }

  def IsRateOwnerParser:Rule1[FencingPredicate]  = rule{ "IsRateOwner("~ WS ~ "'" ~ capture(Alphanumerics)  ~ "'" ~ WS ~ ")" ~>(name => IsRateOwner(name))}
  def IsRatePlanCodeParser:Rule1[FencingPredicate]  = rule{ "IsRatePlanCode("~ WS ~ "'" ~ capture(Alphanumerics) ~ "'" ~ WS ~")" ~>(name => IsRatePlanCode(name))}

  def Alphanumerics = rule { oneOrMore(CharPredicate.AlphaNum) }

  def WS = rule { zeroOrMore(anyOf(" \t \n")) }
}

trait FencingPredicate{
  def and(p2: FencingPredicate) = And.apply(this, p2)
  def or(p2: FencingPredicate) = Or.apply(this, p2)
}

case class And(p1: FencingPredicate, p2: FencingPredicate) extends FencingPredicate
case class Or(p1: FencingPredicate, p2: FencingPredicate) extends FencingPredicate
case class Not(p1: FencingPredicate) extends FencingPredicate
case class IsRateOwner(name:String) extends FencingPredicate
case class IsRatePlanCode(name:String) extends FencingPredicate

package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class FencingRuleParserSpec extends AnyFlatSpec with Matchers {

  /**

  IsRateOwner('Priceline')
  And (Not LeadTimeUnder('1 day') )
  And (Not IsTenant('CapitalOne') )
  And (Not
   (IsAirBooker) Or
   (IsCarBooker) Or
   (IsFintechBooker) Or
   (IsPreviousHotelBooker)
  )
    */
  behavior of "The FencingRuleParser object"

  it should "parse a IsRateOwner('name123') restriction" in {
    FencingRuleParser.parse("IsRateOwner('name123')") must be(IsRateOwner("name123"))
  }

  it should "parse a IsRatePlanCode('WMT') restriction" in {
    FencingRuleParser.parse("IsRatePlanCode('WMT')") must be(IsRatePlanCode("WMT"))
  }

  it should "parse an arbitrary amount of properly closed pairs of parens" in {
    FencingRuleParser.parse("(((IsRatePlanCode('WMT'))))") must be(IsRatePlanCode("WMT"))
  }

  it should "ignore blankspaces between predicates" in {
    FencingRuleParser.parse("   ( ( (     IsRatePlanCode(  'WMT'    ) ) )  )   ") must be(IsRatePlanCode("WMT"))
  }

  it should "parse And composed predicates" in {
    FencingRuleParser.parse("IsRatePlanCode('WMT') And IsRateOwner('Dhisco')") must be(And(IsRatePlanCode("WMT"), IsRateOwner("Dhisco")))
  }

  it should "parse Or composed predicates" in {
    FencingRuleParser.parse("IsRatePlanCode('WMT') Or IsRateOwner('Dhisco')") must be(Or(IsRatePlanCode("WMT"), IsRateOwner("Dhisco")))
  }

}

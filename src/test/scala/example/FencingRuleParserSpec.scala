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

  it should "parse a IsRateOwner('name123') restriction" in {
    FencingRuleParser.parse("IsRateOwner('name123')") must be(IsRateOwner("name123"))
  }

}

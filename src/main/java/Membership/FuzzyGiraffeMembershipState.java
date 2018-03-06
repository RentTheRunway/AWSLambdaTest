package Membership;

import Utilities.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.joda.money.Money;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

@JsonTypeName("fuzzygiraffeMembership")
public class FuzzyGiraffeMembershipState extends MembershipState {
    @Override
    public MembershipType getMembershipType() {
        return MembershipType.FUZZYGIRAFFE;
    }

    @Override
    public ReadablePeriod getMembershipTermLength() {
        return Period.days(28);
    }

    @Override
    public Integer getGracePeriodLength() {
        return 5;
    }

    @Override
    public Money getAddOnPrice() { return MoneyUtil.valueAsUSD(25.00); }

    @Override
    public Money getSwapForFitPrice() { return MoneyUtil.valueAsUSD(12.95); }

    @Override
    public Money getLateFee() { return MoneyUtil.valueAsUSD(15.00); }
}

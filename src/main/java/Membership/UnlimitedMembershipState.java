package Membership;

import Utilities.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.joda.money.Money;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

@JsonTypeName("unlimitedMembership")
public class UnlimitedMembershipState extends MembershipState {
    @Override
    public MembershipType getMembershipType() {
        return MembershipType.UNLIMITED;
    }

    @Override
    public ReadablePeriod getMembershipTermLength() {
        return Period.months(1);
    }

    @Override
    public Integer getGracePeriodLength() {
        return 5;
    }

    @Override
    public Money getAddOnPrice() { return MoneyUtil.valueAsUSD(25.0); }

    @Override
    public Money getSwapForFitPrice() { return MoneyUtil.valueAsUSD(12.95); }

    @Override
    public Money getLateFee() { return MoneyUtil.valueAsUSD(15.00); }
}

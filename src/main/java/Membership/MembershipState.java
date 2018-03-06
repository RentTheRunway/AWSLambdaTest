package Membership;

import Utilities.JodaMoneyJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.money.Money;

/**
 * This code exists to keep in sync with
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(FuzzyGiraffeMembershipState.class),
        @Type(UnlimitedMembershipState.class)
})
public abstract class MembershipState {
    public enum MembershipStatus {
        ACTIVE,
        INACTIVE
    }

    public enum MembershipBillingStatus {
        GOOD_STANDING,
        DELINQUENT,
        RETRY,
        CANCELED,
        CHURNED,
        PAUSED
    }

    public enum MembershipType {
        CLASSIC(0),
        PRO(1),
        UNLIMITED(2),
        FUZZYGIRAFFE(3),
        UNRECOGNIZED(-1);

        private final int type;

        MembershipType(int type){
            this.type = type;
        }

        int getType() {
            return type;
        }

        private static final Map<Integer, MembershipType> typeToValueMap = new HashMap<>();

        static {
            for (MembershipType value : EnumSet.allOf(MembershipType.class)) {
                typeToValueMap.put(value.type, value);
            }
        }

        public static MembershipType forType(int type) {
            if (typeToValueMap.containsKey(type)){
                return typeToValueMap.get(type);
            }
            return MembershipType.UNRECOGNIZED;
        }
    }
    public enum MembershipShipmentState {
        PREVIOUS,
        CURRENT,
        UPCOMING
    }

    private Long membershipId;
    private Long userId;
    @JsonIgnore
    private Long lastActionId;

    private MembershipStatus status;
    private MembershipBillingStatus billingStatus;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate membershipStartedOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate membershipPausedOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate membershipResumedOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate membershipEndedOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate nextBillingDate;


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate eligiblePickDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate pickByDate;

    @JsonIgnore
    private LocalDate lastReturnDate;

    private Long shipmentAddressId;
    private String shipmentPostalCode;
    private Boolean onboarded;
    private boolean hasOrderHistory;
    private List<MembershipItem> surveys;

    private Map<MembershipShipmentState, List<MembershipShipment>> shipments;
    private Integer numOfEligibleAddOnItems;
    private Integer minimumItems;

    public abstract MembershipType getMembershipType();

    public abstract ReadablePeriod getMembershipTermLength();

    public abstract Integer getGracePeriodLength();
    public Integer getNumOfEligibleAddOnItems() {
        return numOfEligibleAddOnItems;
    }

    public Integer getMinimumItems() {
        return minimumItems;
    }

    @JsonSerialize(using = JodaMoneyJsonSerializer.class)
    public abstract Money getAddOnPrice();

    @JsonSerialize(using = JodaMoneyJsonSerializer.class)
    public abstract Money getSwapForFitPrice();

    @JsonSerialize(using = JodaMoneyJsonSerializer.class)
    public abstract Money getLateFee();

    public MembershipState() {}

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public MembershipBillingStatus getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(MembershipBillingStatus billingStatus) {
        this.billingStatus = billingStatus;
    }

    public LocalDate getMembershipStartedOn() {
        return membershipStartedOn;
    }

    public void setMembershipStartedOn(LocalDate membershipStartedOn) {
        this.membershipStartedOn = membershipStartedOn;
    }

    public LocalDate getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDate nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public LocalDate getMembershipEndedOn() {
        return membershipEndedOn;
    }

    public void setMembershipEndedOn(LocalDate membershipEndedOn) {
        this.membershipEndedOn = membershipEndedOn;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getMembershipPausedOn() {
        return membershipPausedOn;
    }

    public void setMembershipPausedOn(LocalDate membershipPausedOn) {
        this.membershipPausedOn = membershipPausedOn;
    }

    public LocalDate getMembershipResumedOn() {
        return membershipResumedOn;
    }

    public void setMembershipResumedOn(LocalDate membershipResumedOn) {
        this.membershipResumedOn = membershipResumedOn;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public LocalDate getEligiblePickDate() {
        return eligiblePickDate;
    }

    public void setEligiblePickDate(LocalDate eligiblePickDate) {
        this.eligiblePickDate = eligiblePickDate;
    }

    public LocalDate getPickByDate() {
        return pickByDate;
    }

    public void setPickByDate(LocalDate pickByDate) {
        this.pickByDate = pickByDate;
    }

    public Long getShipmentAddressId() {
        return shipmentAddressId;
    }

    public void setShipmentAddressId(Long shipmentAddressId) {
        if (shipmentAddressId != null) {
            this.shipmentAddressId = shipmentAddressId;
        }
    }

    public String getShipmentPostalCode() { return shipmentPostalCode; }

    public void setShipmentPostalCode(String shipmentPostalCode) { this.shipmentPostalCode = shipmentPostalCode; }

    public Boolean getOnboarded() { return onboarded; }

    public void setOnboarded(Boolean onboarded) {
        this.onboarded = onboarded;
    }

    public Map<MembershipShipmentState, List<MembershipShipment>> getShipments() {
        return shipments;
    }

    public void setShipments(Map<MembershipShipmentState, List<MembershipShipment>> shipments) {
        this.shipments = shipments;
    }

    public void setSurveys(List<MembershipItem> surveys) {
        this.surveys = surveys;
    }

    public List<MembershipItem> getSurveys() {
        return surveys;
    }

    public boolean getHasOrderHistory() {
        return hasOrderHistory;
    }

    public void setHasOrderHistory(Boolean hasOrderHistory) {
        this.hasOrderHistory = hasOrderHistory;
    }

    public void setMinimumItems(Integer minimumItems) {
        this.minimumItems = minimumItems;
    }

    public void setNumOfEligibleAddOnItems(Integer numOfEligibleAddOnItems) {
        this.numOfEligibleAddOnItems = numOfEligibleAddOnItems;
    }

    @JsonIgnore
    public List<MembershipItem> getAllItems() {
        return this.getShipments().values()
                .stream()
                .flatMap(Collection::stream)
                .flatMap(s -> s.getMembershipItems().stream())
                .collect(Collectors.toList());
    }

    public LocalDate getLastReturnDate() {
        return lastReturnDate;
    }

    public void setLastReturnDate(LocalDate lastReturnDate) {
        this.lastReturnDate = lastReturnDate;
    }


    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "MembershipState{" +
                "membershipId=" + membershipId +
                ", userId=" + userId +
                ", status=" + status +
                ", billingStatus=" + billingStatus +
                ", membershipStartedOn=" + membershipStartedOn +
                ", membershipPausedOn=" + membershipPausedOn +
                ", membershipResumedOn=" + membershipResumedOn +
                ", membershipEndedOn=" + membershipEndedOn +
                ", nextBillingDate=" + nextBillingDate +
                ", eligiblePickDate=" + eligiblePickDate +
                ", pickByDate=" + pickByDate +
                ", lastReturnDate=" + lastReturnDate +
                ", shipmentAddressId=" + shipmentAddressId +
                ", shipmentPostalCode='" + shipmentPostalCode + '\'' +
                ", onboarded=" + onboarded +
                ", hasOrderHistory=" + hasOrderHistory +
                ", surveys=" + surveys +
                ", shipments=" + shipments +
                ", numOfEligibleAddOnItems=" + numOfEligibleAddOnItems +
                ", minimumItems=" + minimumItems +
                '}';
    }

    public Long getLastActionId() {
        return lastActionId;
    }

    public void setLastActionId(Long lastActionId) {
        this.lastActionId = lastActionId;
    }
}

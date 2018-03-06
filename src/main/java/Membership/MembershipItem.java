package Membership;


import Utilities.SkuUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static Membership.MembershipItem.MembershipItemState.AT_HOME;
import static Membership.MembershipItem.MembershipItemState.AT_HOME_TTB;
import static Membership.MembershipItem.MembershipItemState.CANCELED;
import static Membership.MembershipItem.MembershipItemState.OVERDUE;
import static Membership.MembershipItem.MembershipItemState.PREPARING;
import static Membership.MembershipItem.MembershipItemState.PURCHASED;
import static Membership.MembershipItem.MembershipItemState.RETURNED;
import static Membership.MembershipItem.MembershipItemState.RETURNING;
import static Membership.MembershipItem.MembershipItemState.RETURN_PROMISE;
import static Membership.MembershipItem.MembershipItemState.SHIPPED;

public class MembershipItem {
    public enum MembershipItemState {
        PREPARING,
        SHIPPED,
        AT_HOME,
        AT_HOME_TTB,
        RETURN_PROMISE,
        RETURNING,
        OVERDUE,
        RETURNED,
        PURCHASED,
        CANCELED
    }
    public enum MembershipItemSlotType {
        BASE,
        BASEONRACK,
        BONUS,
        SFF, // swap for fit
        QI, // quality issue
        ADDON,
        ORPHANED_ITEM // we should theoretically never see this as an 'open' slot, but its the placeholder for when something gets sent to the customer and it didn't fill a regular spot
    }

    public static final List<MembershipItemState> CAN_UPDATE_STATES = ImmutableList.of(PREPARING, SHIPPED);
    public static final List<MembershipItemState> INACTIVE_STATES = ImmutableList.of(CANCELED, RETURNED, PURCHASED);
    public static final List<MembershipItemState> ITEM_COUNT_STATES = ImmutableList.of(SHIPPED, AT_HOME, AT_HOME_TTB);
    public static final List<MembershipItemState> OWED_STATES = ImmutableList.of(RETURN_PROMISE, RETURNING, OVERDUE);

    private Long orderId;

    private String barcode;

    private String sku;

    private String productId;

    private Long bookingId;

    private List<Long> associatedBookingIds = new ArrayList<>();

    private Long groupId;

    @JsonIgnore
    private Integer tmpShipmentId;

    private MembershipItemState state;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate rentalBeginDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate onRackRentalBeginDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate returnByDate;

    @JsonIgnore
    private LocalDate originalReturnByDate;

    @JsonIgnore
    private LocalDate shippedOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate returnedOn;

    private MembershipItemSlotType type = MembershipItemSlotType.BASE;

    private ArrayList<MembershipItemState> historicalStates = Lists.newArrayList();

    private boolean didHappinessSurvey = false;

    private boolean needsHappinessSurvey = false;

    public MembershipItem(MembershipItemSlotType type) {
        this.type = type;
    }

    public MembershipItem(Long orderId,
                          String barcode,
                          String sku,
                          Long bookingId,
                          Long groupId,
                          MembershipItemState state,
                          LocalDate rentalBeginDate,
                          LocalDate onRackRentalBeginDate,
                          LocalDate returnByDate) {
        this.orderId = orderId;
        this.barcode = barcode;
        this.sku = sku;
        this.productId = SkuUtils.getStyle(sku);
        this.bookingId = bookingId;
        this.associatedBookingIds.add(bookingId);
        this.groupId = groupId;
        this.state = state;
        this.historicalStates.add(state);
        this.rentalBeginDate = rentalBeginDate;
        this.onRackRentalBeginDate = onRackRentalBeginDate;
        this.returnByDate = returnByDate;
        this.originalReturnByDate = returnByDate;
    }

    public MembershipItem() {}

    public void completedHappinessSurvey() {
        this.didHappinessSurvey = true;
    }

    public boolean canUpdate() {
        return CAN_UPDATE_STATES.contains(state);
    }

    public boolean canCancel() {
        return PREPARING.equals(state);
    }

    public LocalDate getShippedOn() {
        return shippedOn;
    }

    public MembershipItem setShippedOn(LocalDate shippedOn) {
        this.shippedOn = shippedOn;
        return this;
    }

    public boolean needsHappinessSurvey() {
        return needsHappinessSurvey;
    }

    public MembershipItem setNeedsHappinessSurvey(boolean needsHappinessSurvey) {
        this.needsHappinessSurvey = needsHappinessSurvey;
        return this;
    }

    public Long getOrderId() {
        return orderId;
    }

    public MembershipItem setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public MembershipItem setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public MembershipItem setSku(String sku) {
        this.sku = sku;
        this.productId = SkuUtils.getStyle(sku);
        return this;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public MembershipItem setBookingId(Long bookingId) {
        this.bookingId = bookingId;
        this.associatedBookingIds.add(bookingId);
        return this;
    }

    public Long getGroupId() {
        return groupId;
    }

    public MembershipItem setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public MembershipItemState getState() {
        return state;
    }

    public MembershipItem setState(MembershipItemState state) {
        this.state = state;
        this.historicalStates.add(state);
        return this;
    }

    public LocalDate getRentalBeginDate() {
        return rentalBeginDate;
    }

    public MembershipItem setRentalBeginDate(LocalDate rentalBeginDate) {
        this.rentalBeginDate = rentalBeginDate;
        return this;
    }

    public LocalDate getOnRackRentalBeginDate() {
        return onRackRentalBeginDate;
    }

    public MembershipItem setOnRackRentalBeginDate(LocalDate onRackRentalBeginDate) {
        this.onRackRentalBeginDate = onRackRentalBeginDate;
        return this;
    }

    public LocalDate getReturnByDate() {
        return returnByDate;
    }

    public MembershipItem setReturnByDate(LocalDate returnByDate) {
        if (this.getOriginalReturnByDate() == null) {
            this.originalReturnByDate = returnByDate;
        }
        this.returnByDate = returnByDate;
        if(originalReturnByDate == null) {
            this.originalReturnByDate = returnByDate;
        }
        return this;
    }

    public MembershipItemSlotType getType() {
        return type;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public MembershipItem setReturnedOn(LocalDate returnedOn) {
        this.returnedOn = returnedOn;
        return this;
    }

    public MembershipItem setType(MembershipItemSlotType type) {
        this.type = type;
        return this;
    }

    public ArrayList getHistoricalStates() {
        return historicalStates;
    }

    public Integer getTmpShipmentId() {
        return tmpShipmentId;
    }

    public MembershipItem setTmpShipmentId(Integer tmpShipmentId) {
        this.tmpShipmentId = tmpShipmentId;
        return this;
    }

    public LocalDate getOriginalReturnByDate() {
        return originalReturnByDate;
    }

    public boolean isOwed() {
        return OWED_STATES.contains(state);
    }

    public boolean isIncludedInItemCount() {
        return ITEM_COUNT_STATES.contains(state);
    }

    public String getProductId() {
        return productId;
    }

    public MembershipItem setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public boolean overdueForReturn() {
        return OVERDUE.equals(state) && !historicalStates.contains(RETURN_PROMISE);
    }

    public List<Long> getAssociatedBookingIds() {
        return associatedBookingIds;
    }
}


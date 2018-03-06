package Membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Membership.MembershipShipment.MembershipShipmentAction.NONE;
import static Membership.MembershipShipment.MembershipShipmentAction.PICK;
import static Membership.MembershipShipment.MembershipShipmentAction.RETURN;

import static Membership.MembershipItem.INACTIVE_STATES;
import static Membership.MembershipItem.MembershipItemState.AT_HOME_TTB;

public class MembershipShipment {
    public enum MembershipShipmentAction {
        PICK,
        NONE,
        RETURN
    }
    @JsonIgnore
    private Integer shipmentId;
    private Integer openAddOnSlots;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate shipmentReturnByDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate shipmentRentalBeginDate;
    private List<MembershipItem> membershipItems;

    private List<MembershipItem> slots;
    private boolean swapForFitEligible;

    public MembershipShipment(List<MembershipItem> items) {
        if (!items.isEmpty()) {
            shipmentRentalBeginDate = items.stream()
                    .map(item -> item.getRentalBeginDate())
                    .sorted()
                    .findFirst()
                    .get();
            shipmentReturnByDate = items.stream()
                    .map(item -> item.getOriginalReturnByDate())
                    .sorted()
                    .findFirst()
                    .get();
            shipmentId = items.get(0).getTmpShipmentId();
        }
        membershipItems = items;
        slots = new ArrayList<>();
        openAddOnSlots = 0;
    }

    // for jackson serialization
    public MembershipShipment() {
    }

    public MembershipShipment(Integer shipmentId) {
        this.shipmentId = shipmentId;
        this.membershipItems = new ArrayList<>();
        this.slots = new ArrayList<>();
    }

    public long getItemsOwed() {
        return membershipItems.stream()
                .filter(MembershipItem::isOwed)
                .count();
    }

    public long getItemsCount() {
        return membershipItems.stream()
                .filter(MembershipItem::isIncludedInItemCount)
                .count();
    }

    public boolean isActive() {
        membershipItems = membershipItems.stream()
                .filter(item -> !INACTIVE_STATES.contains(item.getState()))
                .collect(Collectors.toList());
        return !slots.isEmpty() ||
                !membershipItems.isEmpty();
    }

    public Integer getOpenAddOnSlots() {
        return openAddOnSlots;
    }

    public void setOpenAddOnSlots(Integer openAddOnSlots) {
        this.openAddOnSlots = openAddOnSlots;
    }

    public LocalDate getShipmentReturnByDate() {
        return shipmentReturnByDate;
    }

    public void setShipmentReturnByDate(LocalDate shipmentReturnByDate) {
        this.shipmentReturnByDate = shipmentReturnByDate;
        if (slots != null) {
            slots.forEach(slot -> slot.setReturnByDate(shipmentReturnByDate));
        }
        if (getMembershipItems() != null) {
            getMembershipItems().stream()
                    .filter(MembershipItem::canUpdate)
                    .forEach(item -> item.setReturnByDate(shipmentReturnByDate));
        }
    }

    public LocalDate getShipmentRentalBeginDate() {
        return shipmentRentalBeginDate;
    }

    public void setShipmentRentalBeginDate(MembershipRentalBegin membershipRentalBegin, Boolean nextDayEligible) {
        if (nextDayEligible || membershipRentalBegin.getOnRackRentBegin() == null) {
            this.shipmentRentalBeginDate = membershipRentalBegin.getRentBegin();
        } else {
            this.shipmentRentalBeginDate = membershipRentalBegin.getOnRackRentBegin();
        }

        if (slots != null) {
            slots.forEach(slot -> slot.setRentalBeginDate(shipmentRentalBeginDate));
            slots.forEach(slot -> slot.setOnRackRentalBeginDate(membershipRentalBegin.getOnRackRentBegin()));
        }
    }

    public List<MembershipItem> getMembershipItems() {
        return membershipItems;
    }

    public void setMembershipItems(List<MembershipItem> membershipItems) {
        this.membershipItems = membershipItems;
    }

    public List<MembershipItem> getSlots() {
        return slots;
    }

    public MembershipShipmentAction getShipmentAction() {
        if (membershipItems != null && membershipItems.stream().anyMatch(item -> AT_HOME_TTB.equals(item.getState()))) {
            return RETURN;
        }
        if (slots != null && !slots.isEmpty()) {
            return PICK;
        }
        return NONE;
    }

    public MembershipShipment setSlots(List<MembershipItem> slots) {
        this.slots = slots;
        return this;
    }

    public void setSwapForFitEligible(boolean swapForFitEligible) {
        this.swapForFitEligible = swapForFitEligible;
    }

    public boolean isSwapForFitEligible() {
        return swapForFitEligible;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }
}

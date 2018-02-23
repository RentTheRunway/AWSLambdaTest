package Membership;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MembershipRentalBegin {
    private LocalDate rentBegin;
    private LocalDate onRackRentBegin;

    @JsonCreator
    public MembershipRentalBegin(@JsonProperty("rentBegin") LocalDate rentBegin,
                                 @JsonProperty("onRackRentBegin") LocalDate onRackRentBegin) {
        this.rentBegin = rentBegin;
        this.onRackRentBegin = onRackRentBegin;
    }

    public LocalDate getRentBegin() {
        return this.rentBegin;
    }

    public LocalDate getOnRackRentBegin() {
        return this.onRackRentBegin;
    }

    @JsonProperty("onRack")
    public Boolean isOnRack() {
        return this.onRackRentBegin != null;
    }
}
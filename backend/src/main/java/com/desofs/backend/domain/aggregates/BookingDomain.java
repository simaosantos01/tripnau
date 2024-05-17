package com.desofs.backend.domain.aggregates;

import com.desofs.backend.domain.entities.PaymentEntity;
import com.desofs.backend.domain.enums.BookingStatusEnum;
import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.CreateBookingDto;
import com.desofs.backend.dtos.FetchBookingDto;
import com.desofs.backend.utils.ListUtils;
import com.desofs.backend.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class BookingDomain {

    public static int MAX_DAYS_TO_REFUND = 5;

    private final Id id;
    private final Id accountId;
    private PaymentEntity payment;
    private final IntervalTime intervalTime;
    private final List<Event> eventList;
    private ReviewDomain review;
    private final LocalDateTime createdAt;

    // Constructors ----------------------------------------------------------------------------------------------------

    public BookingDomain(Id id, Id accountId, PaymentEntity payment, IntervalTime intervalTime,
                         List<Event> eventList, ReviewDomain review, LocalDateTime createdAt) {
        notNull(id, "Id must not be null.");
        notNull(accountId, "AccountId must not be null.");
        notNull(payment, "Payment must not be null.");
        notNull(intervalTime, "IntervalTime must not be null.");
        notNull(eventList, "EventList must not be null.");
        isTrue(eventListIsValid(eventList), "EventList can't contain duplicates.");
        notNull(createdAt, "CreatedAt must not be null.");


        this.id = id.copy();
        this.accountId = accountId.copy();
        this.payment = payment.copy();
        this.intervalTime = intervalTime.copy();
        this.eventList = List.copyOf(eventList);
        this.review = review == null ? null : review.copy();
        this.createdAt = LocalDateTimeUtils.copyLocalDateTime(createdAt);
    }

    public BookingDomain(CreateBookingDto dto) {
        this(Id.create(UUID.randomUUID().toString()),
                Id.create(dto.getAccountId()),
                new PaymentEntity(dto.getPayment(), "a"), // todo: o booking id que foi criado agora
                IntervalTime.create(dto.getIntervalTime().getFrom(), dto.getIntervalTime().getTo()),
                List.of(Event.create(LocalDateTime.now(), BookingStatusEnum.BOOKED)),
                null,
                LocalDateTime.now());
    }

    private static boolean eventListIsValid(List<Event> eventList) {
        return ListUtils.hasDuplicates(eventList);
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    public Id getId() {
        return id.copy();
    }

    public Id getAccountId() {
        return accountId.copy();
    }

    public PaymentEntity getPayment() {
        return payment.copy();
    }

    public IntervalTime getIntervalTime() {
        return intervalTime.copy();
    }

    public List<Event> getEventList() {
        return List.copyOf(eventList);
    }

    public ReviewDomain getReview() {
        return review.copy();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Domain methods --------------------------------------------------------------------------------------------------

    public boolean isAlreadyCompleted() {
        return !this.eventList.stream().filter(e -> e.getState() == BookingStatusEnum.COMPLETED).toList().isEmpty();
    }

    public boolean isAlreadyCancelled() {
        return !this.eventList.stream().filter(e -> e.getState() == BookingStatusEnum.CANCELED).toList().isEmpty();
    }

    public boolean isAlreadyRefunded() {
        return !this.eventList.stream().filter(e -> e.getState() == BookingStatusEnum.REFUNDED).toList().isEmpty();
    }

    public boolean checkoutPassed() {
        Date now = new Date();
        return (now.equals(this.intervalTime.getTo()) || now.before(this.intervalTime.getTo()));
    }

    public int daysUntilCheckout() {
        Date now = new Date();

        long differenceInMillis = now.getTime() - this.intervalTime.getTo().getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(differenceInMillis);
    }

    private void addEvent(Event event) {
        boolean alreadyExists = !this.eventList.stream().filter(e -> e.getState() == event.getState()).toList().isEmpty();
        if (alreadyExists) {
            throw new IllegalArgumentException("The event type already exists");
        }
        this.eventList.add(event);
    }

    /**
     * @return If it's possible to do a refund or not
     */
    public boolean cancel() {
        if (this.isAlreadyCompleted()) {
            throw new IllegalArgumentException("The booking is already completed");
        }

        if (this.isAlreadyCancelled()) {
            throw new IllegalArgumentException("The booking is already cancelled");
        }

        if (this.checkoutPassed()) {
            throw new IllegalArgumentException("The checkout date has passed");
        }

        this.addEvent(Event.create(LocalDateTime.now(), BookingStatusEnum.CANCELED));
        return this.daysUntilCheckout() >= MAX_DAYS_TO_REFUND;
    }

    public void refund() {
        if (this.isAlreadyCompleted()) {
            throw new IllegalArgumentException("The booking is already completed");
        }

        if (!this.isAlreadyCancelled()) {
            throw new IllegalArgumentException("The booking must have a cancelled event before the refund");
        }

        if (this.isAlreadyRefunded()) {
            throw new IllegalArgumentException("The booking it was already refunded");
        }

        if (this.daysUntilCheckout() >= MAX_DAYS_TO_REFUND) {
            throw new IllegalArgumentException("The refund acceptance date has passed");
        }

        this.addEvent(Event.create(LocalDateTime.now(), BookingStatusEnum.REFUNDED));
    }

    public void writeReview(ReviewDomain review) {
        if (this.review != null) {
            throw new IllegalArgumentException("There is already a review associated with the booking");
        }
        this.review = review;
    }

}

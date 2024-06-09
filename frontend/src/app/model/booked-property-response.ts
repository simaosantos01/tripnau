import { BookingStatusEnum } from "./bookingStatus.enum";
import { IntervalTime } from "./interval-time";
import { Review } from "./review";

export interface BookedPropertyResponse {
    id: string;
    accountId: string;
    propertyId: string;
    intervalTime: IntervalTime;
    status: BookingStatusEnum;
    review: Review;
    createdAt: Date;
}
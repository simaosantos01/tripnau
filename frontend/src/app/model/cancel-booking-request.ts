import { IntervalTime } from "./interval-time";

export interface CancelBookingRequest {
    accountId: string;
    propertyId: string;
    intervalTime: IntervalTime;
}
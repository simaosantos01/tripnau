import { Interval } from "./intervals";

export interface BookPropertyRequest {
    propertyId: string,
    intervalTime: Interval,
    successUrl: string
}